package com.ultra.shopperlights2.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.Longs;
import com.ultra.shopperlights2.Units.Product;
import com.ultra.shopperlights2.Units.ProductDao;
import com.ultra.shopperlights2.Units.Purchase;
import com.ultra.shopperlights2.Utils.BackgroundTask;
import com.ultra.shopperlights2.Utils.O;
import rx.Subscriber;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * <p></p>
 * <p><sub>(18.06.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class SearchActivity extends AppCompatActivity
	{
	private FrameLayout layoutFullMatch,layoutPartialMatch;
	private EditText input;
	private TextView txtFullMatch,txtPartialMatch;
	private Date from,to;
	private ArrayList<Longs> listLongsFullMatch, listLongsPartialMatch;

	private class XCallable implements Callable<Boolean>
		{
		@Override
		public Boolean call() throws Exception
			{
			getMatchLists(input.getText().toString() );
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
			initViews();
			dialog.dismiss();
			}
		}
	private class DummyListener implements View.OnClickListener
		{
		@Override
		public void onClick(View v) {}
		}
	private class SubmitButtonListener implements View.OnClickListener
		{
		@Override
		public void onClick(View v)
			{
			submitAction();
			}
		}
	private class SubmitEditTextListener implements TextView.OnEditorActionListener
		{
		@Override
		public boolean onEditorAction(TextView v,int actionId,KeyEvent event)
			{
			submitAction();
			return true;
			}
		}
	private class LayoutMatchListener implements View.OnClickListener
		{
		@Override
		public void onClick(View v)
			{
			long ids[];
			long dates[];
			Intent jumper= new Intent();
			int i=0;
			if(v.getId()==R.id.fullMatch)
				{
				ids= new long[listLongsFullMatch.size() ];
				dates= new long[listLongsFullMatch.size() ];
				jumper= new Intent(SearchActivity.this,PriceDynamicsDetailsActivity.class);
				for(Longs longs : listLongsFullMatch)
					{
					ids[i]= longs.id;
					dates[i]= longs.date;
					i++;
					}
				}
			else if(v.getId()==R.id.partialMatch)
				{
				ids= new long[listLongsPartialMatch.size() ];
				dates= new long[listLongsPartialMatch.size() ];
				jumper= new Intent(SearchActivity.this,PartialMatchListActivity.class);
				for(Longs longs : listLongsPartialMatch)
					{
					ids[i]= longs.id;
					dates[i]= longs.date;
					i++;
					}
				}
			else
				{
				ids= new long[1];
				dates= new long[1];
				}
			jumper.putExtra(O.mapKeys.extra.PRODUCT_IDS,ids);
			jumper.putExtra(O.mapKeys.extra.PRODUCT_DATES,dates);
			startActivity(jumper);
			}
		}

	private void submitAction()
		{
		if(input.getText().toString().length()==0)
			{
			input.setError("Введи что-нибудь сначала");
			return;
			}
		BackgroundTask<Boolean> backgroundTask= new BackgroundTask<>(this,new XCallable() );
		backgroundTask.setSubscriber(new XSubscriber(backgroundTask.getDialog() ) );
		backgroundTask.start();
		}
	private void initViews()
		{
		layoutFullMatch.setVisibility(View.VISIBLE);
		layoutPartialMatch.setVisibility(View.VISIBLE);
		if(listLongsFullMatch.size()==0)
			{
			txtFullMatch.setText("Полных совпадений не найдено");
			layoutFullMatch.setOnClickListener(new DummyListener() );
			}
		else
			{
			txtFullMatch.setText(input.getText().toString() );
			layoutFullMatch.setOnClickListener(new LayoutMatchListener() );
			}
		if(listLongsPartialMatch.size()==0)
			{
			txtPartialMatch.setText("Частичных совпадений не найдено");
			layoutPartialMatch.setOnClickListener(new DummyListener() );
			}
		else
			{
			txtPartialMatch.setText(listLongsPartialMatch.size() +" совпадений...");
			layoutPartialMatch.setOnClickListener(new LayoutMatchListener() );
			}
		}
	private void getMatchLists(String key)
		{
		listLongsFullMatch= new ArrayList<>();
		listLongsPartialMatch= new ArrayList<>();
		List<Product> products= App.session.getProductDao().queryBuilder().
									where(ProductDao.Properties.Title.like("%"+key.toLowerCase()+"%")).limit(150).list();
		for(Product product : products)
			{
			Purchase purchase= App.session.getPurchaseDao().load(product.getPurchaseId() );
			if(purchase==null)
				continue;
			Date date= purchase.getDate();
			if(date.before(from) || date.after(to) )
				continue;
			Longs longs= new Longs(product.getId(),date.getTime() );
			if(product.getTitle().equals(key) )
				listLongsFullMatch.add(longs);
			else
				listLongsPartialMatch.add(longs);
			}
		}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
		{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_layout);

		Intent intent= getIntent();
		from= new Date(intent.getLongExtra(O.mapKeys.extra.DATE_FROM,0) );
		to= new Date(intent.getLongExtra(O.mapKeys.extra.DATE_TO,0) );

		Button btnSubmit= (Button)findViewById(R.id.btnSubmit);
		input= (EditText)findViewById(R.id.inputKeyword);
		layoutFullMatch= (FrameLayout)findViewById(R.id.fullMatch);
		layoutPartialMatch= (FrameLayout)findViewById(R.id.partialMatch);
		txtFullMatch= (TextView)findViewById(R.id.txtFullMatch);
		txtPartialMatch= (TextView)findViewById(R.id.txtPartialMatch);

		btnSubmit.setOnClickListener(new SubmitButtonListener() );
		input.setOnEditorActionListener(new SubmitEditTextListener() );
		}
	}
