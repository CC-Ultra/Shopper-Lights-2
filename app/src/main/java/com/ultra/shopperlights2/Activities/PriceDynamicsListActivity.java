package com.ultra.shopperlights2.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.Longs;
import com.ultra.shopperlights2.Units.Product;
import com.ultra.shopperlights2.Units.Purchase;
import com.ultra.shopperlights2.Units.PurchaseDao;
import com.ultra.shopperlights2.Utils.BackgroundTask;
import com.ultra.shopperlights2.Utils.O;
import rx.Subscriber;

import java.util.*;
import java.util.concurrent.Callable;

/**
 * <p></p>
 * <p><sub>(18.06.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class PriceDynamicsListActivity extends AppCompatActivity
	{
	private ListView list;
	private TreeMap<String,ArrayList<Longs> > productMap;
	private Date from;
	private Date to;
	private ArrayAdapter<String> adapter;

	private class XCallable implements Callable<Boolean>
		{
		@Override
		public Boolean call() throws Exception
			{
			initMap();
			filterMap();
			return true;
			}
		}
	private class XSubscriber extends Subscriber<Boolean>
		{
		ProgressDialog dialog;

		XSubscriber(ProgressDialog _dialog)
			{
			dialog=_dialog;
			}

		@Override
		public void onCompleted() {}
		@Override
		public void onError(Throwable e)
			{
			Log.e(O.TAG,"XSubscriber.onError: ",e);
			}
		@Override
		public void onNext(Boolean x)
			{
			Log.d(O.TAG,"onNext: ");
			initList();
			dialog.dismiss();
			}
		}
	private class ListListener implements AdapterView.OnItemClickListener
		{
		@Override
		public void onItemClick(AdapterView<?> parent,View view,int position,long id)
			{
			String item= adapter.getItem(position);
			long ids[],dates[];
			ArrayList<Longs> longs= productMap.get(item);
			ids= new long[longs.size() ];
			dates= new long[longs.size() ];
			int i=0;
			for(Longs x : longs)
				{
				ids[i]= x.id;
				dates[i]= x.date;
				i++;
				}
			Intent jumper= new Intent(PriceDynamicsListActivity.this,PriceDynamicsDetailsActivity.class);
			jumper.putExtra(O.mapKeys.extra.PRODUCT_IDS,ids);
			jumper.putExtra(O.mapKeys.extra.PRODUCT_DATES,dates);
			startActivity(jumper);
			}
		}

	private void initList()
		{
		adapter= new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,new ArrayList<>(productMap.keySet() ) );
		list.setAdapter(adapter);
		}
	private void initMap()
		{
		productMap= new TreeMap<>();
		List<Purchase> purchases=App.session.getPurchaseDao().queryBuilder().where(PurchaseDao.Properties.Date.gt(from),
				PurchaseDao.Properties.Date.le(to) ).list();
		for(Purchase purchase : purchases)
			{
			for(Product product : purchase.getProducts() )
				{
				if(productMap.containsKey(product.getTitle() ) )
					productMap.get(product.getTitle() ).add(new Longs(product.getId(), purchase.getDate().getTime() ) );
				else
					{
					ArrayList<Longs> x= new ArrayList<>();
					x.add(new Longs(product.getId(), purchase.getDate().getTime() ) );
					productMap.put(product.getTitle(),x);
					}
				}
			}
		}
	private void filterMap()
		{
		TreeMap<String,ArrayList<Longs> > resMap= new TreeMap<>();
		for(Map.Entry<String,ArrayList<Longs> > entry : productMap.entrySet())
			if(entry.getValue().size()>1)
				resMap.put(entry.getKey(),entry.getValue() );
		productMap=resMap;
		}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
		{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.price_dynamics_list_layout);

		Intent intent= getIntent();
		from= new Date(intent.getLongExtra(O.mapKeys.extra.DATE_FROM,0) );
		to= new Date(intent.getLongExtra(O.mapKeys.extra.DATE_TO,0) );

		list= (ListView)findViewById(R.id.list);

		BackgroundTask<Boolean> backgroundTask= new BackgroundTask<>(this,new XCallable() );
		backgroundTask.setSubscriber(new XSubscriber(backgroundTask.getDialog() ) );
		backgroundTask.start();
		list.setOnItemClickListener(new ListListener() );
		}
	}
