package com.example.BlogApp.service;

import com.example.BlogApp.payload.PostDto;
import com.example.BlogApp.payload.PostResponse;

import java.util.List;

public interface PostService {

    PostDto createPost(PostDto postDto);
    PostResponse getAllPost(int pageNo, int pageSize, String sortBy,String sortir);

    PostDto getPostById(long id);

    PostDto UpdatePost(PostDto postDto, long id);
    void DeletePost(long id);
}
