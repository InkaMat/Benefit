package pl.lodz.p.it.spjava.benefit.web.account;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.benefit.dto.AccountDTO;
import pl.lodz.p.it.spjava.benefit.ejb.endpoint.AccountEndpoint;
import pl.lodz.p.it.spjava.benefit.exception.AppBaseException;
import pl.lodz.p.it.spjava.benefit.web.utils.ContextUtils;

@Named(value = "userAccountEditPageBean")
@RequestScoped
public class EditUserAccountPageBean {

    @EJB
    private AccountEndpoint accountEndpoint;

    @Inject
    private AccountControllerBean accountControllerBean;

    private AccountDTO accountDTO;

    public EditUserAccountPageBean() {
    }

    public AccountDTO getAccountDTO() {
        return accountDTO;
    }

    public void setAccountDTO(AccountDTO accountDTO) {
        this.accountDTO = accountDTO;
    }

    @PostConstruct
    public void init() {
        accountDTO = accountControllerBean.getSelectedAccountDTO();
    }

    public String saveEditAccountAction() {
        try {
            accountControllerBean.editUserAccount(accountDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(EditUserAccountPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
            ContextUtils.getContext().getFlash().setKeepMessages(true);
            return "listAuthorizedAccounts";

        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        return "listAuthorizedAccounts";
    }

}
