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

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
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
            .map(video -> VideoDto.convertDomainToDto(video))
            .toList();
  }

  @Override
  public VideoDto getVideoMetadata(long id) throws EntityNotFoundException {
    Video video = videoRepository.getReferenceById(id);
    return VideoDto.convertDomainToDto(video);
  }

  @Override
  @Async
  public CompletableFuture uploadVideoAsync(UploadVideoDto videoMetadata, MultipartFile videoForUpload) {
    try {
      if (videoForUpload.isEmpty()) {
        throw new EmptyFileUploadException();
      }
      File targetDir = new File(videoDir);
      checkTargetDir(targetDir);

      String targetPath = Paths.get(targetDir.getAbsolutePath(), createRandomName(videoMetadata.getExtension()))
              .toAbsolutePath()
              .toString();
      File targetFile = new File(targetPath);

      saveFile(targetFile, videoForUpload);

      execPostSave(videoMetadata, targetPath, videoForUpload.getSize());
    } catch (Exception err) {
      // TODO: 로거 도입
      System.out.println(err);
    } finally {
      return CompletableFuture.completedFuture(null);
    }
  }

  private void checkTargetDir(File targetDir) {
    if (targetDir.exists()) {
      return;
    }
    targetDir.mkdir();
  }

  private void saveFile(File targetFile, MultipartFile videoData) throws IOException {
    InputStream inputStream = videoData.getInputStream();
    OutputStream outputStream = new FileOutputStream(targetFile);

    byte[] buffer = new byte[1024];
    int bytesRead;

    while ((bytesRead = inputStream.read(buffer)) != -1) {
      outputStream.write(buffer, 0, bytesRead);
    }
  }

  private String createRandomName(String extension) {
    return UUID.randomUUID() + "." + extension;
  }

  private void execPostSave(UploadVideoDto videoMetadata, String targetPath, long fileSize) {
    System.out.println(videoMetadata.getExtension());
    videoRepository.save(Video.builder()
            .fileTag(videoMetadata.getFileName())
            .extension(videoMetadata.getExtension())
            .path(targetPath)
            .size(fileSize)
            .description(videoMetadata.getDescription())
            .build());
    // TODO: SSE를 통한 저장 완료 이벤트 처리
  }

  @Override
  public Resource createVideoStream(long id) throws FileNotFoundException {
    Video video = videoRepository.getReferenceById(id);
    Path videoPath = Paths.get(video.getPath());
    return new InputStreamResource(new FileInputStream(videoPath.toString()));
  }
}
