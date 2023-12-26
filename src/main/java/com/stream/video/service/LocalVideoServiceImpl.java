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
  public CompletableFuture uploadVideoAsync(UploadVideoDto videoMetadata, MultipartFile videoFile) {
    try {
      if (videoFile.isEmpty()) {
        throw new EmptyFileUploadException();
      }
      File targetDir = new File(videoDir);
      checkTargetDir(targetDir);

      File fileForUpload = new File(targetDir.getAbsoluteFile(), videoMetadata.getFileName() + "." +
              videoMetadata.getExtension()
      );

      writeFile(fileForUpload, videoFile);
      // TODO: 파일 저장 후 후처리 로직 작성
    } catch (Exception err) {
      // TODO: 로거 도입
      System.out.println(err);
    } finally {
      return CompletableFuture.completedFuture(null);
    }
  }

  private void writeFile(File fileForUpload, MultipartFile videoData) throws IOException {
    InputStream inputStream = videoData.getInputStream();
    OutputStream outputStream = new FileOutputStream(fileForUpload);

    byte[] buffer = new byte[1024];
    int bytesRead;

    while ((bytesRead = inputStream.read(buffer)) != -1) {
      outputStream.write(buffer, 0, bytesRead);
    }
  }

  private void checkTargetDir(File targetDir) {
    if (targetDir.exists()) {
      return;
    }
    targetDir.mkdir();
  }

  @Override
  public Resource createVideoStream(long id) throws FileNotFoundException {
    Video video = videoRepository.getReferenceById(id);
    Path videoPath = Paths.get(video.getPath());
    return new InputStreamResource(new FileInputStream(videoPath.toString()));
  }
}
