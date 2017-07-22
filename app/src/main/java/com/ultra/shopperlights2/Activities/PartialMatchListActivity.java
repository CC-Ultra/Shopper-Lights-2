package com.ultra.shopperlights2.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.ultra.shopperlights2.Adapters.PartialMatchListAdapter;
import com.ultra.shopperlights2.Adapters.PriceDynamicsDetailsAdapter;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.PriceDynamicsUnit;
import com.ultra.shopperlights2.Utils.O;

import java.util.ArrayList;
import java.util.Date;

/**
 * <p>Активность-список частичного совпадения</p>
 * <p><sub>(18.06.2017)</sub></p>
 * @author CC-Ultra
 */

public class PartialMatchListActivity extends AppCompatActivity
	{
	private RecyclerView recyclerList;
	private ArrayList<PriceDynamicsUnit> list;

	private void initAdapter()
		{
		PartialMatchListAdapter adapter= new PartialMatchListAdapter(list);
		recyclerList.setAdapter(adapter);
		recyclerList.setLayoutManager(new LinearLayoutManager(this) );
		}
	private void getListFromIntent(Intent intent)
		{
		list= new ArrayList<>();
		long ids[],dates[];
		ids= intent.getLongArrayExtra(O.mapKeys.extra.PRODUCT_IDS);
		dates= intent.getLongArrayExtra(O.mapKeys.extra.PRODUCT_DATES);
		for(int i=0; i<ids.length; i++)
			{
			PriceDynamicsUnit unit= new PriceDynamicsUnit();
			unit.setProduct(App.session.getProductDao().load(ids[i] ) );
			unit.setDate(new Date(dates[i] ) );
			list.add(unit);
			}
		}


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
		{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.partial_match_list_layout);

		getListFromIntent(getIntent() );

		recyclerList= (RecyclerView)findViewById(R.id.list);

		initAdapter();
		}
	}
