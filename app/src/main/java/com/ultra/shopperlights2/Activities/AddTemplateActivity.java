package com.ultra.shopperlights2.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.ultra.shopperlights2.Adapters.TemplatesAdapter;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.Callbacks.EditTemplateCallback;
import com.ultra.shopperlights2.Dialogs.AddTemplateDialog;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.Template;
import com.ultra.shopperlights2.Units.TemplateDao;
import com.ultra.shopperlights2.Utils.O;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Активность списка вроде GTS, только для шаблонов.</p>
 * Здесь можно добавить шаблон
 * <p><sub>(05.06.2017)</sub></p>
 * @author CC-Ultra
 */

public class AddTemplateActivity extends AppCompatActivity implements EditTemplateCallback
	{
	private RecyclerView list;
	private BroadcastReceiver receiver;

	private class Receiver extends BroadcastReceiver
		{
		@Override
		public void onReceive(Context context,Intent intent)
			{
			initAdapter();
			}
		}
	private class AddElementListener implements View.OnClickListener
		{
		@Override
		public void onClick(View v)
			{
			AddTemplateDialog dialog= new AddTemplateDialog();
			dialog.init(AddTemplateActivity.this,(ViewGroup)v.getParent(),O.actions.ACTION_ADD_TEMPLATE_ACTIVITY,"Добавить шаблон");
			dialog.createAndShow();
			}
		}

	/**
	 * @param parent нужен для диалога
	 * @param templateId шаблон, который надо изменить
	 */
	@Override
	public void editTemplate(ViewGroup parent,long templateId)
		{
		AddTemplateDialog dialog= new AddTemplateDialog();
		dialog.init(AddTemplateActivity.this,parent,O.actions.ACTION_ADD_TEMPLATE_ACTIVITY,"Изменить шаблон",templateId);
		dialog.createAndShow();
		}
	private void initAdapter()
		{
		ArrayList<Template> templates= new ArrayList<>(App.session.getTemplateDao().loadAll() );
		List<Template> tempList= App.session.getTemplateDao().queryBuilder().where(TemplateDao.Properties.Title.eq(O.TEMP_TEMPLATE_NAME)).list();
		if(tempList.size()!=0)
			{
			templates.remove(tempList.get(0) );
			Log.d(O.TAG,"initAdapter: tempTemplate ignored");
			}
		TemplatesAdapter adapter= new TemplatesAdapter(this,templates,this);
		this.list.setAdapter(adapter);
		}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
		{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gts_layout);

		Button btnAdd= (Button)findViewById(R.id.btn_add);
		list= (RecyclerView)findViewById(R.id.list);

		btnAdd.setOnClickListener(new AddElementListener() );
		btnAdd.setText("Добавить шаблон");
		list.setLayoutManager(new LinearLayoutManager(this) );
		receiver= new Receiver();
		IntentFilter filter= new IntentFilter(O.actions.ACTION_ADD_TEMPLATE_ACTIVITY);
		registerReceiver(receiver,filter);
		}
	@Override
	protected void onResume()
		{
		super.onResume();
		initAdapter();
		}
	@Override
	protected void onDestroy()
		{
		super.onDestroy();
		unregisterReceiver(receiver);
		}
	}
