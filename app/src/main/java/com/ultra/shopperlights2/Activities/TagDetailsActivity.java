package com.ultra.shopperlights2.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import com.ultra.shopperlights2.Adapters.TagDetailsAdapter;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.*;
import com.ultra.shopperlights2.Utils.Calc;
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

public class TagDetailsActivity extends AppCompatActivity
	{
	private RecyclerView list;
	private Tag tag;
	private Date dateFrom,dateTo;

	private void initAdapter()
		{
		ArrayList<Product> result= new ArrayList<>();
		ArrayList<Product> products= new ArrayList<>();
		List<Purchase> purchases= App.session.getPurchaseDao().queryBuilder().where(PurchaseDao.Properties.Date.gt(dateFrom),
																			PurchaseDao.Properties.Date.le(dateTo)).list();
		for(Purchase purchase : purchases)
			for(Product product : purchase.getProducts())
				products.add(product);
		List<TagToProduct> ttps= App.session.getTagToProductDao().queryBuilder().where(TagToProductDao.Properties.TagId.eq(tag.getId() ) ).list();
		for(TagToProduct ttp : ttps)
			{
			for(Product product : products)
				{
				if(ttp.getProductId()==product.getId() )
					{
					result.add(product);
					break;
					}
				}
			}
		TagDetailsAdapter adapter= new TagDetailsAdapter(result);
		this.list.setLayoutManager(new LinearLayoutManager(this) );
		this.list.setAdapter(adapter);
		}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
		{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tag_details_layout);

		Intent intent= getIntent();
		tag= App.session.getTagDao().load(intent.getLongExtra(O.mapKeys.extra.TAG_ID,0) );
		dateFrom= new Date(intent.getLongExtra(O.mapKeys.extra.DATE_FROM,0) );
		dateTo= new Date(intent.getLongExtra(O.mapKeys.extra.DATE_TO,0) );

		TextView txtTitle= (TextView)findViewById(R.id.title);
		TextView price= (TextView)findViewById(R.id.price);
		list= (RecyclerView)findViewById(R.id.list);

		String title= tag.getTitle();
		if(title.equals(O.TRANSPORT_TAG_NAME) )
			title="транспорт";
		txtTitle.setText(title);
		txtTitle.setTextColor(tag.getColor() );
		price.setText(""+ Calc.round(tag.getTotalPrice() ) );
		initAdapter();
		}
	}
