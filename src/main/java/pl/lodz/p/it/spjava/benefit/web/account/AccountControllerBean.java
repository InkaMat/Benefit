package pl.lodz.p.it.spjava.benefit.web.account;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import pl.lodz.p.it.spjava.benefit.dto.AccountDTO;
import pl.lodz.p.it.spjava.benefit.ejb.endpoint.AccountEndpoint;
import pl.lodz.p.it.spjava.benefit.exception.AppBaseException;
import pl.lodz.p.it.spjava.benefit.web.utils.ContextUtils;

@Named(value = "accountControllerBean")
@SessionScoped
public class AccountControllerBean implements Serializable {

    @EJB
    private AccountEndpoint accountEndpoint;

    private int lastActionMethod = 0;

    private AccountDTO selectedAccountDTO;

    private AccountDTO myAccountDTO;

    private AccountDTO accountDtoQuestionCheck;

    private AccountDTO accountDtoPasswordReset;

    private List<AccountDTO> listsDTOSelectedAccounts;

    public AccountControllerBean() {
    }

    public AccountDTO getSelectedAccountDTO() {
        return selectedAccountDTO;
    }

    public void setSelectedAccountDTO(AccountDTO selectedAccountDTO) {
        this.selectedAccountDTO = selectedAccountDTO;
    }

    public AccountDTO getMyAccountDTO() {
        return myAccountDTO;
    }

    public void setMyAccountDTO(AccountDTO myAccountDTO) {
        this.myAccountDTO = myAccountDTO;
    }

    public AccountDTO getAccountDtoQuestionCheck() {
        return accountDtoQuestionCheck;
    }

    public void setAccountDtoQuestionCheck(AccountDTO accountDtoQuestionCheck) {
        this.accountDtoQuestionCheck = accountDtoQuestionCheck;
    }

    public AccountDTO getAccountDtoPasswordReset() {
        return accountDtoPasswordReset;
    }

    public void setAccountDtoPasswordReset(AccountDTO accountDtoPasswordReset) {
        this.accountDtoPasswordReset = accountDtoPasswordReset;
    }

    public List<AccountDTO> getListsDTOSelectedAccounts() {
        return listsDTOSelectedAccounts;
    }

