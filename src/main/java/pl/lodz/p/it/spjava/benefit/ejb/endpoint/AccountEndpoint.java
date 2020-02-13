package pl.lodz.p.it.spjava.benefit.ejb.endpoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import javax.persistence.OptimisticLockException;
import pl.lodz.p.it.spjava.benefit.dto.AccountDTO;
import pl.lodz.p.it.spjava.benefit.ejb.facade.AccountFacade;
import pl.lodz.p.it.spjava.benefit.ejb.facade.AdministratorFacade;
import pl.lodz.p.it.spjava.benefit.ejb.facade.EmployeeFacade;
import pl.lodz.p.it.spjava.benefit.ejb.facade.ManagerFacade;
import pl.lodz.p.it.spjava.benefit.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.spjava.benefit.exception.AccountException;
import pl.lodz.p.it.spjava.benefit.exception.AppBaseException;
import pl.lodz.p.it.spjava.benefit.model.AccessLevel;
import pl.lodz.p.it.spjava.benefit.model.Account;
import pl.lodz.p.it.spjava.benefit.model.Administrator;
import pl.lodz.p.it.spjava.benefit.model.NewRegisteredAccount;
import pl.lodz.p.it.spjava.benefit.model.Manager;
import pl.lodz.p.it.spjava.benefit.model.Employee;
import pl.lodz.p.it.spjava.benefit.web.utils.ContextUtils;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors(LoggingInterceptor.class)
public class AccountEndpoint extends AbstractEndpoint implements SessionSynchronization {

    @EJB
    private AdministratorFacade administratorFacade;

    @EJB
    private EmployeeFacade employeeFacade;

    @EJB
    private ManagerFacade managerFacade;

    @EJB
    private AccountFacade accountFacade;

    @Resource
    private SessionContext sessionContext;

    private Account accountState;

    private Account myAccountState;

    private Account questionCheckAccountState;

    private Account passwordResetAccountState;

    private List<Account> savedAccountStateList;

    @RolesAllowed({"Administrator"})
    public Account getAccountState() {
        return accountState;
    }

    @RolesAllowed({"Administrator"})
    public void setAccountState(Account accountState) {
        this.accountState = accountState;
    }

    @RolesAllowed({"Administrator", "Manager", "Employee"})
    public Account getMyAccountState() {
        return myAccountState;
    }

    @RolesAllowed({"Administrator", "Manager", "Employee"})
    public void setMyAccountState(Account myAccountState) {
        this.myAccountState = myAccountState;
    }

    public Account getQuestionCheckAccountState() {
        return questionCheckAccountState;
    }

    public void setQuestionCheckAccountState(Account questionCheckAccountState) {
        this.questionCheckAccountState = questionCheckAccountState;
    }

    public Account getPasswordResetAccountState() throws AccountException {
        if (!passwordResetAccountState.getLogin().equals(questionCheckAccountState.getLogin())) { 
            throw AccountException.wrongStateException(passwordResetAccountState);
        }
        return passwordResetAccountState;
    }

    public void setPasswordResetAccountState(Account passwordResetAccountState) throws AccountException {
        if (!passwordResetAccountState.getLogin().equals(questionCheckAccountState.getLogin())) {
            throw AccountException.wrongStateException(passwordResetAccountState);
        }
        this.passwordResetAccountState = passwordResetAccountState;
    }

    @RolesAllowed({"Administrator"})
    private Administrator loadCurrentAdministrator() throws AppBaseException {
        String administratorLogin = sessionContext.getCallerPrincipal().getName();
        Administrator administratorAccount = administratorFacade.findByLogin(administratorLogin);
        if (administratorAccount == null) {
            throw AppBaseException.notAuthorizedActionException();
        }
        if (!administratorAccount.isActive()) {
            throw AccountException.accountNotActiveException(administratorAccount);
        }
        return administratorAccount;
    }

