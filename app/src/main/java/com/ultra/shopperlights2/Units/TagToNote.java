package com.ultra.shopperlights2.Units;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * <p><sub>(27.04.2017)</sub></p>
 * @author CC-Ultra
 */

@Entity
public class TagToNote
	 {
	 @Id(autoincrement = true)
	 private Long id;

	 private long tagId,noteId;

		@Generated(hash = 1314824553)
		public TagToNote(Long id, long tagId, long noteId) {
			this.id = id;
			this.tagId = tagId;
			this.noteId = noteId;
		}

		@Generated(hash = 1490629053)
		public TagToNote() {
		}

		public Long getId() {
			return this.id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public long getTagId() {
			return this.tagId;
		}

		public void setTagId(long tagId) {
			this.tagId = tagId;
		}

		public long getNoteId() {
			return this.noteId;
		}

		public void setNoteId(long noteId) {
			this.noteId = noteId;
		}
	 }
