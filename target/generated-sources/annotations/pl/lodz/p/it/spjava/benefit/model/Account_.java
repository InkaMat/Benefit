package pl.lodz.p.it.spjava.benefit.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import pl.lodz.p.it.spjava.benefit.model.Administrator;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-12-09T00:01:47")
@StaticMetamodel(Account.class)
public class Account_ extends AbstractEntity_ {

    public static volatile SingularAttribute<Account, String> password;
    public static volatile SingularAttribute<Account, String> question;
    public static volatile SingularAttribute<Account, String> answer;
    public static volatile SingularAttribute<Account, Administrator> authorizedBy;
    public static volatile SingularAttribute<Account, Administrator> modificatedBy;
    public static volatile SingularAttribute<Account, String> phone;
    public static volatile SingularAttribute<Account, String> surname;
    public static volatile SingularAttribute<Account, Boolean> authorized;
    public static volatile SingularAttribute<Account, String> name;
    public static volatile SingularAttribute<Account, Boolean> active;
    public static volatile SingularAttribute<Account, Long> id;
    public static volatile SingularAttribute<Account, String> login;

}