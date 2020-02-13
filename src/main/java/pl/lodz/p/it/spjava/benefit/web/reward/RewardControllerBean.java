package pl.lodz.p.it.spjava.benefit.web.reward;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import pl.lodz.p.it.spjava.benefit.dto.RewardDTO;
import pl.lodz.p.it.spjava.benefit.ejb.endpoint.RewardEndpoint;
import pl.lodz.p.it.spjava.benefit.exception.AppBaseException;
import pl.lodz.p.it.spjava.benefit.web.utils.ContextUtils;

@Named(value = "rewardControllerBean")
@SessionScoped
public class RewardControllerBean implements Serializable {

    @EJB
    private RewardEndpoint rewardEndpoint;

    private int lastActionMethod = 0;

    private RewardDTO selectedRewardDTO;

    private List<RewardDTO> selectedRewardsListDTO;

    private DataModel<RewardDTO> dataModelRewards;

    private ListDataModel<RewardDTO> listDataModelRewards;

    private boolean refreshAllLists;

    public RewardControllerBean() {
    }

    public RewardDTO getSelectedRewardDTO() {
        return selectedRewardDTO;
    }

    public void setSelectedRewardDTO(RewardDTO selectedRewardDTO) {
        this.selectedRewardDTO = selectedRewardDTO;
    }

    public List<RewardDTO> getSelectedRewardsListDTO() {
        return selectedRewardsListDTO;
    }

    public void setSelectedRewardsListDTO(List<RewardDTO> selectedRewardsListDTO) {
        this.selectedRewardsListDTO = selectedRewardsListDTO;
    }

    public DataModel<RewardDTO> getDataModelRewards() {
        return dataModelRewards;
    }

    public void setDataModelRewards(DataModel<RewardDTO> dataModelRewards) {
        this.dataModelRewards = dataModelRewards;
    }

    public ListDataModel<RewardDTO> getListDataModelRewards() {
        return listDataModelRewards;
    }

    public void setListDataModelRewards(ListDataModel<RewardDTO> listDataModelRewards) {
        this.listDataModelRewards = listDataModelRewards;
    }

    public boolean isRefreshAllLists() {
        return refreshAllLists;
    }

    public void setRefreshAllLists(boolean refreshAllLists) {
        this.refreshAllLists = refreshAllLists;
    }

    public void addNewReward(final RewardDTO rewardDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = rewardDTO.hashCode() + 1;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = rewardEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                rewardEndpoint.addNewReward(rewardDTO); 
                endpointCallCounter--;
            } while (rewardEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.repeatedTransactionRollbackException();
            }
            ContextUtils.emitI18NMessage("RegisterForm:operationSuccess", "error.success");
        } else {
            ContextUtils.emitI18NMessage("RegisterForm:repeatedAction", "error.repeated.action");
        }
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public List<RewardDTO> listAllRewards() throws AppBaseException {
        int endpointCallCounter = rewardEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
        do {
            selectedRewardsListDTO = rewardEndpoint.listAllRewards(); 
            endpointCallCounter--;
        } while (rewardEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
        if (endpointCallCounter == 0) {
            throw AppBaseException.repeatedTransactionRollbackException();
        }
        return selectedRewardsListDTO;
    }

    void selectRewardForChange(RewardDTO rewardDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = rewardDTO.hashCode() + 2;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = rewardEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                selectedRewardDTO = rewardEndpoint.rememberSelectedRewardInState(rewardDTO.getRewardName()); 
                endpointCallCounter--;
            } while (rewardEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.repeatedTransactionRollbackException();
            }
        } else {
            ContextUtils.emitI18NMessage("RegisterForm:repeatedAction", "error.repeated.action");
        }
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public void editReward(final RewardDTO rewardDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = rewardDTO.hashCode() + 3;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = rewardEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                rewardEndpoint.editReward(rewardDTO); 
                endpointCallCounter--;
            } while (rewardEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.repeatedTransactionRollbackException();
            }
            ContextUtils.emitI18NMessage(null, "error.success");
        } else {
            ContextUtils.emitI18NMessage("RegisterForm:repeatedAction", "error.repeated.action");
        }
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public void activateReward(final RewardDTO rewardDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = rewardDTO.hashCode() + 4;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = rewardEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                rewardEndpoint.activateReward(rewardDTO.getRewardName()); 
                endpointCallCounter--;
            } while (rewardEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.repeatedTransactionRollbackException();
            }
            ContextUtils.emitI18NMessage(null, "error.success");
        } else {
            ContextUtils.emitI18NMessage("RegisterForm:repeatedAction", "error.repeated.action");
        }
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public void deactivateReward(final RewardDTO rewardDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = rewardDTO.hashCode() + 5;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = rewardEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                rewardEndpoint.deactivateReward(rewardDTO.getRewardName()); 
                endpointCallCounter--;
            } while (rewardEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.repeatedTransactionRollbackException();
            }
            ContextUtils.emitI18NMessage(null, "error.success");
        } else {
            ContextUtils.emitI18NMessage("RegisterForm:repeatedAction", "error.repeated.action");
        }
        lastActionMethod = UNIQ_METHOD_ID;
    }

    public void deleteReward(final RewardDTO rewardDTO) throws AppBaseException {
        final int UNIQ_METHOD_ID = rewardDTO.hashCode() + 6;
        if (lastActionMethod != UNIQ_METHOD_ID) {
            int endpointCallCounter = rewardEndpoint.NB_ATEMPTS_FOR_METHOD_INVOCATION;
            do {
                rewardEndpoint.deleteReward(rewardDTO); 
                endpointCallCounter--;
            } while (rewardEndpoint.isLastTransactionRollback() && endpointCallCounter > 0);
            if (endpointCallCounter == 0) {
                throw AppBaseException.repeatedTransactionRollbackException();
            }
            ContextUtils.emitI18NMessage(null, "error.success");
        } else {
            ContextUtils.emitI18NMessage("RegisterForm:repeatedAction", "error.repeated.action");
        }
        lastActionMethod = UNIQ_METHOD_ID;
        ContextUtils.getContext().getFlash().setKeepMessages(true);
    }

}
