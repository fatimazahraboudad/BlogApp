package com.example.BlogApp.service;

import com.example.BlogApp.payload.CommentDto;

import java.util.List;

public interface CommentService {

    CommentDto createComment(long postId, CommentDto commentDto);

    List<CommentDto> getCommentByPostId(long id);
    CommentDto getCommentById(Long postId,Long commentId);

    CommentDto updateComment(long postId,long commentId, CommentDto commentDto);

    void deleteComment( long postId,long commentId);



}
