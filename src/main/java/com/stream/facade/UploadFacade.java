package com.stream.facade;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.stream.domain.video.Video;
import com.stream.domain.video.VideoService;
import com.stream.domain.video.dto.UploadVideoDto;
import com.stream.domain.video.exception.EmptyFileUploadException;
import com.stream.storage.StorageStrategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UploadFacade {
	private final VideoService videoService;
	private final StorageStrategy storageStrategy;

	@Autowired
	public UploadFacade(VideoService videoService, StorageStrategy storageStrategy) {
		this.videoService = videoService;
		this.storageStrategy = storageStrategy;
	}

	public CompletableFuture uploadVideoSync(UploadVideoDto videoMetadata, MultipartFile videoForUpload) {
		try {
			if (videoForUpload.isEmpty()) {
				throw new EmptyFileUploadException();
			}
			String savingPath = storageStrategy.uploadFileAndReturnPath(videoMetadata, videoForUpload);

			execPostSave(videoMetadata, savingPath, videoForUpload.getSize());
		} catch (Exception err) {
			// TODO: 로거 도입
			log.error(err.getMessage(), err.fillInStackTrace());
		}
		return CompletableFuture.completedFuture(null);
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
