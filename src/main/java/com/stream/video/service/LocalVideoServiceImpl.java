package com.stream.video.service;

import com.stream.video.domain.Video;
import com.stream.video.dto.UploadVideoDto;
import com.stream.video.dto.VideoDto;
import com.stream.video.repository.VideoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@Qualifier("LocalVideoService")
public class LocalVideoServiceImpl implements VideoService {
  @Autowired
  private VideoRepository videoRepository;

  @Value("${video.dir}")
  String videoDir;

  @Override
  public List<VideoDto> listVideo() {
    List<Video> videoList = videoRepository.findAll();
    return videoList.stream()
            .map(video -> convertDomainToDto(video))
            .toList();
  }

  @Override
  public VideoDto getVideoMetadata(long id) throws EntityNotFoundException {
    Video video = videoRepository.getReferenceById(id);
    return convertDomainToDto(video);
  }

  @Override
  @Async
  public CompletableFuture uploadVideoAsync(UploadVideoDto videoMetadata, MultipartFile videoFile) {
    // TODO: 비동기 파일 업로드 로직 구현
    return CompletableFuture.completedFuture(null);
  }

  @Override
  public Resource createVideoStream(long id) throws FileNotFoundException {
    Video video = videoRepository.getReferenceById(id);
    Path videoPath = Paths.get(video.getPath());
    return new InputStreamResource(new FileInputStream(videoPath.toString()));
  }

  private VideoDto convertDomainToDto(Video video) {
    return VideoDto.builder()
            .id(video.getId())
            .fileName(video.getFileName())
            .size(video.getSize())
            .description(video.getDescription())
            .createdAt(video.getCreatedAt())
            .build();
  }
}
