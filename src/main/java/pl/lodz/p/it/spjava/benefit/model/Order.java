package pl.lodz.p.it.spjava.benefit.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "ORDERS", uniqueConstraints = {
    @UniqueConstraint(name = "UNIQUE_EMPL_MONTH", columnNames = {"ORDERED_BY", "ORDER_MONTH"})})
@TableGenerator(name = "OrderGenerator", table = "TableGenerator", pkColumnName = "ID", valueColumnName = "value", pkColumnValue = "OrderGen")
@NamedQueries({
    @NamedQuery(name = "Order.findAll", query = "SELECT o FROM Order o")
    , @NamedQuery(name = "Order.findById", query = "SELECT o FROM Order o WHERE o.id = :id")
    , @NamedQuery(name = "Order.findByRewardAndDate", query = "SELECT o FROM Order o WHERE o.orderedReward = :orderedReward AND o.orderMonth = :orderMonth")
    , @NamedQuery(name = "Order.findMyOrders", query = "SELECT o FROM Order o WHERE o.orderedBy = :orderedBy")
})
public class Order extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "OrderGenerator")
    @Column(name = "ID", updatable = false)
    private Long id;

    @Basic(optional = false)
    @NotNull
    @Column(name = "ORDER_MONTH", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date orderMonth;

    @JoinColumn(name = "ORDERED_BY", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Employee orderedBy;

    @JoinColumn(name = "ORDERED_REWARD",  referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Reward orderedReward;

    public Order() {
    }

    public Order(Long id) {
        this.id = id;
    }

    public Order(Long id, Date orderMonth, Employee orderedBy, Reward orderedReward) {
        this.id = id;
        this.orderMonth = orderMonth;
        this.orderedBy = orderedBy;
        this.orderedReward = orderedReward;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getOrderMonth() {
        return orderMonth;
    }

    public void setOrderMonth(Date orderMonth) {
        this.orderMonth = orderMonth;
    }

    public Employee getOrderedBy() {
        return orderedBy;
    }

    public void setOrderedBy(Employee orderedBy) {
        this.orderedBy = orderedBy;
    }

    public Reward getOrderedReward() {
        return orderedReward;
    }

    public void setOrderedReward(Reward orderedReward) {
        this.orderedReward = orderedReward;
    }

    @Override
    public String toString() {
        return "Order{" + "id=" + id + ", orderMonth=" + orderMonth + ", orderedBy=" + orderedBy + ", orderedReward=" + orderedReward + '}';
    }

}
