package com.stream.domain.video.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UploadVideoDto {
	@NotNull(message = "확장자를 제외한 파일명을 입력해주세요. (ex. ~)")
	private String fileName;
	@NotNull(message = "확장자를 입력해주세요. (ex. MOV)")
	private String extension;
	private String description;

	@Builder
	public UploadVideoDto(String fileName, String extension, String description) {
		this.fileName = fileName;
		this.extension = extension;
		this.description = description;
	}
}
