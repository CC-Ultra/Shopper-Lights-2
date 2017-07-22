package com.ultra.shopperlights2.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import com.ultra.shopperlights2.Adapters.MostRequiredAdapter;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.*;
import com.ultra.shopperlights2.Utils.BackgroundTask;
import com.ultra.shopperlights2.Utils.O;
import rx.Subscriber;

import java.util.*;
import java.util.concurrent.Callable;

/**
 * <p>Активность, в которой считается частота употребления продуктов</p>
 * <p><sub>(17.06.2017)</sub></p>
 * @author CC-Ultra
 */

public class MostRequiredActivity extends AppCompatActivity
	{
	private RecyclerView recyclerList;
	private Date to,from;

	/**
	 * Возвращает список частот для адаптера
	 */
	private class XCallable implements Callable<ArrayList<Frequency> >
		{
		@Override
		public ArrayList<Frequency> call() throws Exception
			{
			return getFrequencyList(from,to);
			}
		}

	/**
	 * Передает список частот адаптеру и тушит диалог
	 */
	private class XSubscriber extends Subscriber<ArrayList<Frequency> >
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
		public void onNext(ArrayList<Frequency> x)
			{
			Log.d(O.TAG,"onNext: ");
			initAdapter(x);
			dialog.dismiss();
			}
		}

	/**
	 * Очистка таблицы частот, потом получение всех продуктов в диапазоне дат, подсчет частот и заполнение таблицы частотами
	 */
	private void fillFrequencyTable(Date from,Date to)
		{
		App.session.getFrequencyDao().deleteAll();
		List<Purchase> purchases= App.session.getPurchaseDao().queryBuilder().where(PurchaseDao.Properties.Date.gt(from),
																			PurchaseDao.Properties.Date.le(to) ).list();
		ArrayList<Product> products= new ArrayList<>();
		for(Purchase purchase : purchases)
			products.addAll(purchase.getProducts() );
		HashMap<String,Integer> frequencyMap= new HashMap<>();
		for(Product product : products)
			{
			int n= (product.getN()==0 ? 1 : product.getN() );
			String t= product.getTitle();
			if(frequencyMap.containsKey(t) )
				frequencyMap.put(t, frequencyMap.get(t)+n);
			else
				frequencyMap.put(t,n);
			}
		for(Map.Entry<String,Integer> entry : frequencyMap.entrySet())
			{
			Frequency frequency= new Frequency();
			frequency.setTitle(entry.getKey() );
			frequency.setN(entry.getValue() );
			App.session.getFrequencyDao().insert(frequency);
			}
		}

	/**
	 * Заполнение таблицы частот, запрос частот в порядке убывания, отсечение всех, у кого частота ниже 2 раз
	 * @return список оставшихся частот
	 */
	private ArrayList<Frequency> getFrequencyList(Date from,Date to)
		{
		ArrayList<Frequency> result= new ArrayList<>();
		fillFrequencyTable(from,to);
		List<Frequency> rawFrequencies= App.session.getFrequencyDao().queryBuilder().orderDesc(FrequencyDao.Properties.N).list();
		for(Frequency frequency : rawFrequencies)
			{
			if(frequency.getN()<2)
				break;
			result.add(frequency);
			}
		return result;
		}
	private void initAdapter(ArrayList<Frequency> frequencies)
		{
		MostRequiredAdapter adapter= new MostRequiredAdapter(frequencies);
		recyclerList.setAdapter(adapter);
		recyclerList.setLayoutManager(new LinearLayoutManager(this) );
		}

	/**
	 * получение дат и запуск {@code BackgroundTask}
	 */
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
		{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.most_required_layout);

		Intent intent= getIntent();
		from= new Date(intent.getLongExtra(O.mapKeys.extra.DATE_FROM,0) );
		to= new Date(intent.getLongExtra(O.mapKeys.extra.DATE_TO,0) );

		recyclerList= (RecyclerView)findViewById(R.id.list);

		BackgroundTask<ArrayList<Frequency> > backgroundTask= new BackgroundTask<>(this,new XCallable() );
		backgroundTask.setSubscriber(new XSubscriber(backgroundTask.getDialog() ) );
		backgroundTask.start();
		}
	}
