package io.chillplus.tvshow;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TvShow {
    Long id;
    String title;
    String category;
}
