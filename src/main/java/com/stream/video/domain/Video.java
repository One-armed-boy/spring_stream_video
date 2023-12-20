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
    private Integer id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "extension", nullable = false)
    private String extension;

    @Column(name = "path", nullable = false)
    private String path;

    @Column(name = "size", nullable = false)
    private long size;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Video(int id, String fileName, String extension, String path, long size) {
        this.id = id;
        this.fileName = fileName;
        this.extension = extension;
        this.path = path;
        this.size = size;
    }
}