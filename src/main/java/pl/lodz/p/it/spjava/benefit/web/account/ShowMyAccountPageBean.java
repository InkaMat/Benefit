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

@Named(value = "MyAccountShowPageBean")
@RequestScoped
public class ShowMyAccountPageBean {

    @EJB
    private AccountEndpoint accountEndpoint;

    @Inject
    private AccountControllerBean accountControllerBean;
    
    private AccountDTO accountDTO;

    private String name;
    private String surname;
    private String phone;
    private String login;
    
    public ShowMyAccountPageBean() {
    }

    public AccountDTO getAccountDTO() {
        return accountDTO;
    }

    public void setAccountDTO(AccountDTO accountDTO) {
        this.accountDTO = accountDTO;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLogin() {
        return login;
    }

    @PostConstruct
    public void init() {
try {
        accountDTO = accountControllerBean.getMyAccountDTOForDisplay();
        } catch (AppBaseException ex) {
            Logger.getLogger(ShowMyAccountPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
    }

}
