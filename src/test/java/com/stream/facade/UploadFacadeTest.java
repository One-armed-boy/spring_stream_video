package com.stream.facade;

import com.stream.domain.video.Video;
import com.stream.domain.video.VideoRepository;
import com.stream.domain.video.VideoService;
import com.stream.domain.video.VideoServiceImpl;
import com.stream.domain.video.dto.UploadVideoDto;
import com.stream.storage.DummyStorageStrategy;
import com.stream.storage.StorageStrategy;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Transactional
@SpringBootTest
public class UploadFacadeTest {
  @Autowired
  private VideoRepository videoRepository;
  private UploadFacade uploadFacade;

  @BeforeEach
  private void setUploadFacade() {
    VideoService videoService = new VideoServiceImpl(this.videoRepository);
    StorageStrategy storageStrategy = new DummyStorageStrategy();
    this.uploadFacade = new UploadFacade(videoService, storageStrategy);
  }

  @Test
  @DisplayName("정상적으로 UploadFacade 객체가 초기화되는지 테스트")
  void testUploadFacadeInit() {
    Assertions.assertThat(this.uploadFacade).isInstanceOf(UploadFacade.class);
  }

  @Test
  @DisplayName("빈 파일 입력 > uploadVideoSync 호출 > EmptyFileUploadException 예외로 인한 DB 미적재")
  void testUploadEmptyVideoSync() {
    // Given
    UploadVideoDto uploadVideoDto = UploadVideoDto.builder().build();
    MockMultipartFile videoFile = new MockMultipartFile("test.mp4", (byte[]) null);

    // When
    CompletableFuture future = this.uploadFacade.uploadVideoSync(uploadVideoDto, videoFile);
    future.join();

    // Then
    List<Video> saveVideoList = videoRepository.findAll();
    Assertions.assertThat(saveVideoList.size()).isEqualTo(0);
  }

  @Test
  @DisplayName("정상 파일 입력 > uploadVideoSync 호출 > DB 내 비디오 정보 저장")
  void testUploadVideoSync() {
    // Given
    String fileName = "test-file";
    String extension = "MP4";
    UploadVideoDto uploadVideoDto = UploadVideoDto.builder().fileName(fileName).extension(extension).build();
    MockMultipartFile videoFile = new MockMultipartFile(fileName + "." + extension, "For Test Data".getBytes());

    // When
    CompletableFuture future = this.uploadFacade.uploadVideoSync(uploadVideoDto, videoFile);
    future.join();

    // Then
    List<Video> saveVideoList = videoRepository.findAll();
    Assertions.assertThat(saveVideoList.size()).isEqualTo(1);
    Video saveVideo = saveVideoList.get(0);
    Assertions.assertThat(saveVideo.getExtension()).isEqualTo(extension);
    Assertions.assertThat(saveVideo.getFileTag()).isEqualTo(fileName);
    Assertions.assertThat(saveVideo.getSize()).isEqualTo(videoFile.getSize());
  }
}
