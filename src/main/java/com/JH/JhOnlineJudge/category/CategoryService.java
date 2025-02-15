package com.JH.JhOnlineJudge.category;

import com.JH.JhOnlineJudge.category.domain.Category;
import com.JH.JhOnlineJudge.category.exception.NotFoundCategoryException;
import com.JH.JhOnlineJudge.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Category getCategoryById(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(()->new NotFoundCategoryException(id));
    }

    @Transactional(readOnly = true)
    public List<Category> findAll(){
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Long> getCategoryIdsWithParentCategory(Long categoryId) {
       Category findCategory = getCategoryById(categoryId);
       return findCategory.getCategoryIdsIncludingChildren(categoryId);
    }

    @Transactional(readOnly = true)
    public List<Category> getFirstLevelChildCategories(Long categoryId) {
       Category findCategory = getCategoryById(categoryId);
       return findCategory.getFirstLevelChildCategories();
    }
}
