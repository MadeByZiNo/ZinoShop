package com.JH.JhOnlineJudge.category;

import com.JH.JhOnlineJudge.category.domain.Category;
import com.JH.JhOnlineJudge.category.exception.NotFoundCategoryException;
import com.JH.JhOnlineJudge.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category findCategory(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(()->new NotFoundCategoryException(id));
    }

}
