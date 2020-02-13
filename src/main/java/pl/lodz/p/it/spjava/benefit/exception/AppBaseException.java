package pl.lodz.p.it.spjava.benefit.exception;

import javax.ejb.ApplicationException;
import javax.persistence.OptimisticLockException;


@ApplicationException(rollback = true)
public class AppBaseException extends Exception {

    public static final String KEY_OPTIMISTIC_LOCK = "error.optimistic.lock.problem";
    public static final String KEY_REPEATED_TRANSACTION_ROLLBACK = "error.repeated.transaction.rollback.problem";
    public static final String KEY_DB_QUERY_PROBLEM = "error.database.query.problem";
    public static final String KEY_DB_CONNECTION_PROBLEM = "error.database.connection.problem";
    public static final String KEY_NOT_AUTHORIZED_ACTION = "error.not.authorized.account.problem";

    protected AppBaseException(String message, Throwable cause) {
        super(message, cause);
    }

    protected AppBaseException(String message) {
        super(message);
    }

    public static AppBaseException repeatedTransactionRollbackException() {
        return new AppBaseException(KEY_REPEATED_TRANSACTION_ROLLBACK);
    }

    public static AppBaseException databaseQueryProblemException(Throwable e) {
        return new AppBaseException(KEY_DB_QUERY_PROBLEM);
    }

    public static AppBaseException databaseConnectionProblemException(Throwable e) {
        return new AppBaseException(KEY_DB_CONNECTION_PROBLEM);
    }

    public static AppBaseException optimisticLockException(OptimisticLockException ole) {
        return new AppBaseException(KEY_OPTIMISTIC_LOCK);
    }
    
    public static AppBaseException notAuthorizedActionException() {
        return new AppBaseException(KEY_NOT_AUTHORIZED_ACTION);
    }
 
}

