package com.ultra.shopperlights2.Units;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.Date;
import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * <p></p>
 * <p><sub>(27.04.2017)</sub></p>
 *
 * @author CC-Ultra
 */

@Entity
public class Purchase
	 {
	 @Id(autoincrement = true)
	 private Long id;

	 private Date date;
	 private float price;
	 private long shopId;

	 @ToMany(referencedJoinProperty = "purchaseId")
	 private List<Product> products;

		/** Used to resolve relations */
		@Generated(hash = 2040040024)
		private transient DaoSession daoSession;

		/** Used for active entity operations. */
		@Generated(hash = 1807587041)
		private transient PurchaseDao myDao;

		@Generated(hash = 418095188)
		public Purchase(Long id, Date date, float price, long shopId) {
			this.id = id;
			this.date = date;
			this.price = price;
			this.shopId = shopId;
		}

		@Generated(hash = 1281646125)
		public Purchase() {
		}

		public Long getId() {
			return this.id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Date getDate() {
			return this.date;
		}

		public void setDate(Date date) {
			this.date = date;
		}

		public float getPrice() {
			return this.price;
		}

		public void setPrice(float price) {
			this.price = price;
		}

		public long getShopId() {
			return this.shopId;
		}

		public void setShopId(long shopId) {
			this.shopId = shopId;
		}

		/**
		 * To-many relationship, resolved on first access (and after reset).
		 * Changes to to-many relations are not persisted, make changes to the target entity.
		 */
		@Generated(hash = 1543680266)
		public List<Product> getProducts() {
			if (products == null) {
				final DaoSession daoSession = this.daoSession;
				if (daoSession == null) {
					throw new DaoException("Entity is detached from DAO context");
				}
				ProductDao targetDao = daoSession.getProductDao();
				List<Product> productsNew = targetDao._queryPurchase_Products(id);
				synchronized (this) {
					if (products == null) {
						products = productsNew;
					}
				}
			}
			return products;
		}

		/** Resets a to-many relationship, making the next get call to query for a fresh result. */
		@Generated(hash = 513498032)
		public synchronized void resetProducts() {
			products = null;
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

		/** called by internal mechanisms, do not call yourself. */
		@Generated(hash = 219300322)
		public void __setDaoSession(DaoSession daoSession) {
			this.daoSession = daoSession;
			myDao = daoSession != null ? daoSession.getPurchaseDao() : null;
		}
	 }
