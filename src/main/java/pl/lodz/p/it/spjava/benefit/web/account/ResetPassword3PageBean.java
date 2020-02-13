package pl.lodz.p.it.spjava.benefit.web.account;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.benefit.dto.AccountDTO;
import pl.lodz.p.it.spjava.benefit.exception.AppBaseException;
import pl.lodz.p.it.spjava.benefit.web.utils.ContextUtils;

@Named(value = "resetPassword3PageBean")
@RequestScoped
public class ResetPassword3PageBean {

    @Inject
    private AccountControllerBean accountControllerBean;

    private AccountDTO accountDTO;

    private String repeatNewPassword;

    public ResetPassword3PageBean() {
    }

    public AccountDTO getAccountDTO() {
        return accountDTO;
    }

    public void setAccountDTO(AccountDTO accountDTO) {
        this.accountDTO = accountDTO;
    }

    public String getRepeatNewPassword() {
        return repeatNewPassword;
    }

    public void setRepeatNewPassword(String repeatNewPassword) {
        this.repeatNewPassword = repeatNewPassword;
    }
    
       @PostConstruct
    public void init() {
        accountDTO = accountControllerBean.getAccountDtoPasswordReset();
        if (accountDTO == null) {
            accountDTO = new AccountDTO();
        }
    }

    public String saveResetedUserPasswordAction() {
        if (accountControllerBean.getAccountDtoQuestionCheck() == null) {
            ContextUtils.emitI18NMessage(null, "error.link.forced");
            ContextUtils.getContext().getFlash().setKeepMessages(true);
            return "main";
        }
        if (accountDTO.getLogin() == (accountControllerBean.getAccountDtoQuestionCheck().getLogin())) {
            if (repeatNewPassword.equals(accountDTO.getPassword())) {
                try {
                    accountControllerBean.resetUserAccountPassword(accountDTO);
                } catch (AppBaseException ex) {
                    Logger.getLogger(ResetPassword2PageBean.class.getName()).log(Level.SEVERE, null, ex);
                    ContextUtils.emitI18NMessage(null, ex.getMessage());
                    return null;
                }
                accountControllerBean.setAccountDtoQuestionCheck(null);
                accountControllerBean.setAccountDtoPasswordReset(null);
                ContextUtils.getContext().getFlash().setKeepMessages(true);
                return "main";
            }
            ContextUtils.emitI18NMessage("ResetPassword3Form:repeatNewPassword", "passwords.not.matching");
            return null;
        }
        if (accountControllerBean.getAccountDtoPasswordReset() == null) {
            ContextUtils.emitI18NMessage(null, "error.link.forced");
        } else {
            ContextUtils.emitI18NMessage(null, "error.account.wrong.state.problem");
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        return "main";
    }
}
