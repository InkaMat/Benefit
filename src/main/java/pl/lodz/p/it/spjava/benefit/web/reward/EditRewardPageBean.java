package pl.lodz.p.it.spjava.benefit.web.reward;

import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.benefit.dto.RewardDTO;
import pl.lodz.p.it.spjava.benefit.ejb.endpoint.RewardEndpoint;
import pl.lodz.p.it.spjava.benefit.exception.AppBaseException;
import pl.lodz.p.it.spjava.benefit.web.utils.ContextUtils;

@Named(value = "rewardEditPageBean")
@RequestScoped
public class EditRewardPageBean {

    @EJB
    private RewardEndpoint rewardEndpoint;

    @Inject
    private RewardControllerBean rewardControllerBean;
    
    private RewardDTO rewardDTO;
    private String rewardName;
    private String type; 
    private String frequencyOfUse; 
    private int price;
    
    public EditRewardPageBean() {
    }

    public RewardDTO getRewardDTO() {
        return rewardDTO;
    }

    public void setRewardDTO(RewardDTO rewardDTO) {
        this.rewardDTO = rewardDTO;
    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrequencyOfUse() {
        return frequencyOfUse;
    }

    public void setFrequencyOfUse(String frequencyOfUse) {
        this.frequencyOfUse = frequencyOfUse;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @PostConstruct
    public void init() {
        rewardDTO = rewardControllerBean.getSelectedRewardDTO();
    }

    public String saveEditRewardAction() {
                if (rewardDTO.getPrice(rewardDTO.getPriceFromForm()).compareTo(BigDecimal.ZERO) > 0) {
        try {
            rewardControllerBean.editReward(rewardDTO);
        } catch (AppBaseException ex) {
            Logger.getLogger(EditRewardPageBean.class.getName()).log(Level.SEVERE, null, ex);
            ContextUtils.emitI18NMessage(null, ex.getMessage());
            return null;
        }
         } else {
            ContextUtils.emitI18NMessage("RewardEditForm:rewardPrice", "page.reward.validator.price.problem");
            return null;
        }
        ContextUtils.getContext().getFlash().setKeepMessages(true);                
        return "listRewards";
    }

}
