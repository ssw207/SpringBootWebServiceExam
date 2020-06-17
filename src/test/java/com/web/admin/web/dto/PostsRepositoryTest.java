package com.web.admin.web.dto;

import com.web.admin.web.domain.posts.Posts;
import com.web.admin.web.domain.posts.PostsRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class PostsRepositoryTest {
    
    @Autowired
    PostsRepository postsRepository;

    @After // 단위테스트가 끝날때마다 실행
    public void cleanup() {
        postsRepository.deleteAll();
    }

    @Test
    public void 게시글저장_불러오기() {
        //given
        String author = "myAuthor";
        String content = "myContent";
        String title = "myTitle";

        //when
        //repository.save()는 id값이 있으면 update, 없으면 insert 수행
        postsRepository.save(Posts.builder() 
                                .author(author)
                                .content(content)
                                .title(title)
                                .build());
        //then
        List<Posts> postList = postsRepository.findAll();
        Posts posts = postList.get(0);
        assertThat(posts.getAuthor()).isEqualTo(author);
        assertThat(posts.getContent()).isEqualTo(content);
        assertThat(posts.getTitle()).isEqualTo(title);
    }

    @Test
    public void BaseTimeEntity_등록() {
        LocalDateTime now = LocalDateTime.of(2020,1,1,0,0);
        //given
        String author = "myAuthor";
        String content = "myContent";
        String title = "myTitle";

        postsRepository.save(Posts.builder().title(title).content(content).title(title).build());

        //when
        Posts posts = postsRepository.findAll().get(0);

        log.info("생성시간 : {}, 변경시간 : {}",posts.getCreatedDate(), posts.getModifiedDate());

        //then
        assertThat(posts.getCreatedDate()).isAfter(now);
        assertThat(posts.getModifiedDate()).isAfter(now);
    }
}
