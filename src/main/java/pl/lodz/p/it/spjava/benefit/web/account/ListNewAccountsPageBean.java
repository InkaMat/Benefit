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
import pl.lodz.p.it.spjava.benefit.ejb.endpoint.AccountEndpoint;
import pl.lodz.p.it.spjava.benefit.model.AccessLevel;
import pl.lodz.p.it.spjava.benefit.dto.AccountDTO;
import pl.lodz.p.it.spjava.benefit.exception.AppBaseException;
import pl.lodz.p.it.spjava.benefit.web.utils.ContextUtils;

@Named(value = "listNewAccountsPageBean")
@ViewScoped
public class ListNewAccountsPageBean implements Serializable{

    @EJB
    private AccountEndpoint accountEndpoint;

    @Inject
    private AccountControllerBean accountControllerBean;

    private List<AccountDTO> listNewAccounts;

    private List<AccessLevel> listAccessLevels;

    public List<AccessLevel> getListAccessLevels() {
        return listAccessLevels;
    }

    private DataModel<AccountDTO> dataModelAccounts;

    public ListNewAccountsPageBean() {
    }

    public DataModel<AccountDTO> getDataModelAccounts() {
        return dataModelAccounts;
    }

    @PostConstruct
    public void initListNewAccounts() {
        try {
            listNewAccounts = accountControllerBean.listNewAccounts();
        } catch (AppBaseException ex) {
            Logger.getLogger(ListNewAccountsPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        dataModelAccounts = new ListDataModel<>(listNewAccounts);

        AccessLevel[] listAllAccessLevels = AccessLevel.values();
        for (AccessLevel accessLevel : listAllAccessLevels) {
            accessLevel.setAccessLevelI18NValue(ContextUtils.getI18NMessage(accessLevel.getAccessLevelKey()));
        }

        listAccessLevels = new ArrayList<>(Arrays.asList(listAllAccessLevels));
        listAccessLevels.remove(AccessLevel.ACCOUNT);
        listAccessLevels.remove(AccessLevel.NEWACCOUNT);
    }

    public String deleteUserAccountAction(AccountDTO accountDTO) {
        try {
            accountControllerBean.deleteUserAccount(accountDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(ListNewAccountsPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        initListNewAccounts();
        return null;
    }

    public String chooseAccessLevelUserAccountAction(AccountDTO accountDTO) {
        if (accountDTO.getAccessLevel() != null) {
            try {
                accountControllerBean.chooseAccessLevelUserAccount(accountDTO);
            } catch (AppBaseException ex) {
                Logger.getLogger(ListNewAccountsPageBean.class.getName()).log(Level.SEVERE, null, ex);
                ContextUtils.emitI18NMessage(null, ex.getMessage());
            }
            initListNewAccounts();
        }
        return null;
    }
}
