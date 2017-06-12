package com.ultra.shopperlights2.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.ultra.shopperlights2.Adapters.TagStatAdapter;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.*;
import com.ultra.shopperlights2.Utils.O;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p></p>
 * <p><sub>(12.06.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class TagStatActivity extends AppCompatActivity
	{
	private Date dateTo,dateFrom;
	private RecyclerView list;
	private Tag transportTag;

	private void initTransportTag()
		{
		TagDao tagDao= App.session.getTagDao();
		List<Tag> transportList= tagDao.queryBuilder().where(TagDao.Properties.Title.eq(O.TRANSPORT_TAG_NAME)).list();
		if(transportList.size()==0)
			{
			transportTag= new Tag();
			transportTag.setTitle(O.TRANSPORT_TAG_NAME);
			transportTag.setColor(Color.parseColor("#ff0000") );
			tagDao.insert(transportTag);
			}
		else
			transportTag= transportList.get(0);
		}
	private void dropTagsPrice()
		{
		for(Tag tag : App.session.getTagDao().loadAll() )
			{
			tag.setTotalPrice(0);
			App.session.getTagDao().update(tag);
			}
		}
	private void calculateTagPrice()
		{
		List<Purchase> purchases=App.session.getPurchaseDao().queryBuilder().where(PurchaseDao.Properties.Date.gt(dateFrom),
																		PurchaseDao.Properties.Date.le(dateTo)).list();
		for(Purchase purchase : purchases)
			{
			if(purchase.getProducts().size() == 0)
				{
				transportTag.setTotalPrice(transportTag.getTotalPrice() + purchase.getPrice() );
				App.session.getTagDao().update(transportTag);
				}
			else
				{
				for(Product product : purchase.getProducts() )
					{
					for(Tag tag : product.getTags() )
						{
						tag.setTotalPrice(tag.getTotalPrice() + product.getPrice()*(product.getN()==0 ? 1 : product.getN() ) );
						App.session.getTagDao().update(tag);
						}
					}
				}
			}
		}
	private void initAdapter()
		{
		float totalPrice=0;
		List<Tag> tags= App.session.getTagDao().queryBuilder().orderDesc(TagDao.Properties.TotalPrice).list();
		for(Tag tag : tags)
			totalPrice+= tag.getTotalPrice();
		TagStatAdapter adapter= new TagStatAdapter(this,new ArrayList<>(tags),totalPrice,dateFrom,dateTo);
		list.setLayoutManager(new LinearLayoutManager(this) );
		list.setAdapter(adapter);
		}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
		{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tag_stat_layout);

		Intent intent= getIntent();
		dateFrom= new Date(intent.getLongExtra(O.mapKeys.extra.DATE_FROM,0) );
		dateTo= new Date(intent.getLongExtra(O.mapKeys.extra.DATE_TO,0) );

		list= (RecyclerView)findViewById(R.id.list);

		initTransportTag();
		dropTagsPrice();
		calculateTagPrice();
		initAdapter();
		}
	}
