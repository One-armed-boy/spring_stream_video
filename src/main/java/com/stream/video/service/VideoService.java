package com.stream.video.service;

import com.stream.video.dto.VideoDto;
import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;

public interface VideoService {
    public VideoDto getVideoMetadata(String id) throws FileNotFoundException;
    public Resource createVideoStream(VideoDto videoDto) throws FileNotFoundException;
}
