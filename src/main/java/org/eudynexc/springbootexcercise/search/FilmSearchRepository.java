package org.eudynexc.springbootexcercise.search;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface FilmSearchRepository extends ElasticsearchRepository<FilmDocument, Integer> {
  Page<FilmDocument> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
