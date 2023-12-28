package com.stream.domain.video;

import com.stream.domain.video.dto.VideoDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoServiceImpl implements VideoService {
  @Autowired
  private VideoRepository videoRepository;

  @Override
  public List<VideoDto> listVideo() {
    List<Video> videoList = videoRepository.findAll();
    return videoList.stream()
            .map(video -> VideoDto.convertDomainToDto(video))
            .toList();
  }

  @Override
  public VideoDto getVideoMetadata(long id) throws EntityNotFoundException {
    Video video = getVideoById(id);
    return VideoDto.convertDomainToDto(video);
  }

  @Override
  public String getVideoFilePath(long id) throws EntityNotFoundException {
    Video video = getVideoById(id);
    return video.getPath();
  }

  private Video getVideoById(long id) {
    return videoRepository.getReferenceById(id);
  }

  @Override
  public void createVideo(Video... video) {
    videoRepository.saveAll(List.of(video));
  }
}
