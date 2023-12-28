package com.stream.domain.video;

import com.stream.domain.video.dto.VideoDto;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

public interface VideoService {
  List<VideoDto> listVideo();

  VideoDto getVideoMetadata(long id) throws EntityNotFoundException;

  String getVideoFilePath(long id) throws EntityNotFoundException;

  void createVideo(Video... video);
}
