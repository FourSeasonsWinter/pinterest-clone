package com.luiz.backend.mappers;

import com.luiz.backend.dtos.FollowDto;
import com.luiz.backend.entity.Follow;
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
public class FollowMapperImpl implements FollowMapper {

    @Override
    public FollowDto toDto(Follow follow) {
        if ( follow == null ) {
            return null;
        }

        FollowDto followDto = new FollowDto();

        followDto.setFollowerId( followFollowerId( follow ) );
        followDto.setFollowedById( followFollowedById( follow ) );

        return followDto;
    }

    private UUID followFollowerId(Follow follow) {
        User follower = follow.getFollower();
        if ( follower == null ) {
            return null;
        }
        return follower.getId();
    }

    private UUID followFollowedById(Follow follow) {
        User followedBy = follow.getFollowedBy();
        if ( followedBy == null ) {
            return null;
        }
        return followedBy.getId();
    }
}
