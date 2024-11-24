package com.JH.JhOnlineJudge.category;

import com.JH.JhOnlineJudge.category.domain.Category;
import com.JH.JhOnlineJudge.category.exception.NotFoundCategoryException;
import com.JH.JhOnlineJudge.category.repository.CategoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category findById(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(()->new NotFoundCategoryException(id));
    }

    public List<Category> findAll(){
        return categoryRepository.findAll();
    }

    @Transactional
    public List<Long> getCategoryIdsWithParentCategory(Long categoryId) {
       Category findCategory = findById(categoryId);
       return findCategory.getCategoryIdsIncludingChildren(categoryId);
    }

    @Transactional
    public List<Category> getFirstLevelChildCategories(Long categoryId) {
       Category findCategory = findById(categoryId);
       return findCategory.getFirstLevelChildCategories();
    }
}
