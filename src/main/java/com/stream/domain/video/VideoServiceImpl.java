package com.stream.domain.video;

import com.stream.domain.video.dto.VideoDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class VideoServiceImpl implements VideoService {
  private final VideoRepository videoRepository;

  @Autowired
  public VideoServiceImpl(VideoRepository videoRepository) {
    this.videoRepository = videoRepository;
  }

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
  @Transactional
  public void createVideo(Video... video) {
    videoRepository.saveAll(List.of(video));
  }
}
