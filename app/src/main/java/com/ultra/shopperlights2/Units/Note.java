package com.ultra.shopperlights2.Units;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

/**
 * <p><sub>(10.05.2017)</sub></p>
 * @author CC-Ultra
 */

@Entity
public class Note extends GreenRecyclerListElement
	{
	@Id(autoincrement = true)
	private Long id;

	private boolean locked,ethereal;
	private String title;
	private int n;
	private long groupId,templateId,productId;

	@ToMany
	@JoinEntity
			(
			entity = TagToNote.class,
			sourceProperty = "noteId",
			targetProperty = "tagId"
			)
	private List<Tag> tags;

	/** Used to resolve relations */
	@Generated(hash = 2040040024)
	private transient DaoSession daoSession;

	/** Used for active entity operations. */
	@Generated(hash = 363862535)
	private transient NoteDao myDao;

	@Generated(hash = 719689438)
	public Note(Long id, boolean locked, boolean ethereal, String title, int n, long groupId,
			long templateId, long productId) {
		this.id = id;
		this.locked = locked;
		this.ethereal = ethereal;
		this.title = title;
		this.n = n;
		this.groupId = groupId;
		this.templateId = templateId;
		this.productId = productId;
	}

	@Generated(hash = 1272611929)
	public Note() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isLocked() {
		return this.locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getN() {
		return this.n;
	}

	public void setN(int n) {
		this.n = n;
	}

	public long getGroupId() {
		return this.groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	/**
	 * To-many relationship, resolved on first access (and after reset).
	 * Changes to to-many relations are not persisted, make changes to the target entity.
	 */
	@Generated(hash = 1070775174)
	public List<Tag> getTags() {
		if (tags == null) {
			final DaoSession daoSession = this.daoSession;
			if (daoSession == null) {
				throw new DaoException("Entity is detached from DAO context");
			}
			TagDao targetDao = daoSession.getTagDao();
			List<Tag> tagsNew = targetDao._queryNote_Tags(id);
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

	public boolean getLocked() {
		return this.locked;
	}

	public long getTemplateId() {
		return this.templateId;
	}

	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}

	public boolean isEthereal() {
		return this.ethereal;
	}

	public void setEthereal(boolean ethereal) {
		this.ethereal = ethereal;
	}

	public boolean getEthereal() {
		return this.ethereal;
	}

	public long getProductId() {
		return this.productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	/** called by internal mechanisms, do not call yourself. */
	@Generated(hash = 799086675)
	public void __setDaoSession(DaoSession daoSession) {
		this.daoSession = daoSession;
		myDao = daoSession != null ? daoSession.getNoteDao() : null;
	}
	}
