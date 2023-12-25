package com.stream.video.service;

import com.stream.video.dto.UploadVideoDto;
import com.stream.video.dto.VideoDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface VideoService {
  List<VideoDto> listVideo();

  VideoDto getVideoMetadata(long id) throws EntityNotFoundException;

  CompletableFuture uploadVideoAsync(UploadVideoDto videoMetadata, MultipartFile videoFile);

  Resource createVideoStream(long id) throws FileNotFoundException;
}
