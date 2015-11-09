package com.benji.ejb;

import com.benji.entities.GameRating;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Benjamin Bengtsson
 */
@Stateless
public class GameRatingFacade extends AbstractFacade<GameRating> {
    @PersistenceContext(unitName = "GamerLibraryPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GameRatingFacade() {
        super(GameRating.class);
    }

}
