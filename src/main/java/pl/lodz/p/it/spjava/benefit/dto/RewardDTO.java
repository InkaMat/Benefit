package pl.lodz.p.it.spjava.benefit.dto;

import java.math.BigDecimal;
import pl.lodz.p.it.spjava.benefit.web.utils.ContextUtils;

public class RewardDTO implements Comparable<RewardDTO>{

    private String rewardName;
    private String activityType;
    private String frequencyOfUse;
    private BigDecimal price;
    private String priceFromForm;
    private boolean active;

    public RewardDTO() {
    }

    public RewardDTO(String rewardName, String activityType, String frequencyOfUse, BigDecimal price) {
        this.rewardName = rewardName;
        this.activityType = activityType;
        this.frequencyOfUse = frequencyOfUse;
        this.price = price;
    }

    public RewardDTO(String rewardName, String activityType, String frequencyOfUse, BigDecimal price, boolean active) {
        this.rewardName = rewardName;
        this.activityType = activityType;
        this.frequencyOfUse = frequencyOfUse;
        this.price = price;
        this.active = active;
    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public String getPriceFromForm() {
        return priceFromForm;
    }

    public void setPriceFromForm(String priceFromForm) {
        this.priceFromForm = priceFromForm;
    }

    // metoda przekształcająca BigDecimala w Stringa (w celu wyświetlenia danych z bazy w formularzu edycji produktu na stronie)
    public String setPriceFromForm(BigDecimal price) {
        priceFromForm = price.toString();
        return priceFromForm;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

        // metoda przekształca String w BigDecimal (walidacja formatu ceny wprowadzanej w formularzu na stronie)
    public BigDecimal getPrice(String priceFromForm) {
        price = new BigDecimal(priceFromForm);
        return price;
    }
    
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getFrequencyOfUse() {
        return frequencyOfUse;
    }

    public void setFrequencyOfUse(String frequencyOfUse) {
        this.frequencyOfUse = frequencyOfUse;
    }

    @Override
    public String toString() {
        return rewardName +",  " + activityType +",  " + frequencyOfUse +",  " + price + ContextUtils.printI18NMessage("page.reward.order.form.label.pln");
    }

    @Override
    public int compareTo(RewardDTO o) {
        return this.rewardName.compareTo(o.rewardName);
    }

    
    
}
