package com.stream.facade;

import com.stream.domain.video.Video;
import com.stream.domain.video.VideoRepository;
import com.stream.domain.video.VideoService;
import com.stream.domain.video.VideoServiceImpl;
import com.stream.storage.DummyStorageStrategy;
import com.stream.storage.StorageStrategy;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;

@Transactional
@SpringBootTest
public class StreamingFacadeTest {
  private StreamingFacade streamingFacade;
  @Autowired
  private VideoRepository videoRepository;

  @BeforeEach
  private void setStreamingFacade() {
    VideoService videoService = new VideoServiceImpl(videoRepository);
    StorageStrategy storageStrategy = new DummyStorageStrategy();
    this.streamingFacade = new StreamingFacade(videoService, storageStrategy);
  }

  @Test
  @DisplayName("정상적으로 StreamingFacade 객체가 초기화되는지 테스트")
  void testStreamingFacadeInit() {
    Assertions.assertThat(streamingFacade).isInstanceOf(StreamingFacade.class);
  }

  @Test
  @DisplayName("빈 데이터베이스 > videoId를 이용한 createVideoStream 호출 > 예외 발생")
  void testCreateVideoStreamWithVideoIdNotExisted() {
    // Given
    long mockVideoId = 123;
    Assertions.assertThatThrownBy(() -> {
                      // When
                      streamingFacade.createVideoStream(mockVideoId);
                    }
            )
            // Then
            .isInstanceOf(EntityNotFoundException.class);
  }

  @Test
  @DisplayName("데이터베이스 내 Video 존재 > 해당 videoId를 이용한 createVideoStream 호출 > Resource 객체 반환")
  void testCreateVideoStream() {
    //Given
    Video video = Video.builder()
            .id(123)
            .fileTag("forTest")
            .extension("MP4")
            .path("/for-test/forTest.MP4")
            .size(100)
            .build();
    videoRepository.save(video);

    // When
    Resource resource = streamingFacade.createVideoStream(video.getId());

    // Then
    Assertions.assertThat(resource).isInstanceOf(Resource.class);
    Assertions.assertThat(resource.exists()).isTrue();
  }
}
