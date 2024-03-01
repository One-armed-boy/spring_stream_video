package com.stream.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.stream.controller.dto.upload.UploadVideoRequest;
import com.stream.domain.video.dto.upload.UploadVideoCommand;
import com.stream.facade.UploadFacade;
import com.stream.security.SecurityContextHelper;

import jakarta.validation.Valid;

@RestController
public class UploadController {
	private final UploadFacade uploadFacade;
	private final SecurityContextHelper securityContextHelper;

	@Autowired
	public UploadController(UploadFacade uploadFacade, SecurityContextHelper securityContextHelper) {
		this.uploadFacade = uploadFacade;
		this.securityContextHelper = securityContextHelper;
	}

	@PostMapping(path = "/videos/upload")
	public ResponseEntity uploadVideo(@Valid @RequestPart(value = "videoMetadata") UploadVideoRequest request,
		@RequestPart(value = "videoFile") MultipartFile videoFile) {
		var member = securityContextHelper.getMember();
		uploadFacade.uploadVideoSync(UploadVideoCommand.builder()
			.fileName(request.getFileName())
			.extension(request.getExtension())
			.description(request.getDescription())
			.member(member)
			.build(), videoFile);
		return ResponseEntity.ok().build();
	}
}
