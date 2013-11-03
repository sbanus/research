package com.research.entity;
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
import javax.persistence.OneToMany;
import javax.persistence.PersistenceContext;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.transaction.annotation.Transactional;

@Entity
@Configurable
public class UserModel {

    /**
     */
    @NotNull
    @Column(unique = true)
    @Size(max = 15)
    private String name;

    /**
     */
    @NotNull
    @Size(max = 15)
    private String password;

    /**
     */
    @NotNull
    @Size(max = 20)
    private String firstname;

    /**
     */
    @Size(max = 20)
    private String lastName;

    /**
     */
    @NotNull
    @Column(unique = true)
    private String email;
    
    /**
     */
    @OneToMany(cascade = CascadeType.ALL)
    private Set<FavourModel> favours = new HashSet<FavourModel>();


	@PersistenceContext
    transient EntityManager entityManager;

	public static final EntityManager entityManager() {
        EntityManager em = new UserModel().entityManager;
        if (em == null) throw new IllegalStateException("Entity manager has not been injected (is the Spring Aspects JAR configured as an AJC/AJDT aspects library?)");
        return em;
    }

	public static long countUserModels() {
        return entityManager().createQuery("SELECT COUNT(o) FROM UserModel o", Long.class).getSingleResult();
    }

	public static List<UserModel> findAllUserModels() {
        return entityManager().createQuery("SELECT o FROM UserModel o", UserModel.class).getResultList();
    }

	public static UserModel findUserModel(Long id) {
        if (id == null) return null;
        return entityManager().find(UserModel.class, id);
    }

	public static List<UserModel> findUserModelEntries(int firstResult, int maxResults) {
        return entityManager().createQuery("SELECT o FROM UserModel o", UserModel.class).setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
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
            UserModel attached = UserModel.findUserModel(this.id);
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
    public UserModel merge() {
        if (this.entityManager == null) this.entityManager = entityManager();
        UserModel merged = this.entityManager.merge(this);
        this.entityManager.flush();
        return merged;
    }

	public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public String getPassword() {
        return this.password;
    }

	public void setPassword(String password) {
        this.password = password;
    }

	public String getFirstname() {
        return this.firstname;
    }

	public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

	public String getLastName() {
        return this.lastName;
    }

	public void setLastName(String lastName) {
        this.lastName = lastName;
    }

	public String getEmail() {
        return this.email;
    }

	public void setEmail(String email) {
        this.email = email;
    }

	public Set<FavourModel> getFavours() {
        return this.favours;
    }

	public void setFavours(Set<FavourModel> favours) {
        this.favours = favours;
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
}
