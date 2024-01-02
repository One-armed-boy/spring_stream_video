package com.stream.facade;

import com.stream.domain.video.Video;
import com.stream.domain.video.VideoService;
import com.stream.domain.video.dto.UploadVideoDto;
import com.stream.domain.video.exception.EmptyFileUploadException;
import com.stream.storage.StorageStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class UploadFacade {
  @Autowired
  private VideoService videoService;

  @Autowired
  private StorageStrategy storageStrategy;

  public CompletableFuture uploadVideoSync(UploadVideoDto videoMetadata, MultipartFile videoForUpload) {
    try {
      if (videoForUpload.isEmpty()) {
        throw new EmptyFileUploadException();
      }
      String savingPath = storageStrategy.uploadFileAndReturnPath(videoMetadata, videoForUpload);

      execPostSave(videoMetadata, savingPath, videoForUpload.getSize());
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
    videoService.createVideo(Video.builder()
            .fileTag(videoMetadata.getFileName())
            .extension(videoMetadata.getExtension())
            .path(targetPath)
            .size(fileSize)
            .description(videoMetadata.getDescription())
            .build());
    // TODO: SSE를 통한 저장 완료 이벤트 처리
  }
}
