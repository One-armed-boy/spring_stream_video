package com.stream.facade;

import com.stream.domain.video.VideoService;
import com.stream.storage.StorageStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StreamingFacade {
  private final VideoService videoService;
  private final StorageStrategy storageStrategy;

  @Autowired
  public StreamingFacade(VideoService videoService, StorageStrategy storageStrategy) {
    this.videoService = videoService;
    this.storageStrategy = storageStrategy;
  }

  public Resource createVideoStream(long videoId) {
    String path = videoService.getVideoFilePath(videoId);
    Path absPath = Paths.get(path).toAbsolutePath();
    return new InputStreamResource(storageStrategy.createFileStream(absPath.toString()));
  }
}
