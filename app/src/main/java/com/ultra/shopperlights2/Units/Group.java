package com.ultra.shopperlights2.Units;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

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
public class Group extends NoteListElement
	 {
	 @Id(autoincrement = true)
	 private Long id;

	 private String title;
	 private String holderTitle;
	 private boolean isOpen;
	 private int priority;

	 @ToMany(referencedJoinProperty = "groupId")
	 private List<Note> notes;

		/** Used to resolve relations */
		@Generated(hash = 2040040024)
		private transient DaoSession daoSession;

		/** Used for active entity operations. */
		@Generated(hash = 1591306109)
		private transient GroupDao myDao;

		@Generated(hash = 1161454468)
		public Group(Long id, String title, String holderTitle, boolean isOpen, int priority) {
			this.id = id;
			this.title = title;
			this.holderTitle = holderTitle;
			this.isOpen = isOpen;
			this.priority = priority;
		}

		@Generated(hash = 117982048)
		public Group() {
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

		public boolean isOpen() {
			return this.isOpen;
		}

		public void setIsOpen(boolean isOpen) {
			this.isOpen = isOpen;
		}

		public int getPriority() {
			return this.priority;
		}

		public void setPriority(int priority) {
			this.priority = priority;
		}

		/**
		 * To-many relationship, resolved on first access (and after reset).
		 * Changes to to-many relations are not persisted, make changes to the target entity.
		 */
		@Generated(hash = 1683342289)
		public List<Note> getNotes() {
			if (notes == null) {
				final DaoSession daoSession = this.daoSession;
				if (daoSession == null) {
					throw new DaoException("Entity is detached from DAO context");
				}
				NoteDao targetDao = daoSession.getNoteDao();
				List<Note> notesNew = targetDao._queryGroup_Notes(id);
				synchronized (this) {
					if (notes == null) {
						notes = notesNew;
					}
				}
			}
			return notes;
		}

		/** Resets a to-many relationship, making the next get call to query for a fresh result. */
		@Generated(hash = 2032098259)
		public synchronized void resetNotes() {
			notes = null;
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

		public String getHolderTitle() {
			return this.holderTitle;
		}

		public void setHolderTitle(String holderTitle) {
			this.holderTitle = holderTitle;
		}

		public boolean getIsOpen() {
			return this.isOpen;
		}

		/** called by internal mechanisms, do not call yourself. */
		@Generated(hash = 1333602095)
		public void __setDaoSession(DaoSession daoSession) {
			this.daoSession = daoSession;
			myDao = daoSession != null ? daoSession.getGroupDao() : null;
		}
	 }
