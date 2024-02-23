package com.stream.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stream.domain.video.VideoService;
import com.stream.domain.video.dto.VideoDto;

@RestController
public class VideoController {
	private final VideoService videoService;

	@Autowired
	public VideoController(VideoService videoService) {
		this.videoService = videoService;
	}

	@GetMapping(path = "/videos")
	public ResponseEntity<List<VideoDto>> listVideo() {
		List<VideoDto> videoDtoList = videoService.listVideo();
		return ResponseEntity.ok().body(videoDtoList);
	}

	@GetMapping(path = "/videos", params = "id")
	public ResponseEntity<VideoDto> getVideo(@RequestParam long id) {
		return ResponseEntity.ok().body(videoService.getVideoMetadata(id));

	}
}