    //registerNewAccount
    public void registerNewAccount(final AccountDTO accountDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = accountDTO.hashCode() + 1;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                accountEndpoint.registerNewAccount(accountDTO);
                endpointCallCounter--;
            } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.repeatedTransactionRollbackException();
            }
            ContextUtils.emitI18NMessage("success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        lastActionMethod = UNIQ_METHOD_ID;
    }

    //EditMyAccount 
    public AccountDTO getMyAccountDTOForEdit() throws AppBaseException {
        final int UNIQ_METHOD_ID = accountEndpoint.hashCode() + 2;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                myAccountDTO = accountEndpoint.rememberMyAccountForDisplayAndEdit();
                endpointCallCounter--;
            } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.repeatedTransactionRollbackException();
            }
        }
        lastActionMethod = UNIQ_METHOD_ID;
        return myAccountDTO;
    }

    //EditMyAccount 
    public void editMyAccount(final AccountDTO accountDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = accountDTO.hashCode() + 3;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                accountEndpoint.editMyAccount(accountDTO);
                endpointCallCounter--;
            } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.repeatedTransactionRollbackException();
            }
            ContextUtils.emitI18NMessage(null, "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        lastActionMethod = UNIQ_METHOD_ID;
    }

    //ShowMyAccount
    public AccountDTO getMyAccountDTOForDisplay() throws AppBaseException { 
        final int UNIQ_METHOD_ID = accountEndpoint.hashCode() + 4;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                myAccountDTO = accountEndpoint.rememberMyAccountForDisplayAndEdit(); 
                endpointCallCounter--;
            } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.repeatedTransactionRollbackException();
            }
            ContextUtils.emitI18NMessage("RegisterForm:operationSuccess", "error.success");
        } else {
            ContextUtils.emitI18NMessage("RegisterForm:repeatedAction", "error.repeated.action");
        }
        lastActionMethod = UNIQ_METHOD_ID;
        return myAccountDTO;
    }

    //ChangeMyPassword
    public AccountDTO getMyAccountDTOForPasswordChange() throws AppBaseException {
        final int UNIQ_METHOD_ID = accountEndpoint.hashCode() + 5;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                selectedAccountDTO = accountEndpoint.rememberMyAccountForPasswordChange(); 
                endpointCallCounter--;
            } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.repeatedTransactionRollbackException();
            }
        }
        lastActionMethod = UNIQ_METHOD_ID;
        return selectedAccountDTO;
    }

    //ChangeMyPassword
    void changeMyPassword(AccountDTO accountDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = accountDTO.hashCode() + 6;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                accountEndpoint.changeMyPassword(accountDTO);
                endpointCallCounter--;
            } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.repeatedTransactionRollbackException();
            }
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    //listNewAccounts
    public List<AccountDTO> listNewAccounts() throws AppBaseException {
        int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
        do {
            listsDTOSelectedAccounts = accountEndpoint.listNewAccounts();
            endpointCallCounter--;
        } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
        if (endpointCallCounter == 0) {
            throw AppBaseException.repeatedTransactionRollbackException();
        }
        return listsDTOSelectedAccounts;
    }

    //ListNewAccounts    
    public void chooseAccessLevelUserAccount(final AccountDTO accountDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = accountDTO.hashCode() + 7;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                accountEndpoint.chooseAccessLevelUserAccount(accountDTO);
                endpointCallCounter--;
            } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.repeatedTransactionRollbackException();
            }
            ContextUtils.emitI18NMessage("ListNewAccountsForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    //ListNewAccounts
    public void deleteUserAccount(final AccountDTO accountDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = accountDTO.hashCode() + 8;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                accountEndpoint.deleteUserAccount(accountDTO);
                endpointCallCounter--;
            } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.repeatedTransactionRollbackException();
            }
            ContextUtils.emitI18NMessage("ListNewAccountsForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    //listAuthorizedAccounts
    public List<AccountDTO> listAuthorizedAccounts() throws AppBaseException {
        int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
        do {
            listsDTOSelectedAccounts = accountEndpoint.listAuthorizedAccounts();
            endpointCallCounter--;
        } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
        if (endpointCallCounter == 0) {
            throw AppBaseException.repeatedTransactionRollbackException();
        }
        return listsDTOSelectedAccounts;
    }

    //ListAuthorizedAccounts
    public void activateUserAccount(final AccountDTO accountDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = accountDTO.hashCode() + 9;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                accountEndpoint.activateUserAccount(accountDTO.getLogin());
                endpointCallCounter--;
            } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.repeatedTransactionRollbackException();
            }
            ContextUtils.emitI18NMessage("AuthorizedAccountsForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    //ListAuthorizedAccounts    
    public void deactivateUserAccount(final AccountDTO accountDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = accountDTO.hashCode() + 10;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                accountEndpoint.deactivateUserAccount(accountDTO.getLogin());
                endpointCallCounter--;
            } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.repeatedTransactionRollbackException();
            }
            ContextUtils.emitI18NMessage("AuthorizedAccountsForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    //ListAuthorizedAccounts  
    void selectUserAccountForPasswordChange(AccountDTO accountDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = accountDTO.hashCode() + 11;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                selectedAccountDTO = accountEndpoint.rememberUserAccountForPasswordChange(accountDTO.getLogin());
                endpointCallCounter--;
            } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.repeatedTransactionRollbackException();
            }
            lastActionMethod = UNIQ_METHOD_ID;
        }
    }

    //ListAuthorizedAccounts  
    void changeUserAccountPassword(AccountDTO accountDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = accountDTO.hashCode() + 12;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                accountEndpoint.changeUserAccountPassword(accountDTO);
                endpointCallCounter--;
            } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.repeatedTransactionRollbackException();
            }
            ContextUtils.emitI18NMessage("AuthorizedAccountsForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    //ListAuthorizedAccounts  
    void selectUserAccountForEdit(AccountDTO accountDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = accountDTO.hashCode() + 13;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                selectedAccountDTO = accountEndpoint.rememberSelectedAccountForEdit(accountDTO.getLogin());
                endpointCallCounter--;
            } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.repeatedTransactionRollbackException();
            }
        }
        lastActionMethod = UNIQ_METHOD_ID;
    }

    //ListAuthorizedAccounts
    public void editUserAccount(final AccountDTO accountDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = accountDTO.hashCode() + 14;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                accountEndpoint.editAccount(accountDTO); 
                endpointCallCounter--;
            } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.repeatedTransactionRollbackException();
            }
            ContextUtils.emitI18NMessage("AuthorizedAccountsForm:success", "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        lastActionMethod = UNIQ_METHOD_ID;
    }

    //ResetPassword1-need
    void selectAccountForQuestionCheck(AccountDTO accountDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = accountDTO.hashCode() + 15;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                accountDtoQuestionCheck = accountEndpoint.rememberSelectedAccountForPasswordResetAndQuestionCheck(accountDTO.getLogin());
                endpointCallCounter--;
            } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.repeatedTransactionRollbackException();
            }
            lastActionMethod = UNIQ_METHOD_ID;
        }
    }

    //ResetPassword2-need
    void selectAccountForPasswordReset(AccountDTO accountDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = accountDTO.hashCode() + 16;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                accountDtoPasswordReset = accountEndpoint.rememberSelectedAccountForPasswordResetAndQuestionCheck(accountDTO.getLogin());
                endpointCallCounter--;
            } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.repeatedTransactionRollbackException();
            }
        }
        lastActionMethod = UNIQ_METHOD_ID;
    }

    //ResetPassword3-need
    void resetUserAccountPassword(AccountDTO accountDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = accountDTO.hashCode() + 17;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = accountEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                accountEndpoint.resetUserAccountPassword(accountDTO);
                endpointCallCounter--;
            } while (accountEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.repeatedTransactionRollbackException();
            }
            ContextUtils.emitI18NMessage(null, "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        lastActionMethod = UNIQ_METHOD_ID;
    }

}
