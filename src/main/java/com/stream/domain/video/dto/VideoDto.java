package com.stream.domain.video.dto;

import com.stream.domain.video.Video;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class VideoDto {
  public long id;
  public String fileTag;
  public String extension;
  public long size;
  public String description;
  public LocalDateTime createdAt;

  public static VideoDto convertDomainToDto(Video video) {
    return VideoDto.builder()
            .id(video.getId())
            .fileTag(video.getFileTag())
            .extension(video.getExtension())
            .size(video.getSize())
            .description(video.getDescription())
            .createdAt(video.getCreatedAt())
            .build();
  }
}
