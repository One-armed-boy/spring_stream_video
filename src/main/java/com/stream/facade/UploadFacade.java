package com.stream.facade;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.stream.domain.member.MemberService;
import com.stream.domain.video.VideoService;
import com.stream.domain.video.dto.CreateVideoCommand;
import com.stream.domain.video.dto.upload.UploadVideoCommand;
import com.stream.domain.video.exception.EmptyFileUploadException;
import com.stream.storage.StorageStrategy;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UploadFacade {
	private final VideoService videoService;
	private final MemberService memberService;
	private final StorageStrategy storageStrategy;

	@Autowired
	public UploadFacade(VideoService videoService, MemberService memberService, StorageStrategy storageStrategy) {
		this.videoService = videoService;
		this.memberService = memberService;
		this.storageStrategy = storageStrategy;
	}

	public CompletableFuture<Void> uploadVideoSync(UploadVideoCommand command, MultipartFile videoForUpload) {
		try {
			if (videoForUpload.isEmpty()) {
				throw new EmptyFileUploadException();
			}
			log.info("uploadVideoSync 호출");
			String savingPath = storageStrategy.uploadFileAndReturnPath(command, videoForUpload);

			execPostSave(command, savingPath, videoForUpload.getSize());
		} catch (Exception err) {
			log.error(err.getMessage(), err.fillInStackTrace());
		}
		return CompletableFuture.completedFuture(null);
	}

	private void execPostSave(UploadVideoCommand command, String targetPath, long fileSize) {
		videoService.createVideo(CreateVideoCommand.builder()
			.fileTag(command.getFileName())
			.extension(command.getExtension())
			.path(targetPath)
			.size(fileSize)
			.description(command.getDescription())
			.memberEmail(command.getMember())
			.build());
		// TODO: SSE를 통한 저장 완료 이벤트 처리
	}
}
