package pl.lodz.p.it.spjava.benefit.web.order;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import pl.lodz.p.it.spjava.benefit.dto.RewardDTO;
import pl.lodz.p.it.spjava.benefit.dto.OrderDTO;
import pl.lodz.p.it.spjava.benefit.ejb.endpoint.RewardEndpoint;
import pl.lodz.p.it.spjava.benefit.ejb.endpoint.OrderEndpoint;
import pl.lodz.p.it.spjava.benefit.exception.AppBaseException;
import pl.lodz.p.it.spjava.benefit.web.utils.ContextUtils;

@Named(value = "orderControllerBean")
@SessionScoped
public class OrderControllerBean implements Serializable {

    @EJB
    private OrderEndpoint orderEndpoint;

    @EJB
    private RewardEndpoint rewardEndpoint;

    private int lastActionMethod = 0;

    private List<OrderDTO> selectedOrdersListDTO;

    public OrderControllerBean() {
    }

    public void addNewRewardOrder(final OrderDTO orderDTO, final RewardDTO rewardDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = orderDTO.hashCode() + rewardDTO.hashCode() + 1;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = orderEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                orderEndpoint.addNewOrder(orderDTO, rewardDTO);
                endpointCallCounter--;
            } while (orderEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.repeatedTransactionRollbackException();
            }
            ContextUtils.emitI18NMessage("RegisterForm:operationSuccess", "error.success");
        } else {
            ContextUtils.emitI18NMessage("RegisterForm:repeatedAction", "error.repeated.action");
        }
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public void deleteOrder(final OrderDTO orderDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = orderDTO.hashCode() + 2;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = orderEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                orderEndpoint.deleteOrder(orderDTO); 
                endpointCallCounter--;
            } while (orderEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.repeatedTransactionRollbackException();
            }
            ContextUtils.emitI18NMessage("listAllOrders:delete", "error.success");
        } else {
            ContextUtils.emitI18NMessage("RegisterForm:repeatedAction", "error.repeated.action");
        }
         ContextUtils.getContext().getFlash().setKeepMessages(true);
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public void deleteMyOrder(final OrderDTO orderDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = orderDTO.hashCode() + 3;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = orderEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                orderEndpoint.deleteMyOrder(orderDTO); 
                endpointCallCounter--;
            } while (orderEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.repeatedTransactionRollbackException();
            }
            ContextUtils.emitI18NMessage(null, "error.success");
        } else {
            ContextUtils.emitI18NMessage(null, "error.repeated.action");
        }
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public List<OrderDTO> listOrders() throws AppBaseException {
        int endpointCallCounter = rewardEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
        do {
            selectedOrdersListDTO = orderEndpoint.listOrders(); 
            endpointCallCounter--;
        } while (rewardEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
        if (endpointCallCounter == 0) {
            throw AppBaseException.repeatedTransactionRollbackException();
        }
        return selectedOrdersListDTO;
    }
    public List<OrderDTO> listMyOrders() throws AppBaseException {
        int endpointCallCounter = rewardEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
        do {
            selectedOrdersListDTO = orderEndpoint.listMyOrders(); 
            endpointCallCounter--;
        } while (rewardEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
        if (endpointCallCounter == 0) {
            throw AppBaseException.repeatedTransactionRollbackException();
        }
        return selectedOrdersListDTO;
    }

}
