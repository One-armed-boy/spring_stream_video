package com.stream.video.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "video")
@NoArgsConstructor
@Getter
public class Video {
  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Column(name = "file_tag", nullable = false)
  private String fileTag;

  @Column(name = "extension", nullable = false)
  private String extension;

  @Column(name = "path", nullable = false)
  private String path;

  @Column(name = "size", nullable = false)
  private long size;

  @Column(name = "description")
  private String description;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @Builder
  public Video(long id, String fileTag, String extension, String path, long size, String description) {
    this.id = id;
    this.fileTag = fileTag;
    this.extension = extension;
    this.path = path;
    this.size = size;
    this.description = description;
  }
}