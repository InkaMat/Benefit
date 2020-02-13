package pl.lodz.p.it.spjava.benefit.ejb.facade;

import java.sql.SQLNonTransientConnectionException;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import org.eclipse.persistence.exceptions.DatabaseException;
import pl.lodz.p.it.spjava.benefit.model.Administrator;
import pl.lodz.p.it.spjava.benefit.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.spjava.benefit.exception.AppBaseException;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors(LoggingInterceptor.class)
public class AdministratorFacade extends AbstractFacade<Administrator> {

    @PersistenceContext(unitName = "BenefitPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AdministratorFacade() {
        super(Administrator.class);
    }

    @PermitAll //reset hasła
    public Administrator findByLogin(String login) throws AppBaseException {
        TypedQuery<Administrator> tq = em.createNamedQuery("Administrator.findByLogin", Administrator.class);
        tq.setParameter("login", login);
        try {
            return tq.getSingleResult();
        } catch (NoResultException nre) {
            return null;
        } catch (PersistenceException pe) {
            final Throwable cause = pe.getCause();
            if (cause instanceof DatabaseException && cause.getCause() instanceof SQLNonTransientConnectionException) {
                throw AppBaseException.databaseConnectionProblemException(pe);
            } else {
                throw AppBaseException.databaseQueryProblemException(cause);
            }
        }
    }

}
