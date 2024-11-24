package com.JH.JhOnlineJudge.category.domain;

import com.JH.JhOnlineJudge.product.domain.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "category")
    private List<Product> products; // 이 부분이 필요함


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Category> child = new ArrayList<>();

    public void removeProduct(Product product) {
        products.remove(product);
    }

   public List<Category> getAllChildCategories() {
       List<Category> allChildren = new ArrayList<>(child);
       for (Category c : child) {
           allChildren.addAll(c.getAllChildCategories());
       }
       return allChildren;
   }

    // 현재 카테고리와 그 자식 카테고리들의 ID를 포함하는 리스트를 반환
    public List<Long> getCategoryIdsIncludingChildren(Long categoryId) {
        List<Category> allChildCategories = this.getAllChildCategories();
        List<Long> categoryIds = allChildCategories.stream()
                .map(Category::getId)
                .collect(Collectors.toList());

        categoryIds.add(categoryId);  // 현재 카테고리 ID도 추가
        return categoryIds;
    }

    // 첫번째 자식 카테고리만 반환
    public List<Category> getFirstLevelChildCategories() {
          return this.child;
    }
}
