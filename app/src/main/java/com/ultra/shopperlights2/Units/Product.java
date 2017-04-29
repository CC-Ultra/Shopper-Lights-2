package com.ultra.shopperlights2.Units;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * <p></p>
 * <p><sub>(27.04.2017)</sub></p>
 *
 * @author CC-Ultra
 */

@Entity
public class Product
	 {
	 @Id(autoincrement = true)
	 private Long id;

	 private String title,weightUnit;
	 private float price,weight,qualty;
	 private int n;
	 private long manufacturerId,purchaseId,tagId;
		@Generated(hash = 846502956)
		public Product(Long id, String title, String weightUnit, float price,
				float weight, float qualty, int n, long manufacturerId, long purchaseId,
				long tagId) {
			this.id = id;
			this.title = title;
			this.weightUnit = weightUnit;
			this.price = price;
			this.weight = weight;
			this.qualty = qualty;
			this.n = n;
			this.manufacturerId = manufacturerId;
			this.purchaseId = purchaseId;
			this.tagId = tagId;
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
		public String getTitle() {
			return this.title;
		}
		public void setTitle(String title) {
			this.title = title;
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
		public float getQualty() {
			return this.qualty;
		}
		public void setQualty(float qualty) {
			this.qualty = qualty;
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
		public long getTagId() {
			return this.tagId;
		}
		public void setTagId(long tagId) {
			this.tagId = tagId;
		}
		public String getWeightUnit() {
			return this.weightUnit;
		}
		public void setWeightUnit(String weightUnit) {
			this.weightUnit = weightUnit;
		}
	 }
