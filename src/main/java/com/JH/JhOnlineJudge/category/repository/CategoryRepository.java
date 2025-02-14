package com.JH.JhOnlineJudge.category.repository;

import com.JH.JhOnlineJudge.category.domain.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    Optional<Category> findById(Long id);
    List<Category> findAll();
}
