package com.luiz.backend.dtos;

import java.util.List;

import lombok.Data;

@Data
public class PageDto<T> {
  private List<T> content;
  private int number;
  private int size;
  private int totalPages;
  private long totalElements;
}
