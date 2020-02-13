package pl.lodz.p.it.spjava.benefit.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "REWARDS", uniqueConstraints = {
     @UniqueConstraint(name = "UNIQUE_REW_NAME", columnNames ="REWARD_NAME")
   , @UniqueConstraint(name = "UNIQUE_R_TYPE_FREQ", columnNames = {"ACTIVITY_TYPE", "FREQUENCY_OF_USE"})
})
@TableGenerator(name = "RewardGenerator", table = "TableGenerator", pkColumnName = "ID", valueColumnName = "value", pkColumnValue = "RewardGen")

@NamedQueries({
      @NamedQuery(name = "Reward.findAll", query = "SELECT r FROM Reward r")
    , @NamedQuery(name = "Reward.findById", query = "SELECT r FROM Reward r WHERE r.id = :id")
    , @NamedQuery(name = "Reward.findByRewardName", query = "SELECT r FROM Reward r WHERE r.rewardName = :rewardName")
    , @NamedQuery(name = "Reward.findActive", query = "SELECT r FROM Reward r WHERE r.active = true")
})

public class Reward extends AbstractEntity implements Serializable {

    @GeneratedValue(strategy = GenerationType.TABLE, generator = "RewardGenerator")
    @Id
    @Column(name = "ID", updatable = false)
    private Long id;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "REWARD_NAME", nullable = false)
    private String rewardName;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "ACTIVITY_TYPE", nullable = false)
    private String activityType;

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "FREQUENCY_OF_USE", nullable = false)
    private String frequencyOfUse;

    @Digits(integer = 10, fraction = 2)
    @DecimalMin(value = "0.01")
    @NotNull
    @Column(name = "PRICE", precision = 8, scale = 2, nullable = false)
    private BigDecimal price;

    @NotNull
    @Column(name = "ACTIVE", nullable = false)
    private boolean active;

    @NotNull
    @JoinColumn(name = "CREATED_BY", referencedColumnName = "ID", nullable = false, updatable = false)
    @ManyToOne(optional = false)
    private Manager createdBy;

    @NotNull
    @JoinColumn(name = "MODIFICATED_BY", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Manager modificatedBy;

    public Reward() {
    }

    public Reward(Long id) {
        this.id = id;
    }

    public Reward(Long id, String rewardName, String activityType, String frequencyOfUse, BigDecimal price) {
        this.id = id;
        this.rewardName = rewardName;
        this.activityType = activityType;
        this.frequencyOfUse = frequencyOfUse;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public Manager getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Manager createdBy) {
        this.createdBy = createdBy;
    }

    public Manager getModificatedBy() {
        return modificatedBy;
    }

    public void setModificatedBy(Manager modificatedBy) {
        this.modificatedBy = modificatedBy;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id);
        hash = 79 * hash + Objects.hashCode(this.rewardName);
        hash = 79 * hash + Objects.hashCode(this.activityType);
        hash = 79 * hash + Objects.hashCode(this.frequencyOfUse);
        hash = 79 * hash + Objects.hashCode(this.price);
        hash = 79 * hash + (this.active ? 1 : 0);
        hash = 79 * hash + Objects.hashCode(this.createdBy);
        hash = 79 * hash + Objects.hashCode(this.modificatedBy);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Reward other = (Reward) obj;
        if (this.active != other.active) {
            return false;
        }
        if (!Objects.equals(this.rewardName, other.rewardName)) {
            return false;
        }
        if (!Objects.equals(this.activityType, other.activityType)) {
            return false;
        }
        if (!Objects.equals(this.frequencyOfUse, other.frequencyOfUse)) {
            return false;
        }
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.price, other.price)) {
            return false;
        }
        if (!Objects.equals(this.createdBy, other.createdBy)) {
            return false;
        }
        if (!Objects.equals(this.modificatedBy, other.modificatedBy)) {
            return false;
        }
        return true;
    }


}
