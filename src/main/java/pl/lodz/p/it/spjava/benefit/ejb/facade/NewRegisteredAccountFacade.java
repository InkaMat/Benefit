package pl.lodz.p.it.spjava.benefit.ejb.facade;

import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pl.lodz.p.it.spjava.benefit.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.spjava.benefit.model.NewRegisteredAccount;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors(LoggingInterceptor.class)
@RolesAllowed("Administrator")
public class NewRegisteredAccountFacade extends AbstractFacade<NewRegisteredAccount> {

    @PersistenceContext(unitName = "BenefitPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NewRegisteredAccountFacade() {
        super(NewRegisteredAccount.class);
    }

}
