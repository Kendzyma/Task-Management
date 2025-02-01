package com.taskmanagement.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PaginatedResponse<T> {
  private List<T> content;
  private int pageNumber;
  private int pageSize;

  public PaginatedResponse(
      List<T> content, int pageNumber, int pageSize, long totalElements, int totalPages) {
    this.content = content;
    this.pageNumber = pageNumber;
    this.pageSize = pageSize;
    this.totalElements = totalElements;
    this.totalPages = totalPages;
  }

  private long totalElements;
  private int totalPages;

  public PaginatedResponse(Page<T> page) {
    this.content = page.getContent();
    this.pageNumber = page.getNumber();
    this.pageSize = page.getSize();
    this.totalElements = page.getTotalElements();
    this.totalPages = page.getTotalPages();
  }
}
