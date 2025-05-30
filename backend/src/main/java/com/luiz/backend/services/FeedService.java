package com.luiz.backend.services;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.luiz.backend.dtos.PinDto;

public interface FeedService {
  Page<PinDto> getFeed(UUID userId, Pageable pageable);
}
