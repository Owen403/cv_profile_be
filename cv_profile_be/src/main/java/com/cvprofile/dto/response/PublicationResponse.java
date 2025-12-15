package com.cvprofile.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PublicationResponse {
    private Long id;
    private Long userId;
    private String title;
    private String authors;
    private String journal;
    private LocalDate publishDate;
    private String doi;
    private String url;
    private Integer citations;
}
