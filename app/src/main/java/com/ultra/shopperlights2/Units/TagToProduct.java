package com.ultra.shopperlights2.Units;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * <p></p>
 * <p><sub>(10.05.2017)</sub></p>
 *
 * @author CC-Ultra
 */

@Entity
public class TagToProduct
	{
	@Id(autoincrement = true)
	Long id;

	long tagId,productId;

	@Generated(hash = 426009849)
	public TagToProduct(Long id, long tagId, long productId) {
		this.id = id;
		this.tagId = tagId;
		this.productId = productId;
	}

	@Generated(hash = 1157098039)
	public TagToProduct() {
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

	public long getProductId() {
		return this.productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}
	}
