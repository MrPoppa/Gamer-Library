package com.benji.entitywrappers;

import com.benji.entities.Game;
import com.benji.entities.GameReceipt;
import com.benji.entities.Link;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Benjamin Bengtsson
 */
public class GameWrapper {

    private Game game;
    private GameReceipt gameReceipt;
    private List<Link> links = new ArrayList<>();

    public GameWrapper() {
    }

    public GameWrapper(Game game, GameReceipt gameReceipt) {
        this.game = game;
        this.gameReceipt = gameReceipt;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public GameReceipt getGameReceipt() {
        return gameReceipt;
    }

    public void setGameReceipt(GameReceipt gameReceipt) {
        this.gameReceipt = gameReceipt;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
