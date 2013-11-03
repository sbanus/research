package com.research.entity;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;

@Entity
@Configurable
public class FavourModel {

    /**
     */
    @NotNull
    @ManyToOne
    private UserModel userModel;

    /**
     */
    @NotNull
    private String description;
    

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

	@Version
    @Column(name = "version")
    private Integer version;

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

	public Integer getVersion() {
        return this.version;
    }

	public void setVersion(Integer version) {
        this.version = version;
    }

	public UserModel getUserModel() {
        return this.userModel;
    }

	public void setUserModel(UserModel userModel) {
        this.userModel = userModel;
    }

	public String getDescription() {
        return this.description;
    }

	public void setDescription(String description) {
        this.description = description;
    }

	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new FavourModel().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countFavourModels() {
        return entityManager().createQuery("SELECT COUNT(o) FROM FavourModel o", Long.class).getSingleResult();
    }

	public static List<FavourModel> findAllFavourModels() {
        return entityManager().createQuery("SELECT o FROM FavourModel o", FavourModel.class).getResultList();
    }

	public static FavourModel findFavourModel(Long id) {
        if (id == null) return null;
        return entityManager().find(FavourModel.class, id);
    }

	public static List<FavourModel> findFavourModelEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM FavourModel o", FavourModel.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

	@Transactional
    public void persist() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.persist(this);
    }

	@Transactional
    public void remove() {
        if (this.entityManager == null) this.entityManager = entityManager();
        if (this.entityManager.contains(this)) {
            this.entityManager.remove(this);
        } else {
            FavourModel attached = FavourModel.findFavourModel(this.id);
            this.entityManager.remove(attached);
        }
    }

	@Transactional
    public void flush() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.flush();
    }

	@Transactional
    public void clear() {
        if (this.entityManager == null) this.entityManager = entityManager();
        this.entityManager.clear();
    }

	@Transactional
    public FavourModel merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        FavourModel merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }
}
