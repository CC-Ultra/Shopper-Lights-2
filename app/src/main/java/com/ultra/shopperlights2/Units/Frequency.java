package com.ultra.shopperlights2.Units;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * <p></p>
 * <p><sub>(16.06.2017)</sub></p>
 *
 * @author CC-Ultra
 */

@Entity
public class Frequency
	{
	@Id(autoincrement = true)
	Long id;

	private int n;
	private String title;
	@Generated(hash = 353065084)
	public Frequency(Long id, int n, String title) {
		this.id = id;
		this.n = n;
		this.title = title;
	}
	@Generated(hash = 1580276267)
	public Frequency() {
	}
	public Long getId() {
		return this.id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public int getN() {
		return this.n;
	}
	public void setN(int n) {
		this.n = n;
	}
	public String getTitle() {
		return this.title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	}
