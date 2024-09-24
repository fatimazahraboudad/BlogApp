package com.example.BlogApp.controller;

import com.example.BlogApp.payload.CommentDto;
import com.example.BlogApp.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/")
public class CommentController {

    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentDto> createComment(@PathVariable(name = "postId") long id, @RequestBody CommentDto commentDto) {

        return new ResponseEntity<>(commentService.createComment(id,commentDto), HttpStatus.CREATED);

    }

    @GetMapping("/posts/{postId}/comments")
    public List<CommentDto> getCommentByPostId(@PathVariable(name = "postId") long id) {
        return commentService.getCommentByPostId(id);
    }

    @GetMapping("/posts/{id_post}/comments/{id_comment}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable  long id_post, @PathVariable long id_comment) {
        return ResponseEntity.ok(commentService.getCommentById(id_post,id_comment));
    }

    @PutMapping("/posts/{id_post}/comments/{id_comment}")
    public ResponseEntity<CommentDto> updateCommentById(@PathVariable long id_post, @PathVariable long id_comment, @RequestBody CommentDto commentDto) {
        return ResponseEntity.ok(commentService.updateComment(id_post,id_comment, commentDto));
    }

    @DeleteMapping("/posts/{id_post}/comments/{id_comment}")
    public ResponseEntity<String> deleteComment(@PathVariable long id_post, @PathVariable long id_comment) {
            commentService.deleteComment(id_post, id_comment);
            return new ResponseEntity<>("comment deleted successfully",HttpStatus.OK);
    }

}
