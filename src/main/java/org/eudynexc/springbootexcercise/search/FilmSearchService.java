package org.eudynexc.springbootexcercise.search;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
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
  public Page<FilmDocument> search(
          String queryText,
          String language,
          String priceBracket,
          String lengthBracket,
          List<String> ratings,
          Pageable pageable) {
    BoolQuery.Builder boolBuilder = new BoolQuery.Builder();

    // Text search across title + description with boost
    if (queryText != null && !queryText.isBlank()) {
      boolBuilder.must(MultiMatchQuery.of(m -> m
              .query(queryText)
              .fields("title^3", "description")
              .fuzziness("AUTO")
      )._toQuery());
    } else {
      boolBuilder.must(MatchAllQuery.of(m -> m)._toQuery());
    }

    // Language filter - exact match
    if (language != null && !language.isBlank()) {
      boolBuilder.filter(TermQuery.of(t -> t
              .field("language")
              .value(language)
      )._toQuery());
    }

    // Ratings filter - multi select
    if (ratings != null && !ratings.isEmpty()) {
      List<FieldValue> values = ratings.stream()
              .map(FieldValue::of)
              .toList();
      boolBuilder.filter(TermsQuery.of(t -> t
              .field("rating")
              .terms(builder -> builder.value(values))
      )._toQuery());
    }

    // Price bracket - range filter
    if (priceBracket != null) {
      Range range = priceRange(priceBracket);
      if (range != null) {
        boolBuilder.filter(RangeQuery.of(r -> r
                .number(n -> n
                        .field("rentalRate")
                        .gte(range.min)
                        .lt(range.max)
                )
        )._toQuery());
      }
    }

    // length bracket - range filter
    if (lengthBracket != null) {
      Range range = lengthRange(lengthBracket);
      if (range != null) {
        boolBuilder.filter(RangeQuery.of(r -> r
                .number(n -> n
                        .field("length")
                        .gt(range.min)
                        .lt(range.max)
                )
        )._toQuery());
      }
    }
    NativeQuery searchQuery = NativeQuery.builder()
            .withQuery(boolBuilder.build()._toQuery())
            .withPageable(pageable)
            .build();

    SearchHits<FilmDocument> hits = elasticsearchOperations.search(searchQuery, FilmDocument.class);

    List<FilmDocument> content = hits.stream()
            .map(SearchHit::getContent)
            .toList();

    return new PageImpl<>(content, pageable, hits.getTotalHits());
  }

  private record Range(double min, double max) {
  }

  private Range priceRange(String priceBracket) {
    return switch (priceBracket.toLowerCase()) {
      case "cheap" -> new Range(0.0, 2.0);
      case "standard" -> new Range(2.0, 4.0);
      case "premium" -> new Range(4.0, Double.MAX_VALUE);
      default -> null;
    };
  }

  private Range lengthRange(String lengthBracket) {
    return switch (lengthBracket.toLowerCase()) {
      case "short" -> new Range(0, 90);
      case "standard" -> new Range(90, 120);
      case "premium" -> new Range(120, Double.MAX_VALUE);
      default -> null;
    };
  }


  public List<FilmDocument> autocomplete(String prefix, int limit) {
    Query matchQuery = MatchQuery.of(m -> m
            .field("title.autocomplete")
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