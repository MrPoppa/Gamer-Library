package com.benji.entitywrappers;

import com.benji.entities.Genre;
import com.benji.entities.Link;
import java.util.List;

/**
 *
 * @author Benjamin
 */
public class GenreWrapper {

    private Genre genre;
    private List<Link> links;

    public GenreWrapper(Genre genre, List<Link> links) {
        this.genre = genre;
        this.links = links;
    }

    public GenreWrapper() {
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
    
}
