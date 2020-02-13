package pl.lodz.p.it.spjava.benefit.dto;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import org.apache.commons.lang3.time.DateUtils;
import pl.lodz.p.it.spjava.benefit.model.Reward;
import pl.lodz.p.it.spjava.benefit.model.Employee;

public class OrderDTO implements Comparable<OrderDTO> {

    private Date orderMonth;
    private Date minDate;
    private String orderDateFromFormToString;
    private Reward orderedReward;
    private Employee orderedBy;

    public OrderDTO() {
    }

    public OrderDTO(Date orderMonth) {
        this.orderMonth = orderMonth;
    }

    public OrderDTO(Date orderMonth, Reward orderedReward, Employee orderedBy) {
        this.orderMonth = orderMonth;
        this.orderedReward = orderedReward;
        this.orderedBy = orderedBy;
    }

    public Date getOrderMinDate() {
        return new Date();
    }

    public String getOrderDateFromFormToString() {

        Format formatter = new SimpleDateFormat("yyyy-MM");
        orderDateFromFormToString = formatter.format(orderMonth);
        return orderDateFromFormToString;
    }

    public void setOrderDateFromFormToString(String orderDateFromFormToString) {
        this.orderDateFromFormToString = orderDateFromFormToString;
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

    public Date getNextMonth() {
        Date nextMonth = DateUtils.addMonths(new Date(), 1);
        return nextMonth;
    }

    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 13 * hash + Objects.hashCode(this.orderMonth);
        hash = 13 * hash + Objects.hashCode(this.orderedReward);
        hash = 13 * hash + Objects.hashCode(this.orderedBy);
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
        final OrderDTO other = (OrderDTO) obj;
        if (!Objects.equals(this.orderMonth, other.orderMonth)) {
            return false;
        }
        if (!Objects.equals(this.orderedReward, other.orderedReward)) {
            return false;
        }
        if (!Objects.equals(this.orderedBy, other.orderedBy)) {
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(OrderDTO orderDTO) {
        return this.orderMonth.compareTo(orderDTO.orderMonth);
    }

}
