package pl.lodz.p.it.spjava.benefit.dto;

import java.util.Date;
import java.util.Objects;
import pl.lodz.p.it.spjava.benefit.model.AccessLevel;
import pl.lodz.p.it.spjava.benefit.model.Administrator;

public class AccountDTO implements Comparable<AccountDTO> {

    private String login;
    private String password;
    private String question;
    private String answer;
    private String name;
    private String surname;
    private String phone;
    private boolean authorized;
    private boolean active;
    private AccessLevel accessLevel;
    private Administrator authorizedBy;
    private Administrator modificatedBy;
    private Date creationDate;
    private Date modificationDate;
    private String oldPassword;

    public AccountDTO() {
    }

    // NewAccountsList 4 parameters
    public AccountDTO(String login, String name, String surname, String phone) {
        this.login = login;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
    }

    // listAuthorizedAccounts 6 parameters
    public AccountDTO(String login, String name, String surname, String phone, boolean active, Date creationDate) {
        this.login = login;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.active = active;
        this.creationDate = creationDate;
    }

    // rememberSelectedAccountForEdit 6 parameters
    public AccountDTO(String login, String question, String answer, String name, String surname, String phone) {
        this.login = login;
        this.question = question;
        this.answer = answer;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
    }

    //5 parameters rememberMyAccountForDisplayAndEdit
    public AccountDTO(String login, String password, String name, String surname, String phone) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
    }
    
    public AccountDTO(String login, String name, String surname, String phone, Date creationDate) {
        this.login = login;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.creationDate = creationDate;
    }
    
    // 1parameter    rememberSelectedAccountForPasswordChange
    public AccountDTO(String login) {
        this.login = login;
    }

    //2 parameters rememberMyAccountForPasswordChange
    public AccountDTO(String login, String password) {
        this.login = login;
        this.password = password;
    }

    // 3 parameters rememberSelectedAccountForPasswordReset
    public AccountDTO(String login, String question, String answer) {
        this.login = login;
        this.question = question;
        this.answer = answer;
    }

    // 4 parameters rememberSelectedAccountForPasswordResetANDQuestionCheck
    public AccountDTO(String login, String question, String answer, boolean active) {
        this.login = login;
        this.question = question;
        this.answer = answer;
        this.active = active;
    }

    // registerAccount - 8parameters 
    public AccountDTO(String login, String password, String question, String answer, String name, String surname, String phone, boolean active) {
        this.login = login;
        this.password = password;
        this.question = question;
        this.answer = answer;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.active = active;
    }

    // 6parameters  do wyswietlania danych konta
    public AccountDTO(String login, String password, String name, String surname, String phone, boolean active) {
        this.login = login;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.active = active;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
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

    public boolean isAuthorized() {
        return authorized;
    }

    public void isAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

    public void setAuthorized(boolean authorized) {
        this.authorized = authorized;
    }

    public boolean isActive() {
        return active;
    }

    public void isActive(boolean active) {
        this.active = active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public AccessLevel getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(AccessLevel accessLevel) {
        this.accessLevel = accessLevel;
    }

    public Administrator getAuthorizedBy() {
        return authorizedBy;
    }

    public void setAuthorizedBy(Administrator authorizedBy) {
        this.authorizedBy = authorizedBy;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Administrator getModificatedBy() {
        return modificatedBy;
    }

    public void setModificatedBy(Administrator modificatedBy) {
        this.modificatedBy = modificatedBy;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    public void setModificationDate(Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    @Override
    public String toString() {
        return "AccountDTO:" + " login= " + login;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.login);
        hash = 11 * hash + Objects.hashCode(this.password);
        hash = 11 * hash + Objects.hashCode(this.question);
        hash = 11 * hash + Objects.hashCode(this.answer);
        hash = 11 * hash + Objects.hashCode(this.name);
        hash = 11 * hash + Objects.hashCode(this.surname);
        hash = 11 * hash + Objects.hashCode(this.phone);
        hash = 11 * hash + (this.authorized ? 1 : 0);
        hash = 11 * hash + (this.active ? 1 : 0);
        hash = 11 * hash + Objects.hashCode(this.accessLevel);
        hash = 11 * hash + Objects.hashCode(this.authorizedBy);
        hash = 11 * hash + Objects.hashCode(this.modificatedBy);
        hash = 11 * hash + Objects.hashCode(this.creationDate);
        hash = 11 * hash + Objects.hashCode(this.modificationDate);
        hash = 11 * hash + Objects.hashCode(this.oldPassword);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AccountDTO other = (AccountDTO) obj;
        if (this.authorized != other.authorized) {
            return false;
        }
        if (this.active != other.active) {
            return false;
        }
        if (!Objects.equals(this.login, other.login)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        if (!Objects.equals(this.question, other.question)) {
            return false;
        }
        if (!Objects.equals(this.answer, other.answer)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.surname, other.surname)) {
            return false;
        }
        if (!Objects.equals(this.phone, other.phone)) {
            return false;
        }
        if (!Objects.equals(this.oldPassword, other.oldPassword)) {
            return false;
        }
        if (this.accessLevel != other.accessLevel) {
            return false;
        }
        if (!Objects.equals(this.authorizedBy, other.authorizedBy)) {
            return false;
        }
        if (!Objects.equals(this.modificatedBy, other.modificatedBy)) {
            return false;
        }
        if (!Objects.equals(this.creationDate, other.creationDate)) {
            return false;
        }
        if (!Objects.equals(this.modificationDate, other.modificationDate)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(AccountDTO o) {
        return this.login.compareTo(o.login);
    }

}
