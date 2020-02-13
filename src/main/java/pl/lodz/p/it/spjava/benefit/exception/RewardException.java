package pl.lodz.p.it.spjava.benefit.exception;

import javax.persistence.NoResultException;
import pl.lodz.p.it.spjava.benefit.model.Reward;

public class RewardException extends AppBaseException {

    public static final String KEY_REWARD_WITH_THIS_NAME_EXIST = "error.reward.with.this.name.exist.problem";
    public static final String KEY_REWARD_TYPE_AND_FREQUENCY_EXIST = "error.reward.type.and.frequency.exist.problem";
    public static final String KEY_REWARD_WRONG_STATE = "error.reward.wrong.state.problem";
    public static final String KEY_REWARD_NOT_EXIST = "error.reward.not.exist.problem";
    public static final String KEY_ORDERS_ORDERED_REWARD = "error.reward.is.in.order.problem";
    public static final String KEY_REWARD_ALREADY_ACTIVATED = "error.reward.already.activated.problem";
    public static final String KEY_REWARD_ALREADY_DEACTIVATED = "error.reward.already.deactivated.problem";
    public static final String KEY_REWARD_DETAILS_ALREADY_EDITED = "error.reward.already.edited.problem";
    public static final String KEY_REWARD_UNAVAILABLE = "error.reward.unavailable.problem";

    private Reward reward;

    public Reward getReward() {
        return reward;
    }

    private RewardException(String message, Reward reward) {
        super(message);
        this.reward = reward;
    }

    private RewardException(String message, Throwable cause, Reward reward) {
        super(message, cause);
        this.reward = reward;
    }

    private RewardException(String message) {
        super(message);
    }

    static public RewardException rewardNameAlreadyExistsException(Throwable cause, Reward reward) {
        return new RewardException(KEY_REWARD_WITH_THIS_NAME_EXIST, cause, reward );
    }
    static public RewardException rewardTypeAndFrequencyAlreadyExistsException(Throwable cause, Reward reward) {
        return new RewardException(KEY_REWARD_TYPE_AND_FREQUENCY_EXIST, cause, reward);
    }

    static public RewardException wrongStateException(Reward reward) {
        return new RewardException(KEY_REWARD_WRONG_STATE, reward);
    }

    static public RewardException rewardInOrderException(Throwable cause, Reward reward) {
        return new RewardException(KEY_ORDERS_ORDERED_REWARD, cause, reward);
    }

    static public RewardException rewardAlreadyActvivatedException(Reward reward) {
        return new RewardException(KEY_REWARD_ALREADY_ACTIVATED, reward);
    }

    static public RewardException rewardAlreadyDeactvivatedException(Reward reward) {
        return new RewardException(KEY_REWARD_ALREADY_DEACTIVATED, reward);
    }
    
    static public RewardException rewardDetailsEdited(Reward reward) {
        return new RewardException(KEY_REWARD_DETAILS_ALREADY_EDITED, reward);
    }
    
    static public RewardException rewardUnavailable(Reward reward) {
        return new RewardException(KEY_REWARD_UNAVAILABLE, reward);
    }

    public static AppBaseException createExceptionRewardNotExist(NoResultException nre) {
        return new AppBaseException(KEY_REWARD_NOT_EXIST, nre);
    }

}
