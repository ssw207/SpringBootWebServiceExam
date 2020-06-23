package com.web.admin.web.service;

import com.web.admin.web.dto.PostsListResponseDto;
import com.web.admin.web.dto.PostsResponseDto;
import com.web.admin.web.dto.PostsSaveRequestDto;

import java.util.List;

public interface PostsService {
    public Long save(PostsSaveRequestDto dto);
    public Long update(Long id, PostsSaveRequestDto dto);
    public PostsResponseDto get(Long id);
    public List<PostsListResponseDto> findAllDesc();
    public void delete(Long id);
}
