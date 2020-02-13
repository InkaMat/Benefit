package pl.lodz.p.it.spjava.benefit.model;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@DiscriminatorValue(AccessLevel.AccessLevelKeys.EMPLOYEE_KEY)
@NamedQueries({
    @NamedQuery(name = "Employee.findAll", query = "SELECT e FROM Employee e"),
    @NamedQuery(name = "Employee.findByLogin", query = "SELECT e FROM Employee e WHERE e.login = :login")
})
public class Employee extends Account implements Serializable {

    public Employee() {
    }

    public Employee(Account account) {
          super(account.getId(), account.getLogin(), account.getPassword(), account.getQuestion(), account.getAnswer(), account.getName(), account.getSurname(), account.getPhone(), account.isAuthorized(), account.isActive(), account.getAuthorizedBy(), account.getModificatedBy());
    }
    
}
