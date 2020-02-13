package pl.lodz.p.it.spjava.benefit.model;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@DiscriminatorValue(AccessLevel.AccessLevelKeys.ADMINISTRATOR_KEY)
@NamedQueries({
    @NamedQuery(name = "Administrator.findAll", query = "SELECT a FROM Administrator a"),
    @NamedQuery(name = "Administrator.findByLogin", query = "SELECT a FROM Administrator a WHERE a.login = :login")
  })
public class Administrator extends Account implements Serializable {

    public Administrator() {
    }

        public Administrator(Account account) {
        super(account.getId(), account.getLogin(), account.getPassword(), account.getQuestion(), account.getAnswer(), account.getName(), account.getSurname(), account.getPhone(), account.isAuthorized(), account.isActive(), account.getAuthorizedBy(), account.getModificatedBy());
    }

}
