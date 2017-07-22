package com.ultra.shopperlights2.Units;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * <p><sub>(05.06.2017)</sub></p>
 * @author CC-Ultra
 */

@Entity
public class Template
	{
	@Id(autoincrement = true)
	private Long id;

	private String title;
	@ToMany(referencedJoinProperty = "templateId")
	private List<Note> notes;

	/** Used to resolve relations */
	@Generated(hash = 2040040024)
	private transient DaoSession daoSession;

	/** Used for active entity operations. */
	@Generated(hash = 308168968)
	private transient TemplateDao myDao;
	@Generated(hash = 598335608)
	public Template(Long id, String title) {
		this.id = id;
		this.title = title;
	}
	@Generated(hash = 227162429)
	public Template() {
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
	/**
	 * To-many relationship, resolved on first access (and after reset).
	 * Changes to to-many relations are not persisted, make changes to the target entity.
	 */
	@Generated(hash = 1814343532)
	public List<Note> getNotes() {
		if (notes == null) {
			final DaoSession daoSession = this.daoSession;
			if (daoSession == null) {
				throw new DaoException("Entity is detached from DAO context");
			}
			NoteDao targetDao = daoSession.getNoteDao();
			List<Note> notesNew = targetDao._queryTemplate_Notes(id);
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
	/** called by internal mechanisms, do not call yourself. */
	@Generated(hash = 1094237480)
	public void __setDaoSession(DaoSession daoSession) {
		this.daoSession = daoSession;
		myDao = daoSession != null ? daoSession.getTemplateDao() : null;
	}
	}
