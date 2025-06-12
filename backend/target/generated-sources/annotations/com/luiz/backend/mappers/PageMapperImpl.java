package com.luiz.backend.mappers;

import com.luiz.backend.dtos.BoardDto;
import com.luiz.backend.dtos.PageDto;
import com.luiz.backend.dtos.PinDto;
import com.luiz.backend.dtos.UserDto;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-12T10:27:02-0300",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class PageMapperImpl implements PageMapper {

    @Override
    public PageDto<PinDto> toPinDto(Page<PinDto> page) {
        if ( page == null ) {
            return null;
        }

        PageDto<PinDto> pageDto = new PageDto<PinDto>();

        if ( page.hasContent() ) {
            List<PinDto> list = page.getContent();
            pageDto.setContent( new ArrayList<PinDto>( list ) );
        }
        pageDto.setNumber( page.getNumber() );
        pageDto.setSize( page.getSize() );
        pageDto.setTotalElements( page.getTotalElements() );
        pageDto.setTotalPages( page.getTotalPages() );

        return pageDto;
    }

    @Override
    public PageDto<BoardDto> toBoardDto(Page<BoardDto> boards) {
        if ( boards == null ) {
            return null;
        }

        PageDto<BoardDto> pageDto = new PageDto<BoardDto>();

        if ( boards.hasContent() ) {
            List<BoardDto> list = boards.getContent();
            pageDto.setContent( new ArrayList<BoardDto>( list ) );
        }
        pageDto.setNumber( boards.getNumber() );
        pageDto.setSize( boards.getSize() );
        pageDto.setTotalElements( boards.getTotalElements() );
        pageDto.setTotalPages( boards.getTotalPages() );

        return pageDto;
    }

    @Override
    public PageDto<UserDto> toUserDto(Page<UserDto> users) {
        if ( users == null ) {
            return null;
        }

        PageDto<UserDto> pageDto = new PageDto<UserDto>();

        if ( users.hasContent() ) {
            List<UserDto> list = users.getContent();
            pageDto.setContent( new ArrayList<UserDto>( list ) );
        }
        pageDto.setNumber( users.getNumber() );
        pageDto.setSize( users.getSize() );
        pageDto.setTotalElements( users.getTotalElements() );
        pageDto.setTotalPages( users.getTotalPages() );

        return pageDto;
    }
}
