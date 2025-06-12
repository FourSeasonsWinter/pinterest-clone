package com.luiz.backend.mappers;

import com.luiz.backend.dtos.BoardDto;
import com.luiz.backend.dtos.BoardPostRequest;
import com.luiz.backend.dtos.BoardUpdateRequest;
import com.luiz.backend.entity.Board;
import com.luiz.backend.entity.User;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-12T10:27:02-0300",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class BoardMapperImpl implements BoardMapper {

    @Override
    public BoardDto toDto(Board board) {
        if ( board == null ) {
            return null;
        }

        BoardDto boardDto = new BoardDto();

        boardDto.setUserId( boardUserId( board ) );
        boardDto.setUsername( boardUserUsername( board ) );
        boardDto.setCreatedAt( board.getCreatedAt() );
        boardDto.setDescription( board.getDescription() );
        boardDto.setId( board.getId() );
        boardDto.setName( board.getName() );
        boardDto.setPrivate( board.isPrivate() );
        boardDto.setUpdatedAt( board.getUpdatedAt() );

        return boardDto;
    }

    @Override
    public Board toEntity(BoardPostRequest request) {
        if ( request == null ) {
            return null;
        }

        Board board = new Board();

        board.setName( request.getName() );

        return board;
    }

    @Override
    public void update(BoardUpdateRequest request, Board board) {
        if ( request == null ) {
            return;
        }

        board.setDescription( request.getDescription() );
        board.setName( request.getName() );
        board.setPrivate( request.isPrivate() );
    }

    private UUID boardUserId(Board board) {
        User user = board.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getId();
    }

    private String boardUserUsername(Board board) {
        User user = board.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getUsername();
    }
}
