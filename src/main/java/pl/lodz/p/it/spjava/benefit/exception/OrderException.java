package pl.lodz.p.it.spjava.benefit.exception;

import javax.persistence.NoResultException;
import pl.lodz.p.it.spjava.benefit.model.Order;

public class OrderException extends AppBaseException {

    public static final String KEY_EMPLOYEE_ORDERED_FOR_THIS_DATE_CONFLICT = "error.employee.and.date.exist.problem";
    public static final String KEY_NO_ORDER_FOUND = "error.no.order.found.problem";

    private Order order;

    public Order getOrder() {
        return order;
    }

    private OrderException(String message, Order order) {
        super(message);
        this.order = order;
    }

    private OrderException(String message, Throwable cause, Order order) {
        super(message, cause);
        this.order = order;
    }

    private OrderException(String message) {
        super(message);
    }

    static public OrderException employeeOrderedForThisDateException(Throwable cause, Order order) {
        return new OrderException(KEY_EMPLOYEE_ORDERED_FOR_THIS_DATE_CONFLICT, cause, order);
    }

    public static AppBaseException noOrderFoundException(NoResultException nre) {
        return new AppBaseException(KEY_NO_ORDER_FOUND, nre);
    }

}
