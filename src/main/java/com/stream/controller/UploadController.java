package com.stream.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.stream.domain.video.dto.UploadVideoDto;
import com.stream.facade.UploadFacade;

import jakarta.validation.Valid;

@RestController
public class UploadController {
	private final UploadFacade uploadFacade;

	@Autowired
	public UploadController(UploadFacade uploadFacade) {
		this.uploadFacade = uploadFacade;
	}

	@PostMapping(path = "/videos/upload")
	public ResponseEntity uploadVideo(@Valid @RequestPart(value = "videoMetadata") UploadVideoDto videoMetadata,
		@RequestPart(value = "videoFile") MultipartFile videoFile) {
		uploadFacade.uploadVideoSync(videoMetadata, videoFile);
		return ResponseEntity.ok().build();
	}
}
