package com.benji.ejb;

import com.benji.entities.GameBrand;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Benjamin Bengtsson
 */
@Stateless
public class GameBrandFacade extends AbstractFacade<GameBrand> {
    @PersistenceContext(unitName = "GamerLibraryPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GameBrandFacade() {
        super(GameBrand.class);
    }

}
