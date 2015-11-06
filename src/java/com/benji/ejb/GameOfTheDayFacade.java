package com.benji.ejb;

import com.benji.entities.GameOfTheDay;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Benjamin
 */
@Stateless
public class GameOfTheDayFacade extends AbstractFacade<GameOfTheDay> {
    @PersistenceContext(unitName = "GamerLibraryPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GameOfTheDayFacade() {
        super(GameOfTheDay.class);
    }

}
