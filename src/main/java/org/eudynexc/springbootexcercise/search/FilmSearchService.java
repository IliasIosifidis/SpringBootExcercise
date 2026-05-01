package org.eudynexc.springbootexcercise.search;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MultiMatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmSearchService {

  private final ElasticsearchOperations elasticsearchOperations;

  // changing the ElasticSearch weights for title to be 3 times more significant
  public Page<FilmDocument> search(String queryText, Pageable pageable) {
    Query multiMatch = MultiMatchQuery.of(m -> m
            .query(queryText)
            .fields("title^3", "description")
    )._toQuery();
    // Bundle in one object
    NativeQuery searchQuery = NativeQuery.builder()
            .withQuery(multiMatch)
            .withPageable(pageable)
            .build();
    // ES returned search results
    SearchHits<FilmDocument> hits = elasticsearchOperations.search(searchQuery, FilmDocument.class);
    // Strips the metadata from hits (score, highlights etc.)
    List<FilmDocument> content = hits.stream()
            .map(SearchHit::getContent)
            .toList();
    // Construct a Page for the controller
    return new PageImpl<>(content, pageable, hits.getTotalHits());
  }

  public List<FilmDocument> autocomplete(String prefix, int limit){
    Query matchQuery = MatchQuery.of(m ->m
            .field("title-autocomplete")
            .query(prefix)
    )._toQuery();

    NativeQuery searchQuery = NativeQuery.builder()
            .withQuery(matchQuery)
            .withMaxResults(limit)
            .build();

    SearchHits<FilmDocument> hits = elasticsearchOperations.search(searchQuery, FilmDocument.class);

    return hits.stream()
            .map(SearchHit::getContent)
            .toList();
  }
}