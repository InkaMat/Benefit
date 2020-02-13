package pl.lodz.p.it.spjava.benefit.web.account;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.benefit.dto.AccountDTO;
import pl.lodz.p.it.spjava.benefit.exception.AppBaseException;
import pl.lodz.p.it.spjava.benefit.web.utils.ContextUtils;

@Named(value = "userPasswordChangePageBean")
@RequestScoped
public class ChangeUserPasswordPageBean {

    @Inject
    private AccountControllerBean accountControllerBean;

    private AccountDTO accountDTO;

    private String repeatNewPassword;

    public ChangeUserPasswordPageBean() {
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
        accountDTO = accountControllerBean.getSelectedAccountDTO();
    }

    public String saveChangedPasswordAction() throws AppBaseException {
        if (repeatNewPassword.equals(accountDTO.getPassword())) {
            accountControllerBean.changeUserAccountPassword(accountDTO);
        } else {
            ContextUtils.emitI18NMessage("ChangeUserPasswordForm:repeatNewPassword", "passwords.not.matching");
            return null;
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true); 
        return "listAuthorizedAccounts";
    }

}
