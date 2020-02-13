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
import pl.lodz.p.it.spjava.benefit.ejb.facade.AccountFacade;
import pl.lodz.p.it.spjava.benefit.ejb.facade.RewardFacade;
import pl.lodz.p.it.spjava.benefit.ejb.facade.ManagerFacade;
import pl.lodz.p.it.spjava.benefit.ejb.facade.EmployeeFacade;
import pl.lodz.p.it.spjava.benefit.ejb.interceptor.LoggingInterceptor;
import pl.lodz.p.it.spjava.benefit.exception.AccountException;
import pl.lodz.p.it.spjava.benefit.exception.RewardException;
import pl.lodz.p.it.spjava.benefit.exception.AppBaseException;
import pl.lodz.p.it.spjava.benefit.model.Reward;
import pl.lodz.p.it.spjava.benefit.model.Manager;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors(LoggingInterceptor.class)
public class RewardEndpoint extends AbstractEndpoint implements SessionSynchronization {

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

    private List<Reward> savedRewardStateList;

    public List<Reward> getSavedRewardStateList() {
        return savedRewardStateList;
    }

    public void setSavedRewardStateList(List<Reward> savedRewardStateList) {
        this.savedRewardStateList = savedRewardStateList;
    }

    public Reward getRewardState() {
        return rewardState;
    }

    public void setRewardState(Reward rewardState) {
        this.rewardState = rewardState;
    }

    @RolesAllowed({"Manager"})
    private Manager loadCurrentManager() throws AppBaseException {
        String managerLogin = sessionContext.getCallerPrincipal().getName();
        Manager managerAccount = managerFacade.findByLogin(managerLogin);
        if (managerAccount == null) {
            throw AppBaseException.notAuthorizedActionException();
        }
        if (!managerAccount.isActive()) {
            throw AccountException.accountNotActiveException(managerAccount);
        }
        return managerAccount;
    }

    @RolesAllowed({"Manager"})
    public void addNewReward(RewardDTO rewardDTO) throws AppBaseException { // do register (1)
        Reward reward = new Reward();
        reward.setRewardName(rewardDTO.getRewardName());
        reward.setActivityType(rewardDTO.getActivityType());
        reward.setFrequencyOfUse(rewardDTO.getFrequencyOfUse());
        reward.setPrice(rewardDTO.getPrice(rewardDTO.getPriceFromForm()));

        reward.setActive(true);
        reward.setCreatedBy(loadCurrentManager());

        rewardFacade.create(reward);
    }

// ------------------ Rewards List
    @RolesAllowed({"Manager"})
    public List<RewardDTO> listAllRewards() throws AppBaseException { // do listRewards (2)
        List<Reward> listRewards = rewardFacade.findAll();
        List<RewardDTO> listAllRewards = new ArrayList<>();
        for (Reward reward : listRewards) {
            RewardDTO rewardDTO = new RewardDTO(
                    reward.getRewardName(),
                    reward.getActivityType(),
                    reward.getFrequencyOfUse(),
                    reward.getPrice(),
                    reward.isActive()
            );
            listAllRewards.add(rewardDTO);
        }
        Collections.sort(listAllRewards);
        return listAllRewards;
    }

    public RewardDTO rememberSelectedRewardInState(String rewardName) throws AppBaseException {
        rewardState = rewardFacade.findByRewardName(rewardName);

        RewardDTO rewardDTO = new RewardDTO(
                rewardState.getRewardName(),
                rewardState.getActivityType(),
                rewardState.getFrequencyOfUse(),
                rewardState.getPrice());

        rewardDTO.setPriceFromForm(rewardState.getPrice());
        return rewardDTO;
    }

    @RolesAllowed({"Manager"})
    public void editReward(RewardDTO rewardDTO) throws AppBaseException {
        if (rewardState.getRewardName().equals(rewardDTO.getRewardName())) {
            rewardState.setRewardName(rewardDTO.getRewardName());
            rewardState.setFrequencyOfUse(rewardDTO.getFrequencyOfUse());
            rewardState.setActivityType(rewardDTO.getActivityType());
            rewardState.setPrice(rewardDTO.getPrice(rewardDTO.getPriceFromForm()));
            rewardState.setModificatedBy(loadCurrentManager());

            rewardFacade.edit(rewardState);
        } else {
            throw RewardException.wrongStateException(rewardState);
        }
    }

    @RolesAllowed({"Manager"})
    public void activateReward(String rewardName) throws AppBaseException {// do listRewards (2)
        rewardState = rewardFacade.findByRewardName(rewardName);
        if (!rewardState.isActive()) {
            rewardState.setActive(true);
            rewardState.setModificatedBy(loadCurrentManager());
            rewardFacade.edit(rewardState);
        } else {
            throw RewardException.rewardAlreadyActvivatedException(rewardState);
        }
    }

    @RolesAllowed({"Manager"})
    public void deactivateReward(String rewardName) throws AppBaseException {// do listRewards (2)
        rewardState = rewardFacade.findByRewardName(rewardName);
        if (rewardState.isActive()) {
            rewardState.setActive(false);
            rewardState.setModificatedBy(loadCurrentManager());
            rewardFacade.edit(rewardState);
        } else {
            throw RewardException.rewardAlreadyDeactvivatedException(rewardState);
        }
    }

    @RolesAllowed({"Manager"})
    public void deleteReward(RewardDTO rewardDTO) throws AppBaseException { // do listRewards (2)
        Reward reward = rewardFacade.findByRewardName(rewardDTO.getRewardName());
        rewardFacade.remove(reward);
    }

    //------------------ Rewards List
    @RolesAllowed({"Employee"})
    public List<RewardDTO> listActiveRewards() throws AppBaseException { // do listRewards (2)
        List<Reward> listRewards = rewardFacade.findActiveReward();
        List<RewardDTO> listActiveRewards = new ArrayList<>();
        for (Reward reward : listRewards) {
            RewardDTO rewardDTO = new RewardDTO(
                    reward.getRewardName(),
                    reward.getFrequencyOfUse(),
                    reward.getActivityType(),
                    reward.getPrice(),
                    reward.isActive()
            );
            listActiveRewards.add(rewardDTO);
        }
        Collections.sort(listActiveRewards);
        return listActiveRewards;
    }

}
