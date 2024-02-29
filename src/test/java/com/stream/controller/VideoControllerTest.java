package com.stream.controller;

import java.util.List;

import org.assertj.core.api.Assertions;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stream.controller.dto.video.GetVideoResponse;
import com.stream.controller.dto.video.ListVideoResponse;
import com.stream.domain.member.dto.login.LoginCommand;
import com.stream.domain.member.dto.login.LoginResult;
import com.stream.domain.member.dto.signup.SignupCommand;
import com.stream.domain.video.Video;
import com.stream.domain.video.VideoService;
import com.stream.domain.video.dto.VideoDto;
import com.stream.facade.LoginFacade;
import com.stream.facade.SignupFacade;
import com.stream.security.jwt.JwtMetadata;
import com.stream.util.TestHelper;

import jakarta.servlet.http.Cookie;

@Import(TestHelper.class)
@SpringBootTest
@AutoConfigureMockMvc
public class VideoControllerTest {
	private final MockMvc mockMvc;
	private final TestHelper testHelper;
	private final VideoService videoService;
	private final LoginFacade loginFacade;
	private final SignupFacade signupFacade;

	@Autowired
	public VideoControllerTest(MockMvc mockMvc, TestHelper testHelper,
		VideoService videoService,
		LoginFacade loginFacade, SignupFacade signupFacade) {
		this.mockMvc = mockMvc;
		this.testHelper = testHelper;
		this.videoService = videoService;
		this.loginFacade = loginFacade;
		this.signupFacade = signupFacade;
	}

	@BeforeEach
	void initDB() {
		testHelper.initTables();
	}

	@AfterEach
	void clearDB() {
		testHelper.clearTables();
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

	@Test
	@DisplayName("DB 내 비디오 데이터 존재, 회원 가입 수행한 계정 -> 비디오 목록 요청 -> 정상 반환")
	void listVideoSuccess() throws Exception {
		// given
		var videosInTable = initVideoTable(List.of("video1", "video2"));

		var mockEmail = "testtest@test.com";
		var mockPwd = "qwerty12345";
		var cookie = signupAndLoginAndExtractCookie(mockEmail, mockPwd);

		// when
		mockMvc.perform(
				MockMvcRequestBuilders.get("/videos").cookie(cookie))
			// then
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
			.andDo(result -> {
				String contentString = result.getResponse().getContentAsString();
				ListVideoResponse response = new ObjectMapper().readValue(contentString, ListVideoResponse.class);
				Assertions.assertThat(response)
					.isEqualTo(videosInTable.stream().map(VideoDto::convertDomainToDto).toList());
			});
	}

	@Test
	@DisplayName("로그인 미 수행 클라이언트 -> 특정 비디오 조회 요청 -> Forbidden(403)")
	void getVideoFailBecauseNotLogin() throws Exception {
		// given
		initVideoTable(List.of("video_sample"));
		// when
		mockMvc.perform(MockMvcRequestBuilders.get("/videos")
				.param("id", String.valueOf(1)))
			// then
			.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

	@Test
	@DisplayName("로그인 수행 클라이언트 -> DB 내 존재하지 않는 id 값을 통한 비디오 get 요청 -> NotFound(404)")
	void getVideoFailBecauseInvalidId() throws Exception {
		// given
		var video = initVideoTable(List.of("video_sample")).get(0);

		var mockEmail = "test@test.com";
		var mockPwd = "1q2w3e4r5t6y7u8i9o";
		var cookie = signupAndLoginAndExtractCookie(mockEmail, mockPwd);

		// when
		mockMvc.perform(MockMvcRequestBuilders.get("/videos")
				.cookie(cookie)
				.param("id", String.valueOf((video.getId() + 1))))
			// then
			.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	@DisplayName("로그인 수행 클라이언트 -> DB 내 존재하는 Video 조회 -> 조회 성공")
	void getVideoSuccess() throws Exception {
		// given
		var video = initVideoTable(List.of("video1")).get(0);

		var mockEmail = "test@test.com";
		var mockPwd = "12345678789qwertyui";
		var cookie = signupAndLoginAndExtractCookie(mockEmail, mockPwd);

		// when
		mockMvc.perform(MockMvcRequestBuilders.get("/videos")
				.cookie(cookie)
				.param("id", String.valueOf(video.getId())))
			// then
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
			.andDo(result -> {
				GetVideoResponse videoResponse = new ObjectMapper().readValue(result.getResponse().getContentAsString(),
					GetVideoResponse.class);
				Assertions.assertThat(videoResponse)
					.isEqualTo(GetVideoResponse.convertDtoToResponse(VideoDto.convertDomainToDto(video)));
			});
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

	private Cookie signupAndLoginAndExtractCookie(String email, String password) {
		signup(email, password);
		var accessToken = loginAndGetAccessToken(email, password);
		return new Cookie(JwtMetadata.ACCESS_TOKEN_KEY, accessToken);
	}

	private void signup(String email, String password) {
		signupFacade.signUp(SignupCommand.builder().email(email).password(password).build());
	}

	private String loginAndGetAccessToken(String email, String password) {
		LoginResult result = loginFacade.login(LoginCommand.builder().email(email).inputPassword(password).build());
		return result.getAccessToken();
	}
}
