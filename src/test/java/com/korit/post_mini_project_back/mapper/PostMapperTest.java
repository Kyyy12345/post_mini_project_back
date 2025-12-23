package com.korit.post_mini_project_back.mapper;

import com.korit.post_mini_project_back.entity.Post;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class PostMapperTest {

    @Autowired // final을 지우고 이 어노테이션을 붙이세요.
    private PostMapper postMapper;

    @Test
    void getFeedsTest() {
        int currentPage = 1;
        int size = 10;
        int startIndex = (currentPage - 1) * size;
        int userId = 1;

        List<Post> feeds = postMapper.getFeeds(startIndex, size, userId);
        feeds.forEach(post -> {
            System.out.println(post);
            System.out.println(post.getUser());
            System.out.println(post.getFollow());
            post.getImageFiles().forEach(System.out::println);
        });
    }
}
