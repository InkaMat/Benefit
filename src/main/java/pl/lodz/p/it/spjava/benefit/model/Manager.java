package pl.lodz.p.it.spjava.benefit.model;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@DiscriminatorValue(AccessLevel.AccessLevelKeys.MANAGER_KEY)
@NamedQueries({
    @NamedQuery(name = "Manager.findAll", query = "SELECT m FROM Manager m"),
    @NamedQuery(name = "Manager.findByLogin", query = "SELECT m FROM Manager m WHERE m.login = :login")
})
public class Manager extends Account implements Serializable {

    public Manager() {
    }

    public Manager(Account account) {
        super(account.getId(), account.getLogin(), account.getPassword(), account.getQuestion(), account.getAnswer(), account.getName(), account.getSurname(), account.getPhone(), account.isAuthorized(), account.isActive(), account.getAuthorizedBy(), account.getModificatedBy());
    }

}
