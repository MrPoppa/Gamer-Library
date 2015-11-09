package com.benji.ejb;

import com.benji.entities.PlatformBrand;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Benjamin Bengtsson
 */
@Stateless
public class PlatformBrandFacade extends AbstractFacade<PlatformBrand> {
    @PersistenceContext(unitName = "GamerLibraryPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PlatformBrandFacade() {
        super(PlatformBrand.class);
    }

}
