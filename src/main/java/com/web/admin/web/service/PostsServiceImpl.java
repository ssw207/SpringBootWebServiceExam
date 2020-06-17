package com.web.admin.web.service;

import com.web.admin.web.domain.posts.Posts;
import com.web.admin.web.domain.posts.PostsRepository;
import com.web.admin.web.dto.PostsReponseDto;
import com.web.admin.web.dto.PostsSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class PostsServiceImpl implements  PostsService{

    private final PostsRepository postsRepository;

    @Override
    @Transactional
    public Long save(PostsSaveRequestDto dto) {
        return postsRepository.save(dto.toEntity()).getId();
    }

    @Override
    @Transactional //트렌젝션 안에서 데이터베이스에서 데이터를 가져오면 영속성 컨테이너가 등록되고 트렌젝션이 종료되는 시점에 테이블의 변경분을 반영하므로 @Transactional이 없으면 수정되지 않음  
    public Long update(Long id, PostsSaveRequestDto postsSaveRequestDto) {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다 id:"+id) );
        posts.update(postsSaveRequestDto.getTitle(), postsSaveRequestDto.getContent());
        return posts.getId();
    }

    @Override
    public PostsReponseDto get(Long id) {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다 id:"+id) );
        return new PostsReponseDto(posts);
    }
}
