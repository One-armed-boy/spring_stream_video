package com.stream.storage;

import com.stream.domain.video.dto.UploadVideoDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface StorageStrategy {
  InputStream createFileStream(String path);

  String uploadFileAndReturnPath(UploadVideoDto videoMetadata, MultipartFile videoForSave);
}
