package com.web.admin.web.dto;

import com.web.admin.web.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostsSaveRequestDto {
    private String title;
    private String content;
    private String author;

    @Builder
    public PostsSaveRequestDto(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }
    public Posts toEntity() {
        return Posts.builder()
                .title(this.title)
                .content(this.content)
                .author(this.author)
                .build();
    }
}
