package com.stream.storage;

import com.stream.domain.video.dto.UploadVideoDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.UUID;

public class DummyStorageStrategy implements StorageStrategy {
  @Override
  // Mock InputStream Ref: https://www.baeldung.com/java-mocking-inputstream
  public InputStream createFileStream(String path) {
    return new ByteArrayInputStream(path.getBytes());
  }

  @Override
  public String uploadFileAndReturnPath(UploadVideoDto videoMetadata, MultipartFile videoForSave) {
    return Paths.get("/for-test", UUID.randomUUID() + "." + videoMetadata.getExtension()).toString();
  }
}