    @PermitAll
    public void registerNewAccount(AccountDTO accountDTO) throws AppBaseException {
        Account newRegisteredAccount = new NewRegisteredAccount();

        newRegisteredAccount.setLogin(accountDTO.getLogin());
        newRegisteredAccount.setPassword(accountDTO.getPassword());
        newRegisteredAccount.setQuestion(accountDTO.getQuestion());
        newRegisteredAccount.setAnswer(accountDTO.getAnswer());
        newRegisteredAccount.setName(accountDTO.getName());
        newRegisteredAccount.setSurname(accountDTO.getSurname());
        newRegisteredAccount.setPhone(accountDTO.getPhone());

        newRegisteredAccount.setActive(false);
        newRegisteredAccount.setAuthorized(false);

        accountFacade.create(newRegisteredAccount);

    }

// ------------------ New Accounts List
    @RolesAllowed({"Administrator"})
    public List<AccountDTO> listNewAccounts() throws AppBaseException {
        List<Account> listNewAccount = accountFacade.findNewRegisteredAccounts();
         savedAccountStateList = null;
        savedAccountStateList = listNewAccount;
        List<AccountDTO> listNewRegisteredAccount = new ArrayList<>();
        for (Account account : listNewAccount) {
            AccountDTO accountDTO = new AccountDTO(
                    account.getLogin(),
                    account.getName(),
                    account.getSurname(),
                    account.getPhone(),
                    account.getCreationDate()
            );
            listNewRegisteredAccount.add(accountDTO);
        }
          Collections.sort(listNewRegisteredAccount);
        return listNewRegisteredAccount;
    }

    @RolesAllowed({"Administrator"})
    public void deleteUserAccount(AccountDTO accountDTO) throws AppBaseException { // listNewAccounts
        Account userAccount = accountFacade.findByLogin(accountDTO.getLogin());
        if (userAccount instanceof NewRegisteredAccount) {
            accountFacade.remove(userAccount);
        } else {
            throw AccountException.accountChangedByAnotherAdministratorException(accountState);
        }
    }

    @RolesAllowed({"Administrator"})
    public void chooseAccessLevelUserAccount(AccountDTO accountDTO) throws AppBaseException { // listNewAccounts & authorizedAccountsList
        Account userAccount = accountFacade.findByLogin(accountDTO.getLogin());
        if (accountDTO.getCreationDate() == null || accountDTO.getCreationDate().equals(userAccount.getCreationDate())) {
            Account newAccount = null;
            switch (accountDTO.getAccessLevel()) {
                case MANAGER:
                    newAccount = new Manager(userAccount);
                    break;
                case ADMINISTRATOR:
                    newAccount = new Administrator(userAccount);
                    break;
                case EMPLOYEE:
                    newAccount = new Employee(userAccount);
                    break;
            }

            if (newAccount != null) {
                accountFacade.remove(userAccount);
                if (!newAccount.isAuthorized()) { 
                    newAccount.setAuthorized(true);
                    newAccount.setActive(true);
                }
                newAccount.setAuthorizedBy(loadCurrentAdministrator()); 
                accountFacade.create(newAccount);
            }
        } else {
            try {
                throw new OptimisticLockException("Konto zostało już edytowane przez innego administratora");
            } catch (OptimisticLockException ole) {
                throw AppBaseException.optimisticLockException(ole);
            }
        }
    }

    @RolesAllowed({"Administrator"})
    public List<AccountDTO> listAuthorizedAccounts() throws AppBaseException {
        List<Account> listAuthorizedAccounts = accountFacade.findAuthorizedAccount();
        savedAccountStateList = listAuthorizedAccounts;
        List<AccountDTO> listAccounts = new ArrayList<>();
        for (Account account : listAuthorizedAccounts) {
            AccountDTO accountDTO = new AccountDTO(
                    account.getLogin(),
                    account.getName(),
                    account.getSurname(),
                    account.getPhone(),
                    account.isActive(),
                    account.getCreationDate()
            );

            Administrator administratorAccount = administratorFacade.findByLogin(account.getLogin());
            Employee employeeAccount = employeeFacade.findByLogin(account.getLogin());
            Manager managerAccount = managerFacade.findByLogin(account.getLogin());

            if (administratorAccount instanceof Administrator) {
                accountDTO.setAccessLevel(AccessLevel.ADMINISTRATOR);
            }
            if (employeeAccount instanceof Employee) {
                accountDTO.setAccessLevel(AccessLevel.EMPLOYEE);
            }
            if (managerAccount instanceof Manager) {
                accountDTO.setAccessLevel(AccessLevel.MANAGER);
            }
            listAccounts.add(accountDTO);
        }
        Collections.sort(listAccounts);
        return listAccounts;
    }

