package com.ultra.shopperlights2.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import com.ultra.shopperlights2.Adapters.EditHistoryAdapter;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.Purchase;
import com.ultra.shopperlights2.Units.PurchaseDao;
import com.ultra.shopperlights2.Utils.BackgroundTask;
import com.ultra.shopperlights2.Utils.Calc;
import com.ultra.shopperlights2.Utils.DateUtil;
import com.ultra.shopperlights2.Utils.O;
import rx.Subscriber;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * <p>Активность для просмотра и редактирования истории</p>
 * Здесь идет запрос к базе через {@link BackgroundTask}
 * <p><sub>(11.06.2017)</sub></p>
 * @author CC-Ultra
 */

public class EditHistoryActivity extends AppCompatActivity
	{
	private TextView txtTotalPrice;
	private float totalPrice;
	private RecyclerView list;
	private ArrayList<Purchase> purchases;
	private Date dateTo,dateFrom;

	/**
	 * В задаче для {@code BackgroundTask} получаю список покупок и чищу пустые покупки
	 */
	private class XCallable implements Callable<List<Purchase> >
		{
		@Override
		public List<Purchase> call() throws Exception
			{
			List<Purchase> p= App.session.getPurchaseDao().queryBuilder().where(PurchaseDao.Properties.Date.gt(dateFrom),
					PurchaseDao.Properties.Date.le(dateTo) ).orderDesc(PurchaseDao.Properties.Date).list();
			cleanEmptyPurchases(p);
			return p;
			}
		}

	/**
	 * В основном потоке считаю и оновляю {@code txtTotalPrice}, инициализирую список и тушу диалог
	 */
	private class XSubscriber extends Subscriber<List<Purchase> >
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
		public void onNext(List<Purchase> x)
			{
			Log.d(O.TAG,"onNext: ");
			initTotalPrice(x);
			initList(x);
			dialog.dismiss();
			}
		}
	private void initTotalPrice(List<Purchase> p)
		{
		purchases= new ArrayList<>(p);
		totalPrice=0;
		for(Purchase purchase : purchases)
			totalPrice+= purchase.getPrice();
		txtTotalPrice.setText(""+ Calc.round(totalPrice) );
		}
	private void initList(List<Purchase> p)
		{
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

	/**
	 * Пустой покупкой считается покупка с нулевой стоимостью, пустым продукт-списком и завершенная
	 */
	private void cleanEmptyPurchases(List<Purchase> p)
		{
		for(Purchase purchase : p)
			{
			if(purchase.getPrice()==0 && purchase.isCompleted() && purchase.getProducts().size()==0)
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
		BackgroundTask<List<Purchase> > backgroundTask= new BackgroundTask<>(this,new XCallable() );
		backgroundTask.setSubscriber(new XSubscriber(backgroundTask.getDialog() ) );
		backgroundTask.start();
		}
	}
