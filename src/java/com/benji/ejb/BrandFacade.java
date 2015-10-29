package com.benji.ejb;

import com.benji.entities.Brand;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Benjamin
 */
@Stateless
public class BrandFacade extends AbstractFacade<Brand> {
    @PersistenceContext(unitName = "GamerLibraryPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public BrandFacade() {
        super(Brand.class);
    }

}
