package com.luiz.backend.mappers;

import com.luiz.backend.dtos.UserDto;
import com.luiz.backend.dtos.UserUpdateRequest;
import com.luiz.backend.entity.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-12T10:27:02-0300",
    comments = "version: 1.6.3, compiler: Eclipse JDT (IDE) 3.42.0.v20250514-1000, environment: Java 21.0.7 (Eclipse Adoptium)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDto toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto userDto = new UserDto();

        userDto.setBio( user.getBio() );
        userDto.setCreatedAt( user.getCreatedAt() );
        userDto.setEmail( user.getEmail() );
        userDto.setId( user.getId() );
        userDto.setProfilePictureUrl( user.getProfilePictureUrl() );
        userDto.setUsername( user.getUsername() );

        return userDto;
    }

    @Override
    public void update(UserUpdateRequest request, User user) {
        if ( request == null ) {
            return;
        }

        user.setBio( request.getBio() );
        user.setEmail( request.getEmail() );
        user.setUsername( request.getUsername() );
    }
}
