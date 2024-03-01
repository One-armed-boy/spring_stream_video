package com.stream.storage;

import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

import com.stream.domain.video.dto.upload.UploadVideoCommand;

public interface StorageStrategy {
	InputStream createFileStream(String path);

	String uploadFileAndReturnPath(UploadVideoCommand videoMetadata, MultipartFile videoForSave);
}
