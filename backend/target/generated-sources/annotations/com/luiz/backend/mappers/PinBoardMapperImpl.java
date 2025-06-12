package com.luiz.backend.mappers;

import com.luiz.backend.dtos.PinBoardDto;
import com.luiz.backend.entity.Board;
import com.luiz.backend.entity.Pin;
import com.luiz.backend.entity.PinBoard;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-12T10:27:02-0300",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class PinBoardMapperImpl implements PinBoardMapper {

    @Override
    public PinBoardDto toDto(PinBoard pinBoard) {
        if ( pinBoard == null ) {
            return null;
        }

        PinBoardDto pinBoardDto = new PinBoardDto();

        pinBoardDto.setPinId( pinBoardPinId( pinBoard ) );
        pinBoardDto.setBoardId( pinBoardBoardId( pinBoard ) );
        pinBoardDto.setId( pinBoard.getId() );

        return pinBoardDto;
    }

    private UUID pinBoardPinId(PinBoard pinBoard) {
        Pin pin = pinBoard.getPin();
        if ( pin == null ) {
            return null;
        }
        return pin.getId();
    }

    private UUID pinBoardBoardId(PinBoard pinBoard) {
        Board board = pinBoard.getBoard();
        if ( board == null ) {
            return null;
        }
        return board.getId();
    }
}
