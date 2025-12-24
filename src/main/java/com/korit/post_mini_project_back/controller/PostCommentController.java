package com.korit.post_mini_project_back.controller;

import com.korit.post_mini_project_back.dto.request.CreatePostCommentReqDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
public class PostCommentController {

    @PostMapping
    public ResponseEntity<?> createComments(@PathVariable int postId, @RequestBody CreatePostCommentReqDto dto) {
        System.out.println(postId);
        System.out.println(dto);

        return ResponseEntity.ok(null);
    }
}
