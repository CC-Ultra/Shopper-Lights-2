package com.ultra.shopperlights2.Units;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * <p></p>
 * <p><sub>(10.05.2017)</sub></p>
 *
 * @author CC-Ultra
 */
@Entity
public class Product
	{
	@Id(autoincrement = true)
	private Long id;

	private boolean complete;
	private String title,weightUnit;
	private float price,weight,quality;
	private int n;
	private long manufacturerId,purchaseId;

	@ToMany
	@JoinEntity
			(
			entity = TagToProduct.class,
			sourceProperty = "productId",
			targetProperty = "tagId"
			)
	List<Tag> tags;

	/** Used to resolve relations */
	@Generated(hash = 2040040024)
	private transient DaoSession daoSession;

	/** Used for active entity operations. */
	@Generated(hash = 694336451)
	private transient ProductDao myDao;

	@Generated(hash = 1646973332)
	public Product(Long id, boolean complete, String title, String weightUnit,
			float price, float weight, float quality, int n, long manufacturerId,
			long purchaseId) {
		this.id = id;
		this.complete = complete;
		this.title = title;
		this.weightUnit = weightUnit;
		this.price = price;
		this.weight = weight;
		this.quality = quality;
		this.n = n;
		this.manufacturerId = manufacturerId;
		this.purchaseId = purchaseId;
	}

	@Generated(hash = 1890278724)
	public Product() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isComplete() {
		return this.complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getWeightUnit() {
		return this.weightUnit;
	}

	public void setWeightUnit(String weightUnit) {
		this.weightUnit = weightUnit;
	}

	public float getPrice() {
		return this.price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getWeight() {
		return this.weight;
	}

	public void setWeight(float weight) {
		this.weight = weight;
	}

	public float getQuality() {
		return this.quality;
	}

	public void setQuality(float quality) {
		this.quality = quality;
	}

	public int getN() {
		return this.n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public long getManufacturerId() {
		return this.manufacturerId;
	}

	public void setManufacturerId(long manufacturerId) {
		this.manufacturerId = manufacturerId;
	}

	public long getPurchaseId() {
		return this.purchaseId;
	}

	public void setPurchaseId(long purchaseId) {
		this.purchaseId = purchaseId;
	}

	/**
	 * To-many relationship, resolved on first access (and after reset).
	 * Changes to to-many relations are not persisted, make changes to the target entity.
	 */
	@Generated(hash = 1499514982)
	public List<Tag> getTags() {
		if (tags == null) {
			final DaoSession daoSession = this.daoSession;
			if (daoSession == null) {
				throw new DaoException("Entity is detached from DAO context");
			}
			TagDao targetDao = daoSession.getTagDao();
			List<Tag> tagsNew = targetDao._queryProduct_Tags(id);
			synchronized (this) {
				if (tags == null) {
					tags = tagsNew;
				}
			}
		}
		return tags;
	}

	/** Resets a to-many relationship, making the next get call to query for a fresh result. */
	@Generated(hash = 404234)
	public synchronized void resetTags() {
		tags = null;
	}

	/**
	 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
	 * Entity must attached to an entity context.
	 */
	@Generated(hash = 128553479)
	public void delete() {
		if (myDao == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		myDao.delete(this);
	}

	/**
	 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
	 * Entity must attached to an entity context.
	 */
	@Generated(hash = 1942392019)
	public void refresh() {
		if (myDao == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		myDao.refresh(this);
	}

	/**
	 * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
	 * Entity must attached to an entity context.
	 */
	@Generated(hash = 713229351)
	public void update() {
		if (myDao == null) {
			throw new DaoException("Entity is detached from DAO context");
		}
		myDao.update(this);
	}

	public boolean getComplete() {
		return this.complete;
	}

	/** called by internal mechanisms, do not call yourself. */
	@Generated(hash = 1171535257)
	public void __setDaoSession(DaoSession daoSession) {
		this.daoSession = daoSession;
		myDao = daoSession != null ? daoSession.getProductDao() : null;
	}
	}
