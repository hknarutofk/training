package com.example.es.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author yeqiang
 * @since 12/11/20 11:22 AM
 */
@Data
@Document(indexName = "source_code")
public class SourceCode {
    @Id
    private String id;
    private String path;
    private String content;
}
