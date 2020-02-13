package pl.lodz.p.it.spjava.benefit.web.reward;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import pl.lodz.p.it.spjava.benefit.dto.RewardDTO;
import pl.lodz.p.it.spjava.benefit.exception.AppBaseException;
import pl.lodz.p.it.spjava.benefit.web.utils.ContextUtils;

@Named(value = "addNewRewardPageBean")
@RequestScoped
public class AddNewRewardPageBean implements Serializable {

    @Inject
    private RewardControllerBean rewardControllerBean;

    private RewardDTO rewardDTO;

    public AddNewRewardPageBean() {
    }

    public RewardDTO getRewardDTO() {
        return rewardDTO;
    }

    public void setRewardDTO(RewardDTO rewardDTO) {
        this.rewardDTO = rewardDTO;
    }

    @PostConstruct
    public void init() {
        rewardDTO = new RewardDTO();
    }

    public String addNewRewardAction() {
        if (rewardDTO.getPrice(rewardDTO.getPriceFromForm()).compareTo(BigDecimal.ZERO) > 0) {
            try {
                rewardControllerBean.addNewReward(rewardDTO);
            } catch (AppBaseException ex) {
                Logger.getLogger(AddNewRewardPageBean.class.getName()).log(Level.SEVERE, null, ex);
                ContextUtils.emitI18NMessage(null, ex.getMessage());
                return null;
            }
        } else {
            ContextUtils.emitI18NMessage("addNewRewardForm:rewardPrice", "page.reward.validator.price.problem");
            return null;
        }
       return "listRewards";
    }

}
