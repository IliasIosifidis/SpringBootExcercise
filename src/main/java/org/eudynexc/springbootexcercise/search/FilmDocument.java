package org.eudynexc.springbootexcercise.search;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.*;

@Document(indexName = "films")
@Setting(settingPath = "elasticsearch/film-settings.json")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilmDocument {

  @Id
  private Integer id;

  @MultiField(
          mainField = @Field(type = FieldType.Text),
          otherFields = {
                  @InnerField(suffix = "autocomplete", type = FieldType.Text,
                  analyzer = "autocomplete_analyzer",
                  searchAnalyzer = "standard")
          }
  )
  private String title;

  @Field(type = FieldType.Text)
  private String description;

  @Field(type = FieldType.Keyword)
  private String rating;

  @Field(type = FieldType.Keyword)
  private String language;

  @Field(type = FieldType.Double)
  private Double rentalRate;

  @Field(type = FieldType.Integer)
  private Integer length;

  @Field(type = FieldType.Integer)
  private Integer releaseYear;
}
