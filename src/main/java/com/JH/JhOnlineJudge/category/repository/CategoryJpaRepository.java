package com.JH.JhOnlineJudge.category.repository;

import com.JH.JhOnlineJudge.category.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryJpaRepository extends JpaRepository<Category, Long> {
    List<Category> findByParentId(Long parentId);
}
