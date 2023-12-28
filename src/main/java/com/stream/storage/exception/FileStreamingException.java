package com.stream.storage.exception;

public class FileStreamingException extends RuntimeException {
  public FileStreamingException(Exception cause) {
    super("Occur Exception while create file stream to video streaming.", cause);
  }
}
