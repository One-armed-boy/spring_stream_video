package com.stream.domain.video;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Example;

import com.stream.domain.video.dto.VideoDto;
import com.stream.util.TestHelper;

@Import(TestHelper.class)
@SpringBootTest
class VideoServiceTest {
	private final VideoService videoService;
	private final VideoRepository videoRepository;
	private final TestHelper testHelper;

	@Autowired
	public VideoServiceTest(VideoService videoService, VideoRepository videoRepository, TestHelper testHelper) {
		this.videoService = videoService;
		this.videoRepository = videoRepository;
		this.testHelper = testHelper;
	}

	@AfterEach
	void cleanDB() {
		testHelper.clearTables();
	}

	@Test
	@DisplayName("정상적으로 DI가 이루어지는지 테스트")
	void testDi() {
		Assertions.assertThat(videoService).isInstanceOf(VideoService.class);
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
		videoService.createVideo(video1, video2);

		// When
		List<VideoDto> videoDtoList = videoService.listVideo();
		List<VideoDto> videoDtoForSaveList = videoListForSave.stream().map((video) -> {
			return VideoDto.builder().id(video.getId()).size(video.getSize()).fileTag(video.getFileTag()).build();
		}).toList();

		// Then
		Assertions.assertThat(videoDtoList.size()).isEqualTo(videoListForSave.size());
		for (VideoDto videoDto : videoDtoList) {
			Assertions.assertThat(videoDto.getId())
				.isIn(videoDtoForSaveList.stream().map((video) -> video.getId()).toList());
			Assertions.assertThat(videoDto.getSize())
				.isIn(videoDtoForSaveList.stream().map((video) -> video.getSize()).toList());
			Assertions.assertThat(videoDto.getFileTag())
				.isIn(videoDtoForSaveList.stream().map((video) -> video.getFileTag()).toList());
			Assertions.assertThat(videoDto.getCreatedAt()).isNotNull();
			Assertions.assertThat(videoDto.getExtension()).isNotNull();
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

		videoService.createVideo(videoForSave);

		Optional<Video> videoInDB = videoRepository.findOne(Example.of(videoForSave));
		Assertions.assertThat(videoInDB.isPresent()).isEqualTo(true);

		// When
		VideoDto videoDto = videoService.getVideoMetadata(videoInDB.get().getId());

		// Then
		Assertions.assertThat(videoDto.getId()).isEqualTo(videoForSave.getId());
		Assertions.assertThat(videoDto.getFileTag()).isEqualTo(videoForSave.getFileTag());
		Assertions.assertThat(videoDto.getExtension()).isEqualTo(videoForSave.getExtension());
	}

	@Test
	@DisplayName("빈 DB > createVideo 호출 > 이후 조회 시 성공")
	void testCreateVideo() {
		// Given
		// When
		Video videoForSave = createVideoEntity("test");
		videoService.createVideo(videoForSave);

		// Then
		List<VideoDto> videoDtoList = videoService.listVideo();
		Assertions.assertThat(videoDtoList.size()).isEqualTo(1);

		VideoDto videoDto = videoDtoList.get(0);
		Assertions.assertThat(videoDto.getFileTag()).isEqualTo(videoForSave.getFileTag());
	}

	private Video createVideoEntity(String fileName) {
		return Video.builder()
			.fileTag(fileName)
			.extension("MOV")
			.path(fileName + ".MOV")
			.size(1000)
			.build();
	}

}
