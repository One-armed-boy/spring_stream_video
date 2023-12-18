package com.stream.video.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileNotFoundException;


@SpringBootTest
class LocalVideoServiceImplTest {
    @Autowired
    @Qualifier("LocalVideoService")
    private VideoService videoService;

    @Test
    @DisplayName("Qualifier를 통해 정상적으로 DI가 이루어지는지 테스트")
    void testDi() {
        Assertions.assertThat(videoService).isInstanceOf(LocalVideoServiceImpl.class);
    }

    @Test
    @DisplayName("File 명으로 사용될 수 없는 문자열이 ID로 입력 > 파일을 찾을 수 없음")
    void TestFileNotFound() {
        try {
            videoService.getVideoMetadata("///");
        } catch (Exception err) {
            Assertions.assertThat(err).isInstanceOf(FileNotFoundException.class);
        }
    }
}