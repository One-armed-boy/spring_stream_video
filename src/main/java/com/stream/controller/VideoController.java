package com.stream.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.stream.controller.dto.video.GetVideoResponse;
import com.stream.controller.dto.video.ListVideoResponse;
import com.stream.domain.video.VideoService;

@RestController
public class VideoController {
	private final VideoService videoService;

	@Autowired
	public VideoController(VideoService videoService) {
		this.videoService = videoService;
	}

	@GetMapping(path = "/videos")
	public ResponseEntity<ListVideoResponse> listVideo() {
		return ResponseEntity.ok()
			.body(ListVideoResponse.create(videoService.listVideo()));
	}

	@GetMapping(path = "/videos/{id}")
	public ResponseEntity<GetVideoResponse> getVideo(@PathVariable long id) {
		return ResponseEntity.ok()
			.body(GetVideoResponse.convertDtoToResponse(videoService.getVideoMetadata(id)));

	}
}
