package pl.lodz.p.it.spjava.benefit.web.reward;

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
import pl.lodz.p.it.spjava.benefit.ejb.endpoint.RewardEndpoint;
import pl.lodz.p.it.spjava.benefit.dto.RewardDTO;
import pl.lodz.p.it.spjava.benefit.exception.AppBaseException;
import pl.lodz.p.it.spjava.benefit.web.utils.ContextUtils;

@Named(value = "listRewardsPageBean")
@ViewScoped
public class RewardsListPageBean implements Serializable {

    @EJB
    private RewardEndpoint rewardsEndpoint;

    @Inject
    private RewardControllerBean rewardControllerBean;

    private List<RewardDTO> listRewardDTO;

    private DataModel<RewardDTO> dataModelRewards;

    public RewardsListPageBean() {
    }

    public DataModel<RewardDTO> getDataModelRewards() {
        return dataModelRewards;
    }

    public void setDataModelRewards(DataModel<RewardDTO> dataModelRewards) {
        this.dataModelRewards = dataModelRewards;
    }

    public RewardControllerBean getRewardControllerBean() {
        return rewardControllerBean;
    }

    public void setRewardControllerBean(RewardControllerBean rewardControllerBean) {
        this.rewardControllerBean = rewardControllerBean;
    }

    @PostConstruct
    public void listAllRewards() {
        try {

            listRewardDTO = rewardControllerBean.listAllRewards();
        } catch (AppBaseException ex) {
            Logger.getLogger(RewardsListPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        dataModelRewards = new ListDataModel<>(listRewardDTO);
    }

    public String editRewardAction(RewardDTO rewardDTO) {
        try {
            rewardControllerBean.setSelectedRewardsListDTO(null); 
            rewardControllerBean.selectRewardForChange(rewardDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(RewardsListPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
            return null;
        }
        return "rewardEdit";
    }

    public String activateRewardAction(RewardDTO rewardDTO) {
        try {
            rewardControllerBean.activateReward(rewardDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(RewardsListPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        listAllRewards();
        return null;
    }

    public String deactivateRewardAction(RewardDTO rewardDTO) {
        try {
            rewardControllerBean.deactivateReward(rewardDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(RewardsListPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        }
        listAllRewards();
        return null;
    }

    public String deleteSelectedRewardAction(RewardDTO rewardDTO) {
        try {
            rewardControllerBean.deleteReward(rewardDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(RewardsListPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
        ContextUtils.getContext().getFlash().setKeepMessages(true);
        }
        listAllRewards();
        return null;
    }

}
