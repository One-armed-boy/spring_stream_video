package com.stream.controller;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.stream.domain.member.dto.login.LoginCommand;
import com.stream.domain.member.dto.login.LoginResult;
import com.stream.domain.video.Video;
import com.stream.domain.video.VideoRepository;
import com.stream.domain.video.VideoService;
import com.stream.facade.LoginFacade;
import com.stream.util.TestHelper;

@Import(TestHelper.class)
@SpringBootTest
@AutoConfigureMockMvc
public class VideoControllerTest {
	private final MockMvc mockMvc;
	private final TestHelper testHelper;
	private final VideoRepository videoRepository;
	private final VideoService videoService;
	private final LoginFacade loginFacade;

	@Autowired
	public VideoControllerTest(MockMvc mockMvc, TestHelper testHelper, VideoRepository videoRepository,
		VideoService videoService,
		LoginFacade loginFacade) {
		this.mockMvc = mockMvc;
		this.testHelper = testHelper;
		this.videoRepository = videoRepository;
		this.videoService = videoService;
		this.loginFacade = loginFacade;
	}

	@BeforeEach
	void initDB() {
		testHelper.initDB();
	}

	@AfterEach
	void clearDB() {
		testHelper.clearTables(videoRepository);
	}

	@Test
	@DisplayName("로그인 미 수행 클라이언트 -> 비디오 리스트 조회 -> Forbidden(403)")
	void listVideoFailBecauseNotLogin() throws Exception {
		// given
		initVideoTable(List.of("video1", "video2", "video3"));

		// when
		mockMvc.perform(MockMvcRequestBuilders.get("/videos"))
			// then
			.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	private List<Video> initVideoTable(List<String> videoNames) {
		return createVideos(
			videoNames.stream()
				.map(name -> Video.builder()
					.fileTag(name)
					.extension("MOV")
					.path("/mock-path")
					.size(100L)
					.build()).toList());
	}

	private List<Video> createVideos(List<Video> videos) {
		return videoService.createVideo(videos.toArray(new Video[videos.size()]));
	}

	private String loginAndGetAccessToken(String email, String password) {
		LoginResult result = loginFacade.login(LoginCommand.builder().email(email).inputPassword(password).build());
		return result.getAccessToken();
	}
}
