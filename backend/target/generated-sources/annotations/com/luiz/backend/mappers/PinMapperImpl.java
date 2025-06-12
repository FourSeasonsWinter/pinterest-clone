package com.luiz.backend.mappers;

import com.luiz.backend.dtos.PinDto;
import com.luiz.backend.dtos.PinPostRequest;
import com.luiz.backend.entity.Pin;
import com.luiz.backend.entity.User;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-12T10:27:01-0300",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class PinMapperImpl implements PinMapper {

    @Override
    public PinDto toDto(Pin pin) {
        if ( pin == null ) {
            return null;
        }

        PinDto pinDto = new PinDto();

        pinDto.setUserId( pinUserId( pin ) );
        pinDto.setCreatedAt( pin.getCreatedAt() );
        pinDto.setDescription( pin.getDescription() );
        pinDto.setId( pin.getId() );
        pinDto.setImageUrl( pin.getImageUrl() );
        pinDto.setSourceLink( pin.getSourceLink() );
        pinDto.setTitle( pin.getTitle() );

        return pinDto;
    }

    @Override
    public Pin toEntity(PinPostRequest request) {
        if ( request == null ) {
            return null;
        }

        Pin pin = new Pin();

        pin.setDescription( request.getDescription() );
        pin.setImageUrl( request.getImageUrl() );
        pin.setSourceLink( request.getSourceLink() );
        pin.setTitle( request.getTitle() );

        return pin;
    }

    @Override
    public Pin toEntity(PinDto dto) {
        if ( dto == null ) {
            return null;
        }

        Pin pin = new Pin();

        pin.setCreatedAt( dto.getCreatedAt() );
        pin.setDescription( dto.getDescription() );
        pin.setId( dto.getId() );
        pin.setImageUrl( dto.getImageUrl() );
        pin.setSourceLink( dto.getSourceLink() );
        pin.setTitle( dto.getTitle() );

        return pin;
    }

    private UUID pinUserId(Pin pin) {
        User user = pin.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getId();
    }
}
