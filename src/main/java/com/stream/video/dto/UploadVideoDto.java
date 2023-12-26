package com.stream.video.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UploadVideoDto {
  @NotNull(message = "확장자를 제외한 파일명을 입력해주세요. (ex. ~)")
  private String fileName;

  @NotNull(message = "확장자를 입력해주세요. (ex. MOV)")
  private String extension;

  private String description;
}
