package com.inkFront.inFront.repository;

import com.inkFront.inFront.entity.BlogMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogMediaRepository extends JpaRepository<BlogMedia, Long> {

    List<BlogMedia> findAllByBlogPostIdOrderByDisplayOrderAsc(Long blogPostId);

    void deleteAllByBlogPostId(Long blogPostId);
}