package com.benji.ejb;

import com.benji.entities.Platform;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Benjamin Bengtsson
 */
@Stateless
public class PlatformFacade extends AbstractFacade<Platform> {
    @PersistenceContext(unitName = "GamerLibraryPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PlatformFacade() {
        super(Platform.class);
    }

    public List<Platform> getAllPlatformsByOwnerId(int ownerId) {
        return getEntityManager().createNamedQuery("Platform.findAllPlatformsByOwnerId").setParameter("ownerId", ownerId).getResultList();
    }
}
