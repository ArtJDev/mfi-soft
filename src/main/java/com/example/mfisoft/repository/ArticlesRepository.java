package com.example.mfisoft.repository;

import com.example.mfisoft.entities.Article;
import org.springframework.data.repository.CrudRepository;

public interface ArticlesRepository extends CrudRepository<Article, Long> {

    Iterable<Article> findAllByNewsSite(String newsSite);
}
