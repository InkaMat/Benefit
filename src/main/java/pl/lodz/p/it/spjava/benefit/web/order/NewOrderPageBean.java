package pl.lodz.p.it.spjava.benefit.web.order;

import pl.lodz.p.it.spjava.benefit.web.reward.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.benefit.dto.AccountDTO;
import pl.lodz.p.it.spjava.benefit.dto.RewardDTO;
import pl.lodz.p.it.spjava.benefit.dto.OrderDTO;
import pl.lodz.p.it.spjava.benefit.ejb.endpoint.RewardEndpoint;
import pl.lodz.p.it.spjava.benefit.exception.AppBaseException;
import pl.lodz.p.it.spjava.benefit.web.utils.ContextUtils;

@Named(value = "newOrderPageBean")
@ViewScoped
public class NewOrderPageBean implements Serializable {

    @EJB
    private RewardEndpoint rewardEndpoint;

    @Inject
    private OrderControllerBean orderControllerBean;

    private List<RewardDTO> listActiveRewards;
    private List<RewardDTO> listRewards;

    private AccountDTO accountDTO;

    private OrderDTO orderDTO;

    private RewardDTO rewardDTO;

    private Date minDate;

    private Date maxDate;

    public NewOrderPageBean() {

    }

    public List<RewardDTO> getListRewards() {
        return listRewards;
    }

    public void setListRewards(List<RewardDTO> listRewards) {
        this.listRewards = listRewards;
    }

    public List<RewardDTO> getListActiveRewards() {
        return listActiveRewards;
    }

    public void setListActiveRewards(List<RewardDTO> listActiveRewards) {
        this.listActiveRewards = listActiveRewards;
    }

    public AccountDTO getAccountDTO() {
        return accountDTO;
    }

    public void setAccountDTO(AccountDTO accountDTO) {
        this.accountDTO = accountDTO;
    }

    public OrderDTO getOrderDTO() {
        return orderDTO;
    }

    public void setOrderDTO(OrderDTO orderDTO) {
        this.orderDTO = orderDTO;
    }

    public RewardDTO getRewardDTO() {
        return rewardDTO;
    }

    public void setRewardDTO(RewardDTO rewardDTO) {
        this.rewardDTO = rewardDTO;
    }

    public OrderControllerBean getOrderControllerBean() {
        return orderControllerBean;
    }

    public void setOrderControllerBean(OrderControllerBean orderControllerBean) {
        this.orderControllerBean = orderControllerBean;
    }

    public Date getMinDate() {
        return minDate;
    }

    public void setMinDate(Date minDate) {
        this.minDate = minDate;
    }

    public Date getMaxDate() {
        return maxDate;
    }

    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }

    @PostConstruct
    public void initOrderDetails() {
        try {
            listRewards = rewardEndpoint.listActiveRewards();
            
                        Date today = new Date();
            long oneDay = 24 * 60 * 60 * 1000;
            minDate = new Date(today.getTime());
            maxDate = new Date(today.getTime() + (365 * oneDay));

        } catch (AppBaseException ex) {
            Logger.getLogger(AddNewRewardPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        orderDTO = new OrderDTO();
        rewardDTO = new RewardDTO();

    }

    public String saveRewardOrderAction() throws AppBaseException {
        if (rewardDTO.getRewardName() != null) {


            if (minDate.after(orderDTO.getOrderMonth())) {
                ContextUtils.emitI18NMessage("NewOrderForm:orderMonth", "error.new.order.min.date");
                return null;
            }

            if (maxDate.before(orderDTO.getOrderMonth())) {
                ContextUtils.emitI18NMessage("NewOrderForm:orderMonth", "error.new.order.max.date");
                return null;
            }

            try {
                orderControllerBean.addNewRewardOrder(orderDTO, rewardDTO);
            } catch (AppBaseException ex) {
                Logger.getLogger(AddNewRewardPageBean.class.getName()).log(Level.SEVERE, null, ex);
                ContextUtils.emitI18NMessage(null, ex.getMessage());
                ContextUtils.getContext().getFlash().setKeepMessages(true);
                return null;
            }
            ContextUtils.emitI18NMessage(null, "error.success");
            return "listMyOrders";
        }
        ContextUtils.emitI18NMessage(null, "error");
        ContextUtils.emitI18NMessage("NewOrderForm:chooseReward", "choosen.reward.is.empty");
        return null;
    }

}
