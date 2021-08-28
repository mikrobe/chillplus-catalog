package io.chillplus.tvshow;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;

/**
 * TvShowService
 */
@ApplicationScoped
public class TvShowService {
    List<TvShow> tvShows = new ArrayList<>();

    public List<TvShow> getTvShows(){
        return tvShows;
    }

    public boolean deleteById(long id) {
        return tvShows.removeIf(t -> t.id == id);
    }
    
}