package com.luiz.pin_board_service.services;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.luiz.pin_board_service.clients.BoardServiceClient;
import com.luiz.pin_board_service.clients.PinServiceClient;
import com.luiz.pin_board_service.dtos.BoardDto;
import com.luiz.pin_board_service.dtos.PinDto;
import com.luiz.pin_board_service.entity.PinBoard;
import com.luiz.pin_board_service.exceptions.PinBoardNotFoundException;
import com.luiz.pin_board_service.exceptions.UnauthorizedException;
import com.luiz.pin_board_service.repository.PinBoardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PinBoardServiceImpl implements PinBoardService {
    
    private final PinBoardRepository repository;
    private final PinServiceClient pinClient;
    private final BoardServiceClient boardClient;
    
    @Override
    public Page<PinDto> getPinsOnBoard(UUID boardId, Pageable pageable) {
        Page<PinBoard> pinBoards = repository.findByBoardId(boardId, pageable);
        List<UUID> pinIds = pinBoards.stream().map(PinBoard::getPinId).toList();
        List<PinDto> pins = pinClient.getPinsByIds(pinIds);

        return new PageImpl<>(pins, pageable, pinBoards.getTotalElements());
    }
    
    @Override
    public Page<BoardDto> getBoardsWithPin(UUID pinId, Pageable pageable) {
        Page<PinBoard> pinBoards = repository.findByPinId(pinId, pageable);
        List<UUID> boardIds = pinBoards.stream().map(PinBoard::getBoardId).toList();
        List<BoardDto> boards = boardClient.getBoardsByIds(boardIds);

        return new PageImpl<>(boards, pageable, pinBoards.getTotalElements());
    }
    
    @Override
    public PinBoard addPinToBoard(UUID pinId, UUID boardId, UUID userId) {
        PinBoard pinBoard = new PinBoard();
        pinBoard.setPinId(pinId);
        pinBoard.setBoardId(boardId);
        repository.save(pinBoard);
        
        return pinBoard;
    }
    
    @Override
    public void removePinFromBoard(UUID pinId, UUID boardId, UUID userId) {
        PinBoard pinBoard = repository.findByBoardIdAndPinId(boardId, pinId).orElseThrow(() -> new PinBoardNotFoundException("PinBoard not found with pinId " + pinId + "and boardId " + boardId));
        BoardDto board = boardClient.getBoardById(userId);

        if (!board.getUserId().equals(userId)) {
            throw new UnauthorizedException("Current user is not the owner of this board");
        }
        
        repository.delete(pinBoard);
    }
}
