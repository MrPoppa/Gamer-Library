package com.benji.ejb;

import com.benji.entities.PlatformReceipt;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Benjamin Bengtsson
 */
@Stateless
public class PlatformReceiptFacade extends AbstractFacade<PlatformReceipt> {
    @PersistenceContext(unitName = "GamerLibraryPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PlatformReceiptFacade() {
        super(PlatformReceipt.class);
    }

}
