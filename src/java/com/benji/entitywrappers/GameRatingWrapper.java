package com.benji.entitywrappers;

import com.benji.entities.GameRating;
import com.benji.entities.Link;
import java.util.List;

/**
 *
 * @author Benjamin
 */
public class GameRatingWrapper {

    private GameRating gameRating;
    private List<Link> links;

    public GameRatingWrapper() {
    }

    public GameRatingWrapper(GameRating gameRating, List<Link> links) {
        this.gameRating = gameRating;
        this.links = links;
    }

    public GameRating getGameRating() {
        return gameRating;
    }

    public void setGameRating(GameRating gameRating) {
        this.gameRating = gameRating;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
