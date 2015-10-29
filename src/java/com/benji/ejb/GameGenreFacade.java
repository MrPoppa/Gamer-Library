package com.benji.ejb;

import com.benji.entities.GameGenre;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Benjamin
 */
@Stateless
public class GameGenreFacade extends AbstractFacade<GameGenre> {
    @PersistenceContext(unitName = "GamerLibraryPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GameGenreFacade() {
        super(GameGenre.class);
    }

}
