package com.web.admin.web.service;

import com.web.admin.web.domain.posts.Posts;
import com.web.admin.web.domain.posts.PostsRepository;
import com.web.admin.web.dto.PostsListResponseDto;
import com.web.admin.web.dto.PostsResponseDto;
import com.web.admin.web.dto.PostsSaveRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostsServiceImpl implements  PostsService{

    private final PostsRepository postsRepository;

    @Transactional
    @Override
    public Long save(PostsSaveRequestDto dto) {
        return postsRepository.save(dto.toEntity()).getId();
    }

    @Transactional //트렌젝션 안에서 데이터베이스에서 데이터를 가져오면 영속성 컨테이너가 등록되고 트렌젝션이 종료되는 시점에 테이블의 변경분을 반영하므로 @Transactional이 없으면 수정되지 않음
    @Override
    public Long update(Long id, PostsSaveRequestDto postsSaveRequestDto) {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다 id:"+id) );
        posts.update(postsSaveRequestDto.getTitle(), postsSaveRequestDto.getContent());
        return posts.getId();
    }

    @Override
    public PostsResponseDto get(Long id) {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다 id:"+id) );
        return new PostsResponseDto(posts);
    }

    @Transactional(readOnly = true)
    @Override
    public List<PostsListResponseDto> findAllDesc() {
        log.debug("리스트 조회 시작");
        return postsRepository.findAllDesc().stream()
                                .map(PostsListResponseDto::new) // 조회된 결과를 순회하면서 PostsResponseDto를 생성하고 인자로 전달
                                .collect(Collectors.toList()); // map에서 작업한 결과를 List로 반환
    }

    @Transactional
    @Override
    public void delete(Long id) {
        Posts posts = postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 사용자가 없습니다 야:"+id));
        postsRepository.delete(posts);
    }
}
