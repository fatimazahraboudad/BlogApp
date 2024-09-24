package com.example.BlogApp.service.impl;

import com.example.BlogApp.entity.Comment;
import com.example.BlogApp.entity.Post;
import com.example.BlogApp.exception.BlogAPIException;
import com.example.BlogApp.exception.ResourceNotFoundException;
import com.example.BlogApp.payload.CommentDto;
import com.example.BlogApp.repository.CommentRepository;
import com.example.BlogApp.repository.PostRepository;
import com.example.BlogApp.service.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    private CommentRepository commentRepository;
    private PostRepository postRepository;
    private ModelMapper modelMapper;


    public CommentServiceImpl(CommentRepository commentRepository,ModelMapper modelMapper, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.modelMapper=modelMapper;
        this.postRepository = postRepository;

    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
        Comment comment = mappToEntity(commentDto);
        Post post= postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","id",postId));
        comment.setPost(post);

         Comment newComment =commentRepository.save(comment);
        return mappToDTO(newComment);
    }

    @Override
    public List<CommentDto> getCommentByPostId(long id) {
        List<Comment> commentList =commentRepository.findByPostId(id);
        return commentList.stream().map(comment->mappToDTO(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {
        Post post= postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","id",postId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment","id",commentId));
        if (comment.getPost().getId() != (post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"comment does not belong to post");
        }

        return mappToDTO(comment);
    }

    @Override
    public CommentDto updateComment(long postId, long commentId, CommentDto commentDto) {
        Post post= postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","id",postId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment","id",commentId));
        if (comment.getPost().getId() != (post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"comment ndpes not belong to post");
        }
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());
        Comment updateComment=commentRepository.save(comment);


        return mappToDTO(updateComment);
    }

    @Override
    public void deleteComment(long postId, long commentId) {
        Post post= postRepository.findById(postId).orElseThrow(()-> new ResourceNotFoundException("Post","id",postId));
        Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFoundException("Comment","id",commentId));
        if (comment.getPost().getId() != (post.getId())) {
            throw new BlogAPIException(HttpStatus.BAD_REQUEST,"comment ndpes not belong to post");
        }
        commentRepository.delete(comment);
    }

    private CommentDto mappToDTO(Comment comment) {
        CommentDto commentDto=modelMapper.map(comment,CommentDto.class);

        return commentDto;
    }

    private Comment mappToEntity(CommentDto commentDto) {
        Comment comment=modelMapper.map(commentDto,Comment.class);
        return  comment;
    }
}
