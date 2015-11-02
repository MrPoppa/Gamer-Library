package com.benji.entitywrappers;

import com.benji.entities.Game;
import com.benji.utils.Link;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Benjamin
 */
public class GameWrapper {

    private Game game;
    private List<Link> links = new ArrayList<>();

    public GameWrapper() {
    }

    public GameWrapper(Game game, List<Link> links) {
        this.game = game;
        this.links = links;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
