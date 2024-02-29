package com.stream.controller.dto.upload;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UploadVideoRequest {
	@NotNull(message = "확장자를 제외한 파일명을 입력해주세요. (ex. ~)")
	private String fileName;
	@NotNull(message = "확장자를 입력해주세요. (ex. MOV)")
	private String extension;
	private String description;

	@Builder
	public UploadVideoRequest(String fileName, String extension, String description) {
		this.fileName = fileName;
		this.extension = extension;
		this.description = description;
	}
}
