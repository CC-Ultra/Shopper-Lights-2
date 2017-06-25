package com.ultra.shopperlights2.Units;

import java.util.Date;

/**
 * <p></p>
 * <p><sub>(18.06.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class PriceDynamicsUnit
	{
	private Product product;
	private Date date;

	public Date getDate()
		{
		return date;
		}
	public void setDate(Date _date)
		{
		date=_date;
		}
	public Product getProduct()
		{
		return product;
		}
	public void setProduct(Product _product)
		{
		product=_product;
		}
	}
