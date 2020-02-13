package pl.lodz.p.it.spjava.benefit.model;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import pl.lodz.p.it.spjava.benefit.model.Manager;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-12-09T00:01:47")
@StaticMetamodel(Reward.class)
public class Reward_ extends AbstractEntity_ {

    public static volatile SingularAttribute<Reward, String> rewardName;
    public static volatile SingularAttribute<Reward, Manager> modificatedBy;
    public static volatile SingularAttribute<Reward, Manager> createdBy;
    public static volatile SingularAttribute<Reward, BigDecimal> price;
    public static volatile SingularAttribute<Reward, Boolean> active;
    public static volatile SingularAttribute<Reward, String> frequencyOfUse;
    public static volatile SingularAttribute<Reward, Long> id;
    public static volatile SingularAttribute<Reward, String> activityType;

}