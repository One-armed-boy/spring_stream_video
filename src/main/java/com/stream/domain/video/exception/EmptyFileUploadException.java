package com.stream.domain.video.exception;

public class EmptyFileUploadException extends RuntimeException {
  public EmptyFileUploadException() {
    super("video file in upload request maybe empty");
  }
}
