package pl.lodz.p.it.spjava.benefit.exception;

import javax.persistence.NoResultException;
import pl.lodz.p.it.spjava.benefit.model.Account;

public class AccountException extends AppBaseException {

    public static final String KEY_ACCOUNT_LOGIN_EXIST = "error.account.login.exist.problem";
    public static final String KEY_ACCOUNT_WRONG_STATE = "error.account.wrong.state.problem";
    public static final String KEY_ACCOUNT_WRONG_PASSWORD = "error.account.wrong.password.problem";
    public static final String KEY_ACCOUNT_ALREADY_ACTIVATED = "error.account.already.activated.problem";
    public static final String KEY_ACCOUNT_ALREADY_DEACTIVATED = "error.account.already.deactivated.problem";
    public static final String KEY_ACCOUNT_YOURS_DEACTIVATED_BY_YOU = "error.yours.account.deactivated.by.you";
    public static final String KEY_ACCOUNT_ALREADY_CHANGED = "error.account.already.changed.problem";
    public static final String KEY_ACCOUNT_NOT_EXISTS = "error.account.not.exists.problem";
    public static final String KEY_ACCOUNT_NOT_ACTIVE = "error.account.not.active.problem";
    public static final String KEY_ACCOUNT_USED_IN_REWARD = "error.account.used.in.reward";
    public static final String KEY_ACCOUNT_USED_IN_OTHER_ACCOUNT = "error.account.used.in.other.accounts";
    public static final String KEY_ACCOUNT_USED_IN_ORDER = "error.account.used.in.order.problem";
    public static final String NO_RESULT = "error.no.result.problem";

    private Account account;

    public Account getAccount() {
        return account;
    }

    private AccountException(String message, Account account) {
        super(message);
        this.account = account;
    }

    private AccountException(String message, Throwable cause, Account account) {
        super(message, cause);
        this.account = account;
    }

    private AccountException(String message, Throwable cause) {
        super(message, cause);
    }

    private AccountException(String message) {
        super(message);
    }

    static public AccountException loginAlreadyExistsException(Throwable cause, Account account) {
        return new AccountException(KEY_ACCOUNT_LOGIN_EXIST, cause, account);
    }

    static public AccountException wrongStateException(Account account) {
        return new AccountException(KEY_ACCOUNT_WRONG_STATE, account);
    }

    static public AccountException wrongPasswordException(Account account) {
        return new AccountException(KEY_ACCOUNT_WRONG_PASSWORD, account);
    }

    static public AccountException accountAlreadyActvivatedException(Account account) {
        return new AccountException(KEY_ACCOUNT_ALREADY_ACTIVATED, account);
    }

    static public AccountException accountAlreadyDeactivatedException(Account account) {
        return new AccountException(KEY_ACCOUNT_ALREADY_DEACTIVATED, account);
    }

    static public AccountException accountChangedByAnotherAdministratorException(Account account) {
        return new AccountException(KEY_ACCOUNT_ALREADY_CHANGED, account);
    }

    static public AccountException accountAlreadyHaveThisAccessLevelException(Account account) {
        return new AccountException(KEY_ACCOUNT_ALREADY_CHANGED, account);
    }

    static public AccountException accountNotExistsException(NoResultException nre) {
        return new AccountException(KEY_ACCOUNT_NOT_EXISTS, nre);
    }

    public static AccountException accountNotActiveException(Account account) {
        return new AccountException(KEY_ACCOUNT_NOT_ACTIVE);
    }

    static public AccountException accountInUseInRewardException(Throwable cause, Account account) {
        return new AccountException(KEY_ACCOUNT_USED_IN_REWARD, cause, account);
    }

    static public AccountException accountInUseInOtherAccountException(Throwable cause, Account account) {
        return new AccountException(KEY_ACCOUNT_USED_IN_OTHER_ACCOUNT, cause, account);
    }

    static public AccountException accountInUseInOrderException(Throwable cause, Account account) {
        return new AccountException(KEY_ACCOUNT_USED_IN_ORDER, cause, account);
    }
    
    static public AccountException yoursAccountHasJustBeenDeactivatedByYouException(Account account) {
        return new AccountException(KEY_ACCOUNT_YOURS_DEACTIVATED_BY_YOU, account);
    }
    
}
