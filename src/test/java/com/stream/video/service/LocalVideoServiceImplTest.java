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
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Optional;


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
    Video video1 = createVideoEntity("video1");
    Video video2 = createVideoEntity("video2");

    List<Video> videoListForSave = List.of(video1, video2);
    videoRepository.saveAll(videoListForSave);

    // When
    List<VideoDto> videoDtoList = videoService.listVideo();
    List<VideoDto> videoDtoForSaveList = videoListForSave.stream().map((video) -> {
      return VideoDto.builder().id(video.getId()).size(video.getSize()).fileName(video.getFileName()).build();
    }).toList();

    for (VideoDto videoDto : videoDtoList) {
      System.out.println(videoDto.id);
      System.out.println(videoDto.fileName);
    }

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

  @Test
  @DisplayName("빈 DB > getVideo 호출 > 예외 발생")
  void testEmptyGetVideo() {
    // Given
    try {
      // When
      videoService.getVideoMetadata(0);

      // 호출이 된다면 에러가 발생하지 않은 것
      Assertions.assertThat(true).isEqualTo(false);
    } catch (Exception err) {
      // Then
      Assertions.assertThat(err).isInstanceOf(Exception.class);
    }
  }

  @Test
  @DisplayName("DB 내 조회할 데이터가 존재 > getVideo 호출 > 조회 성공")
  void testGetVideo() {
    // Given
    Video videoForSave = createVideoEntity("video");
    videoRepository.save(videoForSave);
    Optional<Video> videoInDB = videoRepository.findOne(Example.of(videoForSave));
    Assertions.assertThat(videoInDB.isPresent()).isEqualTo(true);

    // When
    VideoDto videoDto = videoService.getVideoMetadata(videoInDB.get().getId());

    // Then
    Assertions.assertThat(videoDto.id).isEqualTo(videoForSave.getId());
    Assertions.assertThat(videoDto.fileName).isEqualTo(videoForSave.getFileName());
  }

  private Video createVideoEntity(String fileName) {
    return Video.builder()
            .fileName(fileName)
            .extension("MOV")
            .path(fileName + ".MOV")
            .size(1000)
            .build();
  }

}