    @RolesAllowed({"Administrator"})
    public void activateUserAccount(String login) throws AppBaseException {
        Account userAccount = accountFacade.findByLogin(login);

        if (!administratorFacade.findByLogin(sessionContext.getCallerPrincipal().getName()).isActive()) {
            throw AccountException.accountNotActiveException(userAccount);
        }
        if (!userAccount.isActive()) {
            userAccount.setActive(true);
            userAccount.setModificatedBy(loadCurrentAdministrator());
            accountFacade.edit(userAccount);
        } else {
            throw AccountException.accountAlreadyActvivatedException(userAccount);
        }
    }

    @RolesAllowed({"Administrator"})
    public void deactivateUserAccount(String login) throws AppBaseException {
        Account userAccount = accountFacade.findByLogin(login);
        if (!userAccount.isActive()) {
            throw AccountException.accountAlreadyDeactivatedException(userAccount);
        } else {
            userAccount.setActive(false);
            if (!userAccount.getLogin().equals(loadCurrentAdministrator().getLogin())) {
                userAccount.setModificatedBy(loadCurrentAdministrator());
                accountFacade.edit(userAccount);
            } else {
                throw AccountException.yoursAccountHasJustBeenDeactivatedByYouException(userAccount);
            }
        }
    }

    @RolesAllowed({"Administrator"})
    public void changeUserAccountPassword(AccountDTO accountDTO) throws AppBaseException {
        if (accountState.getLogin().equals(accountDTO.getLogin())) {
            accountState.setPassword(accountDTO.getPassword());
            accountState.setModificatedBy(loadCurrentAdministrator());
            accountFacade.edit(accountState);
        } else {
            throw AccountException.wrongStateException(accountState);
        }
    }

    @RolesAllowed({"Administrator"})
    public void editAccount(AccountDTO accountDTO) throws AppBaseException { 
        if (accountState.getLogin().equals(accountDTO.getLogin())) {
            accountState.setName(accountDTO.getName());
            accountState.setSurname(accountDTO.getSurname());
            accountState.setPhone(accountDTO.getPhone());
            accountState.setQuestion(accountDTO.getQuestion());
            accountState.setAnswer(accountDTO.getAnswer());
            accountState.setModificatedBy(loadCurrentAdministrator());
            accountFacade.edit(accountState);
        } else {
            throw AccountException.wrongStateException(accountState);
        }
    }

    @RolesAllowed({"Administrator", "Manager", "Employee"})
    public void editMyAccount(AccountDTO accountDTO) throws AppBaseException { 

        Account tempAccountForPasswordHash = new Account();
        tempAccountForPasswordHash.setPassword(accountDTO.getPassword());

        if (myAccountState.getLogin().equals(accountDTO.getLogin())) {
            if (myAccountState.getPassword().equals(tempAccountForPasswordHash.getPassword())) {
                myAccountState.setName(accountDTO.getName());
                myAccountState.setSurname(accountDTO.getSurname());
                myAccountState.setPhone(accountDTO.getPhone());
                myAccountState.setModificatedBy(myAccountState.getClass().getSimpleName().equals("Administrator") ? loadCurrentAdministrator() : null);
                accountFacade.edit(myAccountState);
            } else {
                throw AccountException.wrongPasswordException(myAccountState);
            }
        } else {
            throw AccountException.wrongStateException(myAccountState);
        }
    }

