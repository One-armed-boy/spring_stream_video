package com.stream.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.stream.facade.StreamingFacade;

@RestController
public class StreamingController {
	private final StreamingFacade streamingFacade;

	@Autowired
	public StreamingController(StreamingFacade streamingFacade) {
		this.streamingFacade = streamingFacade;
	}

	@GetMapping(path = "/videos/stream", params = "id")
	public ResponseEntity<Resource> getVideoStream(@RequestParam long id) {
		try {
			Resource videoStream = streamingFacade.createVideoStream(id);

			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-Type", "video/mp4");

			return ResponseEntity.ok().headers(headers).body(videoStream);
		} catch (Exception err) {
			return ResponseEntity.notFound().build();
		}
	}
}
