package pl.lodz.p.it.spjava.benefit.web.account;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.benefit.dto.AccountDTO;
import pl.lodz.p.it.spjava.benefit.ejb.endpoint.AccountEndpoint;
import pl.lodz.p.it.spjava.benefit.exception.AppBaseException;
import pl.lodz.p.it.spjava.benefit.model.AccessLevel;
import pl.lodz.p.it.spjava.benefit.web.utils.ContextUtils;

@Named(value = "listAuthorizedAccountsPageBean")
@ViewScoped
public class ListAuthorizedAccountsPageBean implements Serializable {

    @EJB
    private AccountEndpoint accountEndpoint;

    @Inject
    private AccountControllerBean accountControllerBean;

    private List<AccountDTO> listAccounts;

    private List<AccessLevel> listAccessLevels;

    public List<AccessLevel> getListAccessLevels() {
        return listAccessLevels;
    }

    private DataModel<AccountDTO> dataModelAccounts;

    public ListAuthorizedAccountsPageBean() {
    }

    public DataModel<AccountDTO> getDataModelAccounts() {
        return dataModelAccounts;
    }

    @PostConstruct
    public void initListAuthorizedAccounts() {
        try {
            listAccounts = accountControllerBean.listAuthorizedAccounts();
        } catch (AppBaseException ex) {
            Logger.getLogger(ListAuthorizedAccountsPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        dataModelAccounts = new ListDataModel<>(listAccounts);

        AccessLevel[] listAllAccessLevels = AccessLevel.values();
        for (AccessLevel accessLevel : listAllAccessLevels) {  
            accessLevel.setAccessLevelI18NValue(ContextUtils.getI18NMessage(accessLevel.getAccessLevelKey()));
        }
        listAccessLevels = new ArrayList<>(Arrays.asList(listAllAccessLevels));
        listAccessLevels.remove(AccessLevel.ACCOUNT);
        listAccessLevels.remove(AccessLevel.NEWACCOUNT);
        
    }

    public String chooseAccessLevelUserAccountAction(AccountDTO accountDTO) {
        if (accountDTO.getAccessLevel() != null) {
            try {
                accountControllerBean.chooseAccessLevelUserAccount(accountDTO);
            } catch (AppBaseException ex) {
                Logger.getLogger(ListAuthorizedAccountsPageBean.class.getName()).log(Level.SEVERE, null, ex);
                ContextUtils.emitI18NMessage(null, ex.getMessage());
            }
            initListAuthorizedAccounts();
        }
        return "listAuthorizedAccounts";
    }

    public String activateUserAccountAction(AccountDTO accountDTO) {
        try {
            accountControllerBean.activateUserAccount(accountDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListAuthorizedAccountsPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
            return null;
        }
        initListAuthorizedAccounts();
        return "listAuthorizedAccounts";
    }

    public String deactivateUserAccountAction(AccountDTO accountDTO) {
        try {
            accountControllerBean.deactivateUserAccount(accountDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListAuthorizedAccountsPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
            return null;
        }
        initListAuthorizedAccounts();
        return "listAuthorizedAccounts";
    }

    public String changeUserPasswordAction(AccountDTO accountDTO) {
        try {
            accountControllerBean.selectUserAccountForPasswordChange(accountDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListAuthorizedAccountsPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
            initListAuthorizedAccounts();
            return null;
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        return "changeUserPassword";
    }

    public String editAccountAction(AccountDTO accountDTO) {
        try {
            accountControllerBean.selectUserAccountForEdit(accountDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListAuthorizedAccountsPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
            return null;
        }
        return "userAccountEdit";
    }
}