    @RolesAllowed({"Administrator", "Manager", "Employee"})
    public void changeMyPassword(AccountDTO accountDTO) throws AppBaseException {

        if (myAccountState.getLogin().equals(accountDTO.getLogin())) {
            myAccountState = accountFacade.findByLogin(sessionContext.getCallerPrincipal().getName()); 

            Account account = new Account();
            account.setPassword(accountDTO.getOldPassword()); 
            if ((myAccountState.getPassword().equals(account.getPassword()))) {
                myAccountState.setPassword(accountDTO.getPassword());
                myAccountState.setModificatedBy(myAccountState.getClass().getSimpleName().equals("Administrator") ? loadCurrentAdministrator() : null);
                accountFacade.edit(myAccountState);
            } else {
                throw AccountException.wrongPasswordException(myAccountState);
            }
        } else {
            throw AccountException.wrongStateException(myAccountState);
        }
    }

    @RolesAllowed({"Administrator", "Manager", "Employee"})
    public String getI18nAccountForAccessLevelDisplay() throws AppBaseException {
        accountState = accountFacade.findByLogin(sessionContext.getCallerPrincipal().getName());
        String accessLevel;
        switch (accountState.getClass().getSimpleName()) {
            case "Administrator":
                accessLevel = "access.level.administrator";
                break;
            case "Manager":
                accessLevel = "access.level.manager";
                break;
            case "Employee":
                accessLevel = "access.level.employee";
                break;
            default:
                accessLevel = "application.no.authentication";
        }
        return ContextUtils.printI18NMessage(accessLevel);
    }

    @RolesAllowed({"Administrator"})
    public AccountDTO rememberUserAccountForPasswordChange(String login) throws AppBaseException {
        accountState = accountFacade.findByLogin(login);
        return new AccountDTO(
                accountState.getLogin()
        );
    }

    @RolesAllowed({"Administrator"})
    public AccountDTO rememberSelectedAccountForEdit(String login) throws AppBaseException {
        accountState = accountFacade.findByLogin(login);
        return new AccountDTO(
                accountState.getLogin(),
                accountState.getQuestion(),
                accountState.getAnswer(),
                accountState.getName(),
                accountState.getSurname(),
                accountState.getPhone()
        );
    }


    @PermitAll
    public AccountDTO rememberSelectedAccountForPasswordResetAndQuestionCheck(String login) throws AppBaseException { 
        passwordResetAccountState = accountFacade.findByLogin(login);
        return new AccountDTO(
                passwordResetAccountState.getLogin(),
                passwordResetAccountState.getQuestion(),
                passwordResetAccountState.getAnswer(),
                passwordResetAccountState.isActive()
        );
    }


    @PermitAll
    public void resetUserAccountPassword(AccountDTO accountDTO) throws AppBaseException {
        if (passwordResetAccountState.getLogin().equals(accountDTO.getLogin())) {
            passwordResetAccountState.setPassword(accountDTO.getPassword());
            passwordResetAccountState.setModificatedBy(passwordResetAccountState.getClass().getSimpleName().equals("Administrator") ? administratorFacade.findByLogin(accountDTO.getLogin()) : null);
            accountFacade.edit(passwordResetAccountState);
        } else {
            throw AccountException.wrongStateException(passwordResetAccountState);
        }
    }

    @RolesAllowed({"Administrator", "Manager", "Employee"})
    public AccountDTO rememberMyAccountForDisplayAndEdit() throws AppBaseException {
        myAccountState = accountFacade.findByLogin(sessionContext.getCallerPrincipal().getName());
        return new AccountDTO(
                myAccountState.getLogin(),
                myAccountState.getPassword(),
                myAccountState.getName(),
                myAccountState.getSurname(),
                myAccountState.getPhone(),
                myAccountState.isActive()
        );
    }

    @RolesAllowed({"Administrator", "Manager", "Employee"})
    public AccountDTO rememberMyAccountForPasswordChange() throws AppBaseException {
        myAccountState = accountFacade.findByLogin(sessionContext.getCallerPrincipal().getName());
        return new AccountDTO(
                myAccountState.getLogin(),
                myAccountState.getPassword()
        );
    }
}
