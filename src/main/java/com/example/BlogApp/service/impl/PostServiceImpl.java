package com.example.BlogApp.service.impl;

import com.example.BlogApp.entity.Post;
import com.example.BlogApp.exception.ResourceNotFoundException;
import com.example.BlogApp.payload.PostDto;
import com.example.BlogApp.payload.PostResponse;
import com.example.BlogApp.repository.PostRepository;
import com.example.BlogApp.service.PostService;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private ModelMapper modelMapper;


    public PostServiceImpl(PostRepository postRepository,ModelMapper modelMapper) {
        this.postRepository = postRepository;
        this.modelMapper=modelMapper;
    }

    @Override
    public PostDto createPost(PostDto postDto) {
        Post post=maptoEntity(postDto);
        Post newPost=  postRepository.save(post);
        PostDto postResponse= maptoDto(newPost);
        return postResponse;
    }

    @Override
    public PostResponse getAllPost(int pageNo,int pageSize,String sortBy,String sortDir) {
        Sort sort=sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ?Sort.by(sortBy).ascending():Sort.by(sortBy).descending();

        Pageable pageable= PageRequest.of(pageNo,pageSize, sort);
        Page<Post> posts=postRepository.findAll(pageable);
        List<Post> ListOfPosts = posts.getContent();
        List<PostDto> content= ListOfPosts.stream().map(post -> maptoDto(post)).collect(Collectors.toList());
        PostResponse postResponse=new PostResponse();
        postResponse.setContent(content);
        postResponse.setPageNumber(posts.getNumber());
        postResponse.setPageSize(posts.getSize());
        postResponse.setTotalElement(posts.getTotalElements());
        postResponse.setTotalPage(posts.getTotalPages());
        postResponse.setLast(posts.isLast());
        return postResponse;
    }

    @Override
    public PostDto getPostById(long id) {

        return maptoDto(postRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Post","id",id)));
    }

    @Override
    public PostDto UpdatePost(PostDto postDto, long id) {
        Post post=maptoEntity(getPostById(id));
        post.setTitle(postDto.getTitle());
        post.setDescription(postDto.getDescription());
        post.setContent(postDto.getContent());
        Post postupdate=postRepository.save(post);
        return maptoDto(postupdate);
    }

    @Override
    public void DeletePost(long id) {
        Post post=maptoEntity(getPostById(id));
        postRepository.delete(post);

    }

    private PostDto maptoDto(Post post) {
        PostDto postDto=modelMapper.map(post,PostDto.class);
        return postDto;
    }
    private Post maptoEntity(PostDto postDto) {
        Post post=modelMapper.map(postDto,Post.class);
        return post;
    }
}
