package com.stream.controller.dto.video;

import java.util.List;

import com.stream.domain.video.dto.VideoDto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class ListVideoResponse {
	private List<VideoDto> videos;

	@Builder
	public ListVideoResponse(List<VideoDto> videos) {
		this.videos = videos;
	}
}
