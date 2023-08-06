package com.example.mfisoft.controllers;

import com.example.mfisoft.entities.Article;

import com.example.mfisoft.exceptions.NotFoundException;
import com.example.mfisoft.repository.ArticlesRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/articles")
public class ArticlesController {
    private final ArticlesRepository articlesRepository;

    public ArticlesController(ArticlesRepository articlesRepository) {
        this.articlesRepository = articlesRepository;
    }

    @GetMapping
    public Iterable<Article> getArticles() {
        return articlesRepository.findAll();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getArticleById(@PathVariable("id") Long id) {
        Article article = articlesRepository.findById(id).orElseThrow(NotFoundException::new);
        return ResponseEntity.ok(article.article());
    }

    @GetMapping("/newsSite/{newsSite}")
    public Iterable<Article> getArticleByNewsSite(@PathVariable("newsSite") String newsSite) {
        return articlesRepository.findAllByNewsSite(newsSite);
    }
}
