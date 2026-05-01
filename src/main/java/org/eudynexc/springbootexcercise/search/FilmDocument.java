package org.eudynexc.springbootexcercise.search;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "films")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilmDocument {

  @Id
  private Integer id;

  @Field(type = FieldType.Text)
  private String title;

  @Field(type = FieldType.Text)
  private String description;

  @Field(type = FieldType.Keyword)
  private String rating;

  @Field(type = FieldType.Integer)
  private Integer releaseYear;
}
