package com.stream.video.service;

import com.stream.video.dto.VideoDto;
import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;
import java.util.List;

public interface VideoService {
    List<VideoDto> listVideo();
    VideoDto getVideoMetadata(long id);
    Resource createVideoStream(long id) throws FileNotFoundException;
}
