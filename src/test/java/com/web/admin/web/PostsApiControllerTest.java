package com.web.admin.web;

import com.web.admin.web.domain.posts.Posts;
import com.web.admin.web.domain.posts.PostsRepository;
import com.web.admin.web.dto.PostsSaveRequestDto;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@WebMvcTest //JPA 작동하지 않음
public class PostsApiControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private PostsRepository postsRepository;

    @Autowired
    private TestRestTemplate restTemplate;

    @After
    public void clearup() {
        postsRepository.deleteAll();;
    }

    @Test
    public void posts_등록된다() {
        //given
        String title ="제목";
        String content ="내용";
        String author = "작성자";

        PostsSaveRequestDto dto = PostsSaveRequestDto.builder()
                                                        .title(title)
                                                        .content(content)
                                                        .author(author)
                                                        .build();
        //when
        String url = "http://localhost:" + port + "/api/v1/posts";
        ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, dto, Long.class); // url로 dto롤 보내구 Logn타입으로 리턴을 받음

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isGreaterThan(0L);

        List<Posts> list = postsRepository.findAll();
        assertThat(list.get(0).getTitle()).isEqualTo(title);
        assertThat(list.get(0).getContent()).isEqualTo(content);
        assertThat(list.get(0).getAuthor()).isEqualTo(author);
    }

    @Test
    public void posts_수정된다() {
        //given
        String title ="제목";
        String content ="내용";
        String author = "작성자";

        Posts entity = postsRepository.save(Posts.builder().title(title).content(content).author(author).build());

        String exchangeTitle ="변경제목";
        String exchangeContent = "변경내용";

        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder().title(exchangeTitle).content(exchangeContent).build();
        HttpEntity<PostsSaveRequestDto> httpEntity = new HttpEntity<>(requestDto);

        //when
        String url = "http://localhost:" + port + "/api/v1/posts/"+entity.getId();
        ResponseEntity<Long> responseEntity = this.restTemplate.exchange(url, HttpMethod.PUT, httpEntity, Long.class);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        Posts posts = postsRepository.findById(responseEntity.getBody()).orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        assertThat(posts.getTitle()).isEqualTo(exchangeTitle);
        assertThat(posts.getContent()).isEqualTo(exchangeContent);
    }
}

