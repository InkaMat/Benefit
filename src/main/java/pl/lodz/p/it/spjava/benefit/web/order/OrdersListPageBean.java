package pl.lodz.p.it.spjava.benefit.web.order;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.benefit.ejb.endpoint.OrderEndpoint;
import pl.lodz.p.it.spjava.benefit.dto.OrderDTO;
import pl.lodz.p.it.spjava.benefit.exception.AppBaseException;
import pl.lodz.p.it.spjava.benefit.web.utils.ContextUtils;

@Named(value = "ordersListPageBean")
@ViewScoped
public class OrdersListPageBean implements Serializable{

    @EJB
    private OrderEndpoint orderEndpoint;

    @Inject
    private OrderControllerBean orderControllerBean;

    private List<OrderDTO> listOrders;

    private DataModel<OrderDTO> dataModelOrders;

    public OrdersListPageBean() {
    }

    public DataModel<OrderDTO> getDataModelOrders() {
        return dataModelOrders;
    }

    @PostConstruct
    public void initListNewOrders() {
        try {
            listOrders = orderEndpoint.listOrders();
        } catch (AppBaseException ex) {
            Logger.getLogger(OrdersListPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        dataModelOrders = new ListDataModel<>(listOrders);
    }

    public String deleteSelectedOrderAction(OrderDTO orderDTO) {
        try {
            orderControllerBean.deleteOrder(orderDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(OrdersListPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
             ContextUtils.getContext().getFlash().setKeepMessages(true);
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        initListNewOrders();
        return null;
    }

}
