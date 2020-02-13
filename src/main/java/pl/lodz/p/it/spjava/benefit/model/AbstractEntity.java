package pl.lodz.p.it.spjava.benefit.model;

import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

@MappedSuperclass
public abstract class AbstractEntity {

    protected static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = "VERSION", nullable = false)
    @Version
    private int version;

    @NotNull
    @Column(name = "CREATION_DATE", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @NotNull
    @Column(name = "MODIFICATION_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date modificationDate;

    @PrePersist
    public void initCreationDate() {
        creationDate = new Date();
    }

    @PreUpdate
    public void initModificationDate() {
        modificationDate = new Date();
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + this.version;
        hash = 97 * hash + Objects.hashCode(this.creationDate);
        hash = 97 * hash + Objects.hashCode(this.modificationDate);
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
        final AbstractEntity other = (AbstractEntity) obj;
        if (this.version != other.version) {
            return false;
        }
        if (!Objects.equals(this.creationDate, other.creationDate)) {
            return false;
        }
        if (!Objects.equals(this.modificationDate, other.modificationDate)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AbstractEntity: " + "version=" + version + ", creationDate=" + creationDate;
    }

}
