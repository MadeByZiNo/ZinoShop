package com.JH.JhOnlineJudge.domain.category.repository;

import com.JH.JhOnlineJudge.domain.category.entity.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

    Optional<Category> findById(Long id);
    List<Category> findAll();
}
