package com.JH.JhOnlineJudge.category.repository;

import com.JH.JhOnlineJudge.category.domain.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CategoryRepositoryImpl implements CategoryRepository{

    private final CategoryJpaRepository categoryJpaRepository;

    public Optional<Category> findById(Long id) {
        return categoryJpaRepository.findById(id);
    }
    public List<Category> findAll() {return categoryJpaRepository.findAll(); }

}
