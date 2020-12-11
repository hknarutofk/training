package com.example.es.repository;

import com.example.es.entity.SourceCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author yeqiang
 * @since 12/11/20 11:24 AM
 */
public interface SourceCodeRepository extends ElasticsearchRepository<SourceCode, String> {
    Page<SourceCode> findByPath(String path, Pageable page);

    Page<SourceCode> findByContent(String content, Pageable pageable);
}
