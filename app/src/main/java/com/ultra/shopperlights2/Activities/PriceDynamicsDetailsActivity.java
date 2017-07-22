package com.ultra.shopperlights2.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import com.ultra.shopperlights2.Adapters.PriceDynamicsDetailsAdapter;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.PriceDynamicsUnit;
import com.ultra.shopperlights2.Utils.O;
import java.util.ArrayList;
import java.util.Date;

/**
 * <p><sub>(18.06.2017)</sub></p>
 * @author CC-Ultra
 */

public class PriceDynamicsDetailsActivity extends AppCompatActivity
	{
	private TextView title;
	private RecyclerView recyclerList;
	private ArrayList<PriceDynamicsUnit> pricelist;

	private void getPricelist(Intent intent)
		{
		pricelist= new ArrayList<>();
		long ids[],dates[];
		ids= intent.getLongArrayExtra(O.mapKeys.extra.PRODUCT_IDS);
		dates= intent.getLongArrayExtra(O.mapKeys.extra.PRODUCT_DATES);
		for(int i=0; i<ids.length; i++)
			{
			PriceDynamicsUnit unit= new PriceDynamicsUnit();
			unit.setProduct(App.session.getProductDao().load(ids[i] ) );
			unit.setDate(new Date(dates[i] ) );
			pricelist.add(unit);
			}
		}
	private void initAdapter()
		{
		PriceDynamicsDetailsAdapter adapter= new PriceDynamicsDetailsAdapter(pricelist);
		recyclerList.setAdapter(adapter);
		recyclerList.setLayoutManager(new LinearLayoutManager(this) );
		}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
		{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.price_dynamics_details_layout);

		getPricelist(getIntent() );

		title= (TextView)findViewById(R.id.title);
		recyclerList= (RecyclerView)findViewById(R.id.list);

		title.setText(pricelist.get(0).getProduct().getTitle() );
		initAdapter();
		}
	}
