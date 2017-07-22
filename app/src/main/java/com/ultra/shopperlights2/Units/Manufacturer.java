package com.ultra.shopperlights2.Units;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * <p><sub>(11.05.2017)</sub></p>
 * @author CC-Ultra
 */

@Entity
public class Manufacturer
	{
	@Id(autoincrement = true)
	Long id;

	String title;

	@Generated(hash = 670160011)
	public Manufacturer(Long id, String title) {
		this.id = id;
		this.title = title;
	}

	@Generated(hash = 1512554386)
	public Manufacturer() {
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
	}
