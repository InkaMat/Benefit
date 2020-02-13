package pl.lodz.p.it.spjava.benefit.web.utils;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import pl.lodz.p.it.spjava.benefit.dto.AccountDTO;
import pl.lodz.p.it.spjava.benefit.ejb.endpoint.AccountEndpoint;
import pl.lodz.p.it.spjava.benefit.exception.AppBaseException;
import pl.lodz.p.it.spjava.benefit.web.account.ShowMyAccountPageBean;

@Named(value = "mainApplicationPageBean")
@ApplicationScoped
public class MainApplicationBean {

    @EJB
    private AccountEndpoint accountEndpoint;

    public MainApplicationBean() {
    }

    AccountDTO accountDTO = new AccountDTO();

    // Wylogowanie sesji
    public String logOutAction() {
        ContextUtils.invalidateSession();
        return "main";
    }

    // Login obecnie uwierzytelnionego użytkownika
    public String getMyLogin() {
        return ContextUtils.getUserName();
    }

    // Poziom dostępu obecnie zalogowanego użytkownika
    public String myAccessLevel() {
        try {
            return accountEndpoint.getI18nAccountForAccessLevelDisplay();
        } catch (AppBaseException ex) {
            Logger.getLogger(ShowMyAccountPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        return null;
    }
    
 

}
