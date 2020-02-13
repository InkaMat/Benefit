package pl.lodz.p.it.spjava.benefit.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import pl.lodz.p.it.spjava.benefit.model.Employee;
import pl.lodz.p.it.spjava.benefit.model.Reward;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-12-09T00:01:47")
@StaticMetamodel(Order.class)
public class Order_ extends AbstractEntity_ {

    public static volatile SingularAttribute<Order, Reward> orderedReward;
    public static volatile SingularAttribute<Order, Date> orderMonth;
    public static volatile SingularAttribute<Order, Employee> orderedBy;
    public static volatile SingularAttribute<Order, Long> id;

}