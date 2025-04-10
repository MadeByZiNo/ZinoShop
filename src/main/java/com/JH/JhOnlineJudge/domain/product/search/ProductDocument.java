package com.JH.JhOnlineJudge.domain.product.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)  // 추가: 알 수 없는 필드를 무시하도록 설정
@Document(indexName = "products")
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDocument {

    @Id
    private Long id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Keyword)
    private String category;

    @Field(type = FieldType.Text)
    private List<String> keywords;

    @Field(type = FieldType.Text)
    private String keywordtext;

}