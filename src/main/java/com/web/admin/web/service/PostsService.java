package com.web.admin.web.service;

import com.web.admin.web.dto.PostsReponseDto;
import com.web.admin.web.dto.PostsSaveRequestDto;

public interface PostsService {
    public Long save(PostsSaveRequestDto dto);
    public Long update(Long id, PostsSaveRequestDto dto);
    public PostsReponseDto get(Long id);
}
