package com.benji.entitywrappers;

import com.benji.entities.GameBrand;
import com.benji.entities.Link;
import java.util.List;

/**
 *
 * @author Benjamin Bengtsson
 */
public class GameBrandWrapper {
    
    private GameBrand gameBrand;
    private List<Link> links;

    public GameBrandWrapper() {
    }

    public GameBrandWrapper(GameBrand gameBrand, List<Link> links) {
        this.gameBrand = gameBrand;
        this.links = links;
    }

    public GameBrand getGameBrand() {
        return gameBrand;
    }

    public void setGameBrand(GameBrand gameBrand) {
        this.gameBrand = gameBrand;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
