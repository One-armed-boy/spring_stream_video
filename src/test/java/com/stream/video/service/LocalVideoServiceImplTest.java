package com.stream.video.service;

import com.stream.video.domain.Video;
import com.stream.video.dto.VideoDto;
import com.stream.video.repository.VideoRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@Transactional
@SpringBootTest
class LocalVideoServiceImplTest {
  @Autowired
  @Qualifier("LocalVideoService")
  private VideoService videoService;

  @Autowired
  private VideoRepository videoRepository;

  @Test
  @DisplayName("Qualifier를 통해 정상적으로 DI가 이루어지는지 테스트")
  void testDi() {
    Assertions.assertThat(videoService).isInstanceOf(LocalVideoServiceImpl.class);
  }

  @Test
  @DisplayName("DB가 비어있는 상황 > listVideo 호출 > 빈배열 반환")
  void testEmptyListVideo() {
    // Given
    // When
    List<VideoDto> videoList = videoService.listVideo();

    // Then
    Assertions.assertThat(videoList).isEmpty();
  }

  @Test
  @DisplayName("DB 내 Video 데이터가 존재 > listVideo 호출 > 데이터들 반환")
  void testListVideo() {
    // Given
    String videoDir = "./videos";
    Video video1 = Video.builder()
            .id(1)
            .fileName("video1")
            .extension("MOV")
            .path(videoDir + "video1.MOV")
            .size(1000)
            .build();
    Video video2 = Video.builder()
            .id(2)
            .fileName("video2")
            .extension("MOV")
            .path(videoDir + "video2.MOV")
            .size(2000)
            .build();
    List<Video> videoListForSave = List.of(video1, video2);
    videoRepository.saveAll(videoListForSave);

    // When
    List<VideoDto> videoDtoList = videoService.listVideo();
    List<VideoDto> videoDtoForSaveList = videoListForSave.stream().map((video) -> {
      return VideoDto.builder().id(video.getId()).size(video.getSize()).fileName(video.getFileName()).build();
    }).toList();

    // Then
    Assertions.assertThat(videoDtoList.size()).isEqualTo(videoListForSave.size());
    for (VideoDto videoDto : videoDtoList) {
      Assertions.assertThat(videoDto.id).isIn(videoDtoForSaveList.stream().map((video) -> video.id).toList());
      Assertions.assertThat(videoDto.size).isIn(videoDtoForSaveList.stream().map((video) -> video.size).toList());
      Assertions.assertThat(videoDto.fileName)
              .isIn(videoDtoForSaveList.stream().map((video) -> video.fileName).toList());
      Assertions.assertThat(videoDto.createdAt).isNotNull();
    }
  }
}