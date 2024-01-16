package com.example.demo.post.service.port;

import com.example.demo.post.infra.PostEntity;
import com.example.demo.user.infra.UserEntity;

import java.util.Optional;

public interface PostRepository {
    Optional<PostEntity> findById( long id );

    PostEntity save(PostEntity postEntity);
}
