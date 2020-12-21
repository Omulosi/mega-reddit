package com.example.megareddit.repository;


import com.example.megareddit.model.Post;
import com.example.megareddit.model.Subreddit;
import com.example.megareddit.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);
}
