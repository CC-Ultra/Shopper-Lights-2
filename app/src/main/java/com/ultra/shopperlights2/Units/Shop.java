package com.ultra.shopperlights2.Units;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * <p></p>
 * <p><sub>(28.04.2017)</sub></p>
 *
 * @author CC-Ultra
 */

@Entity
public class Shop
	 {
	 @Id(autoincrement = true)
	 private Long id;

	 private String title,adr;

	 @ToMany(referencedJoinProperty = "shopId")
	 private List<Purchase> purchases;

		/** Used to resolve relations */
		@Generated(hash = 2040040024)
		private transient DaoSession daoSession;

		/** Used for active entity operations. */
		@Generated(hash = 173397329)
		private transient ShopDao myDao;

		@Generated(hash = 1536133714)
		public Shop(Long id, String title, String adr) {
			this.id = id;
			this.title = title;
			this.adr = adr;
		}

		@Generated(hash = 633476670)
		public Shop() {
		}

		public Long getId() {
			return this.id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getTitle() {
			return this.title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getAdr() {
			return this.adr;
		}

		public void setAdr(String adr) {
			this.adr = adr;
		}

		/**
		 * To-many relationship, resolved on first access (and after reset).
		 * Changes to to-many relations are not persisted, make changes to the target entity.
		 */
		@Generated(hash = 1455014133)
		public List<Purchase> getPurchases() {
			if (purchases == null) {
				final DaoSession daoSession = this.daoSession;
				if (daoSession == null) {
					throw new DaoException("Entity is detached from DAO context");
				}
				PurchaseDao targetDao = daoSession.getPurchaseDao();
				List<Purchase> purchasesNew = targetDao._queryShop_Purchases(id);
				synchronized (this) {
					if (purchases == null) {
						purchases = purchasesNew;
					}
				}
			}
			return purchases;
		}

		/** Resets a to-many relationship, making the next get call to query for a fresh result. */
		@Generated(hash = 528316983)
		public synchronized void resetPurchases() {
			purchases = null;
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
		@Generated(hash = 1040006210)
		public void __setDaoSession(DaoSession daoSession) {
			this.daoSession = daoSession;
			myDao = daoSession != null ? daoSession.getShopDao() : null;
		}
	 }
