package com.stream.video.service;

public class EmptyFileUploadException extends RuntimeException {
  public EmptyFileUploadException() {
    super("video file in upload request maybe empty");
  }
}
