package com.benji.ejb;

import com.benji.entities.GameReceipt;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Benjamin
 */
@Stateless
public class GameReceiptFacade extends AbstractFacade<GameReceipt> {
    @PersistenceContext(unitName = "GamerLibraryPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GameReceiptFacade() {
        super(GameReceipt.class);
    }

}
