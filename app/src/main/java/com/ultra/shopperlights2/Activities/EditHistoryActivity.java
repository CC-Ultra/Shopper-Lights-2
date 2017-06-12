package com.ultra.shopperlights2.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ultra.shopperlights2.Adapters.EditHistoryAdapter;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.Purchase;
import com.ultra.shopperlights2.Units.PurchaseDao;
import com.ultra.shopperlights2.Utils.Calc;
import com.ultra.shopperlights2.Utils.DateUtil;
import com.ultra.shopperlights2.Utils.O;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p></p>
 * <p><sub>(11.06.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class EditHistoryActivity extends AppCompatActivity
	{
	private TextView txtTotalPrice;
	private float totalPrice;
	private RecyclerView list;
	private ArrayList<Purchase> purchases;
	private Date dateTo,dateFrom;

	private void initTotalPrice()
		{
		List<Purchase> p= App.session.getPurchaseDao().queryBuilder().where(PurchaseDao.Properties.Date.gt(dateFrom),
																	PurchaseDao.Properties.Date.le(dateTo) ).list();
		purchases= new ArrayList<>(p);
		totalPrice=0;
		for(Purchase purchase : purchases)
			totalPrice+= purchase.getPrice();
		txtTotalPrice.setText(""+ Calc.round(totalPrice) );
		}
	private void initList()
		{
		List<Purchase> p= App.session.getPurchaseDao().queryBuilder().where(PurchaseDao.Properties.Date.gt(dateFrom),
									PurchaseDao.Properties.Date.le(dateTo) ).orderDesc(PurchaseDao.Properties.Date).list();
		purchases.clear();
		for(Purchase purchase : p)
			{
			if(purchase.getProducts().size()==0)
				continue;
			purchases.add(purchase);
			}
		EditHistoryAdapter adapter= new EditHistoryAdapter(this,purchases,totalPrice);
		list.setAdapter(adapter);
		list.setLayoutManager(new LinearLayoutManager(this) );
		}
	private void cleanEmptyPurchases()
		{
		List<Purchase> p= App.session.getPurchaseDao().queryBuilder().where(PurchaseDao.Properties.Date.gt(dateFrom),
																	PurchaseDao.Properties.Date.le(dateTo) ).list();
		for(Purchase purchase : p)
			{
			if(purchase.getPrice()==0)
				App.session.getPurchaseDao().delete(purchase);
			}
		}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
		{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_history_layout);

		Intent intent= getIntent();
		dateFrom= new Date(intent.getLongExtra(O.mapKeys.extra.DATE_FROM,0) );
		dateTo= new Date(intent.getLongExtra(O.mapKeys.extra.DATE_TO,0) );

		txtTotalPrice= (TextView)findViewById(R.id.txtTotalPrice);
		list= (RecyclerView)findViewById(R.id.historyList);
		TextView txtDateFrom= (TextView)findViewById(R.id.txtDateFrom);
		TextView txtDateTo= (TextView)findViewById(R.id.txtDateTo);

		txtDateFrom.setText(DateUtil.getDateStr(dateFrom) );
		txtDateTo.setText(DateUtil.getDateStr(dateTo) );
		}
	@Override
	protected void onResume()
		{
		super.onResume();
		cleanEmptyPurchases();
		initTotalPrice();
		initList();
		}
	}
