package com.JH.JhOnlineJudge.domain.category;

import com.JH.JhOnlineJudge.domain.category.entity.Category;
import com.JH.JhOnlineJudge.domain.category.exception.NotFoundCategoryException;
import com.JH.JhOnlineJudge.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Map<String, Object> getCategoryIdsWithParentCategory(Long categoryId) {
       Category findCategory = getCategoryById(categoryId);
       HashMap<String,Object> result = new HashMap<>();
       result.put("rootCategory",findCategory);
       result.put("categoryIds",findCategory.getCategoryIdsIncludingChildren(categoryId));
       return result;
    }

}
