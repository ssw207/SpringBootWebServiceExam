package com.web.admin.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.admin.web.domain.posts.Posts;
import com.web.admin.web.domain.posts.PostsRepository;
import com.web.admin.web.dto.PostsSaveRequestDto;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @After
    public void clearup() {
        postsRepository.deleteAll();;
    }

    @Test
    @WithMockUser(roles = "USER")
    public void posts_등록된다() throws Exception {
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
        //ResponseEntity<Long> responseEntity = restTemplate.postForEntity(url, dto, Long.class); // url로 dto롤 보내구 Logn타입으로 리턴을 받음
        mvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isOk());

        //then
        List<Posts> list = postsRepository.findAll();
        assertThat(list.get(0).getTitle()).isEqualTo(title);
        assertThat(list.get(0).getContent()).isEqualTo(content);
        assertThat(list.get(0).getAuthor()).isEqualTo(author);
    }

    @Test
    @WithMockUser(roles = "USER")
    public void posts_수정된다() throws Exception {
        //given
        String title ="제목";
        String content ="내용";
        String author = "작성자";

        Posts entity = postsRepository.save(Posts.builder().title(title).content(content).author(author).build());

        String exchangeTitle ="변경제목";
        String exchangeContent = "변경내용";

        PostsSaveRequestDto requestDto = PostsSaveRequestDto.builder().title(exchangeTitle).content(exchangeContent).build();
        //HttpEntity<PostsSaveRequestDto> httpEntity = new HttpEntity<>(requestDto);

        //when
        String url = "http://localhost:" + port + "/api/v1/posts/"+entity.getId();
        //ResponseEntity<Long> responseEntity = this.restTemplate.exchange(url, HttpMethod.PUT, httpEntity, Long.class);
        mvc.perform(put(url)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(new ObjectMapper().writeValueAsString(requestDto)))
                .andExpect(status().isOk());

        //then
        //Posts posts = postsRepository.findById(responseEntity.getBody()).orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));
        Posts posts = postsRepository.findAll().get(0);
        assertThat(posts.getTitle()).isEqualTo(exchangeTitle);
        assertThat(posts.getContent()).isEqualTo(exchangeContent);
    }
}

