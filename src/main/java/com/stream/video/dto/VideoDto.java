package com.stream.video.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class VideoDto {
    public long id;
    public String fileName;
    public String extension;
    public long size;
    public String description;
    public LocalDateTime createdAt;
}
