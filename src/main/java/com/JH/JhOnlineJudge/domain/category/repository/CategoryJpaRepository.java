package com.JH.JhOnlineJudge.domain.category.repository;

import com.JH.JhOnlineJudge.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {
    List<Category> findByParentId(Long parentId);
}
