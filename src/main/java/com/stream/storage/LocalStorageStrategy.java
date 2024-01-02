package com.stream.storage;

import com.stream.domain.video.dto.UploadVideoDto;
import com.stream.storage.exception.FileStreamingException;
import com.stream.storage.exception.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Paths;
import java.util.UUID;

public class LocalStorageStrategy implements StorageStrategy {
  @Value("${my.app.storage.localStorageDir}")
  private String videoDir;

  @Override
  public InputStream createFileStream(String path) {
    try {
      return new FileInputStream(path);
    } catch (Exception err) {
      throw new FileStreamingException(err);
    }
  }

  @Override
  public String uploadFileAndReturnPath(UploadVideoDto videoMetadata, MultipartFile videoForUpload) {
    try {
      File targetDir = new File(videoDir);
      checkTargetDir(targetDir);

      String targetPath = Paths.get(targetDir.getAbsolutePath(), createRandomName(videoMetadata.getExtension()))
              .toAbsolutePath()
              .toString();
      File targetFile = new File(targetPath);
      saveFile(targetFile, videoForUpload);
      return targetPath;
    } catch (Exception err) {
      throw new FileUploadException(err);
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
}
