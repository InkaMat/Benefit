package pl.lodz.p.it.spjava.benefit.model;

import java.io.Serializable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(AccessLevel.AccessLevelKeys.NEWACCOUNT_KEY)
public class NewRegisteredAccount extends Account implements Serializable {

    public NewRegisteredAccount() {
    }
}
