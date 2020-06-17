package com.web.admin.web.dto;

import com.web.admin.web.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostsReponseDto {
    private Long id;
    private String title;
    private String content;
    private String author;

    @Builder
    public PostsReponseDto(Posts entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.author = entity.getAuthor();
    }
}
