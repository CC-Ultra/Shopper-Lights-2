package com.ultra.shopperlights2.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import com.ultra.shopperlights2.Adapters.TemplatesAdapter;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.Callbacks.EditTemplateCallback;
import com.ultra.shopperlights2.Fragments.AddTemplateDialog;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.Template;
import com.ultra.shopperlights2.Utils.O;

import java.util.ArrayList;

/**
 * <p></p>
 * <p><sub>(05.06.2017)</sub></p>
 *
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
			dialog.init(O.actions.ACTION_ADD_TEMPLATE_ACTIVITY,"Добавить шаблон");
			FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
			dialog.show(transaction,"");
			}
		}

	@Override
	public void editTemplate(long templateId)
		{
		AddTemplateDialog dialog= new AddTemplateDialog();
		dialog.init(O.actions.ACTION_ADD_TEMPLATE_ACTIVITY,"Изменить шаблон",templateId);
		FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
		dialog.show(transaction,"");
		}
	private void initAdapter()
		{
		ArrayList<Template> templates= new ArrayList<>(App.session.getTemplateDao().loadAll() );
		TemplatesAdapter adapter= new TemplatesAdapter(this,templates,this);
		list.setAdapter(adapter);
		}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
		{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_list_layout);

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
