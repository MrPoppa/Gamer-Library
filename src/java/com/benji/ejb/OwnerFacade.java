package com.benji.ejb;

import com.benji.entities.Game;
import com.benji.entities.GameReceipt;
import com.benji.entities.Owner;
import com.benji.entities.PlatformReceipt;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Benjamin Bengtsson
 */
@Stateless
public class OwnerFacade extends AbstractFacade<Owner> {
    @PersistenceContext(unitName = "GamerLibraryPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public OwnerFacade() {
        super(Owner.class);
    }
    
    public List<PlatformReceipt> getPlatformRecieiptByPlatformAndOwnerId(Integer platformId, Integer ownerId) {
        return getEntityManager().createNamedQuery("Owner.findPlatformRecieiptByPlatformAndOwnerId").setParameter("platformId", platformId).setParameter("ownerId", ownerId).getResultList();
    }

    public GameReceipt getGameRecieiptByGameAndOwnerId(Integer gameId, Integer ownerId) {
        return  (GameReceipt) getEntityManager().createNamedQuery("Owner.findGameRecieiptByGameAndOwnerId").setParameter("gameId", gameId).setParameter("ownerId", ownerId).getSingleResult();
    }
    
    public List<Game> getOwnedPlatformGamesByPlatformId(Integer platformId) {
        return getEntityManager().createNamedQuery("Owner.findOwnedPlatformGamesByPlatformId").setParameter("platformId", platformId).getResultList();
    }
}
