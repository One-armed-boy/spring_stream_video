package com.stream.domain.video.dto;

import com.stream.domain.video.Video;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class VideoDto {
  private long id;
  private String fileTag;
  private String extension;
  private long size;
  private String description;
  private LocalDateTime createdAt;

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
