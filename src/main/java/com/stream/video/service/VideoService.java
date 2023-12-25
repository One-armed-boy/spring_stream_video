package com.stream.video.service;

import com.stream.video.dto.VideoDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;
import java.util.List;

public interface VideoService {
    List<VideoDto> listVideo();
    VideoDto getVideoMetadata(long id) throws EntityNotFoundException;
    Resource createVideoStream(long id) throws FileNotFoundException;
}
