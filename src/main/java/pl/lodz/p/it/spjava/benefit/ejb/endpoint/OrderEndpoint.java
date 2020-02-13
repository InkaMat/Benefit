package pl.lodz.p.it.spjava.benefit.ejb.endpoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.SessionSynchronization;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.interceptor.Interceptors;
import pl.lodz.p.it.spjava.benefit.dto.RewardDTO;
import pl.lodz.p.it.spjava.benefit.dto.OrderDTO;
import pl.lodz.p.it.spjava.benefit.ejb.facade.AccountFacade;
import pl.lodz.p.it.spjava.benefit.ejb.facade.RewardFacade;
import pl.lodz.p.it.spjava.benefit.ejb.facade.EmployeeFacade;
import pl.lodz.p.it.spjava.benefit.ejb.facade.OrderFacade;
import pl.lodz.p.it.spjava.benefit.ejb.facade.ManagerFacade;
import pl.lodz.p.it.spjava.benefit.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.spjava.benefit.exception.AccountException;
import pl.lodz.p.it.spjava.benefit.exception.AppBaseException;
import pl.lodz.p.it.spjava.benefit.exception.RewardException;
import pl.lodz.p.it.spjava.benefit.model.Reward;
import pl.lodz.p.it.spjava.benefit.model.Employee;
import pl.lodz.p.it.spjava.benefit.model.Order;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors(LoggingInterceptor.class)
public class OrderEndpoint extends AbstractEndpoint implements SessionSynchronization {

    @EJB
    private OrderFacade orderFacade;

    @EJB
    private ManagerFacade managerFacade;

    @EJB
    private EmployeeFacade employeeFacade;

    @EJB
    private AccountFacade accountFacade;

    @EJB
    private RewardFacade rewardFacade;

    @Resource
    private SessionContext sessionContext;

    private Reward rewardState;

    private List<Order> savedOrderStateList;

    public Reward getRewardState() {
        return rewardState;
    }

    public void setRewardState(Reward rewardState) {
        this.rewardState = rewardState;
    }

    @RolesAllowed({"Employee"})
    private Employee loadCurrentEmployee() throws AppBaseException {
        String employeeLogin = sessionContext.getCallerPrincipal().getName();
        Employee employeeAccount = employeeFacade.findByLogin(employeeLogin);
        if (employeeAccount == null) {
            throw AppBaseException.notAuthorizedActionException();
        }
        if (!employeeAccount.isActive()) {
            throw AccountException.accountNotActiveException(employeeAccount);
        }
        return employeeAccount;
    }

    @RolesAllowed({"Employee"})
    public void addNewOrder(OrderDTO orderDTO, RewardDTO rewardDTO) throws AppBaseException {
        Reward reward = rewardFacade.findByRewardName(rewardDTO.getRewardName());

        if (!reward.isActive()) {
            throw RewardException.rewardUnavailable(reward);
        }

        Order order = new Order();

        order.setOrderMonth(orderDTO.getOrderMonth());
        order.setOrderedReward(reward);
        order.setOrderedBy(loadCurrentEmployee());

        orderFacade.create(order);
    }

    @RolesAllowed({"Manager"})
    public List<OrderDTO> listOrders() throws AppBaseException { 
        List<Order> listAllOrders = orderFacade.findAll();
        savedOrderStateList = listAllOrders;
        List<OrderDTO> listAllOrdersDTO = new ArrayList<>();
        for (Order order : listAllOrders) {
            OrderDTO orderDTO = new OrderDTO(
                    order.getOrderMonth(),
                    order.getOrderedReward(), 
                    order.getOrderedBy() 
            );
            listAllOrdersDTO.add(orderDTO);
        }
        Collections.sort(listAllOrdersDTO);
        return listAllOrdersDTO;
    }

    @RolesAllowed({"Employee"})
    public List<OrderDTO> listMyOrders() throws AppBaseException { // do listRewards (2)
        List<Order> listMyOrders = orderFacade.findMyOrders(loadCurrentEmployee());
        savedOrderStateList = listMyOrders;
        List<OrderDTO> listMyOrdersDTO = new ArrayList<>();
        for (Order order : listMyOrders) {
            OrderDTO orderDTO = new OrderDTO(
                    order.getOrderMonth(),
                    order.getOrderedReward(),
                    order.getOrderedBy()
            );
            listMyOrdersDTO.add(orderDTO);
        }
        Collections.sort(listMyOrdersDTO);
        return listMyOrdersDTO;
    }

    @RolesAllowed({"Manager"})
    public void deleteOrder(OrderDTO orderDTO) throws AppBaseException { 

        Order order = orderFacade.findByRewardAndDate(orderDTO.getOrderedReward(), orderDTO.getOrderMonth());
        orderFacade.remove(order);
    }

    @RolesAllowed({"Employee"})
    public void deleteMyOrder(OrderDTO orderDTO) throws AppBaseException {
        Order order = orderFacade.findByRewardAndDate(orderDTO.getOrderedReward(), orderDTO.getOrderMonth());
        orderFacade.remove(order);
    }


}
