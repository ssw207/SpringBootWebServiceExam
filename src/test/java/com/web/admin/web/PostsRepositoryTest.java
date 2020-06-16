package com.web.admin.web;

import com.web.admin.web.domain.posts.Posts;
import com.web.admin.web.domain.posts.PostsRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
        //givne
        String author = "myAuthor";
        String content = "myContent";
        String title = "myTitle";

        //when
        /*
        repository.save()는 id값이 있으면 update, 없으면 insert 수행
         */
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
}
