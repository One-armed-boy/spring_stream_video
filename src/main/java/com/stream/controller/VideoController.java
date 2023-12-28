package com.stream.controller;

import com.stream.domain.video.VideoService;
import com.stream.domain.video.dto.VideoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VideoController {
  @Autowired
  private VideoService videoService;

  @GetMapping(path = "/videos")
  public ResponseEntity<List<VideoDto>> listVideo() {
    List<VideoDto> videoDtoList = videoService.listVideo();
    return ResponseEntity.ok().body(videoDtoList);
  }

  @GetMapping(path = "/videos", params = "id")
  public ResponseEntity<VideoDto> getVideo(@RequestParam long id) {
    try {
      VideoDto videoDto = videoService.getVideoMetadata(id);
      return ResponseEntity.ok().body(videoDto);
    } catch (final Exception err) {
      return ResponseEntity.notFound().build();
    }
  }
}
