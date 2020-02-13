package pl.lodz.p.it.spjava.benefit.ejb.facade;

import java.sql.SQLNonTransientConnectionException;
import javax.annotation.security.RolesAllowed;
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
import pl.lodz.p.it.spjava.benefit.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.spjava.benefit.exception.AppBaseException;
import pl.lodz.p.it.spjava.benefit.model.Employee;

@Stateless
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors(LoggingInterceptor.class)
public class EmployeeFacade extends AbstractFacade<Employee> {

    @PersistenceContext(unitName = "BenefitPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EmployeeFacade() {
        super(Employee.class);
    }

    @RolesAllowed({"Administrator", "Manager", "Employee"})
    public Employee findByLogin(String login) throws AppBaseException {
        TypedQuery<Employee> tq = em.createNamedQuery("Employee.findByLogin", Employee.class);
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
