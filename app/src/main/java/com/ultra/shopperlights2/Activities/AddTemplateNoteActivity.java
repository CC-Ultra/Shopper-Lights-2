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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.ultra.shopperlights2.Adapters.TemplatesNotesAdapter;
import com.ultra.shopperlights2.Callbacks.EditNoteTemplateCallback;
import com.ultra.shopperlights2.Dialogs.AddNoteDialog;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Utils.O;

/**
 * <p>Активность списка записей шаблона</p>
 * Здесь можно редактировать список записей шаблона
 * <p><sub>(06.06.2017)</sub></p>
 * @author CC-Ultra
 */

public class AddTemplateNoteActivity extends AppCompatActivity implements EditNoteTemplateCallback
	{
	private BroadcastReceiver receiver;
	private RecyclerView list;
	private long templateId;

	private class Receiver extends BroadcastReceiver
		{
		@Override
		public void onReceive(Context context,Intent intent)
			{
			initAdapter();
			}
		}
	private class AddBtnListener implements View.OnClickListener
		{
		@Override
		public void onClick(View v)
			{
			AddNoteDialog dialog= new AddNoteDialog();
			dialog.init(AddTemplateNoteActivity.this,(ViewGroup)v.getParent(),O.actions.ACTION_ADD_TEMPLATE_NOTE_ACTIVITY,templateId,"Добавить продукт");
			dialog.createAndShow();
			}
		}

	/**
	 * @param parent нужен для диалога
	 * @param noteId запись, которую надо изменить
	 * @param templateId шаблон, которому принадлежит запись
	 */
	@Override
	public void initNoteFragment(ViewGroup parent,long noteId,long templateId)
		{
		AddNoteDialog dialog= new AddNoteDialog();
		dialog.init(AddTemplateNoteActivity.this,parent,O.actions.ACTION_ADD_TEMPLATE_NOTE_ACTIVITY,"Изменить продукт",noteId,templateId);
		dialog.createAndShow();
		}
	private void initAdapter()
		{
		TemplatesNotesAdapter adapter= new TemplatesNotesAdapter(templateId,this);
		list.setAdapter(adapter);
		}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
		{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gts_layout);

		templateId= getIntent().getLongExtra(O.mapKeys.extra.TEMPLATE_ID,0);

		list= (RecyclerView)findViewById(R.id.list);
		Button btnAdd= (Button)findViewById(R.id.btn_add);

		list.setLayoutManager(new LinearLayoutManager(this) );
		btnAdd.setText("Добавить продукт");
		btnAdd.setOnClickListener(new AddBtnListener() );
		receiver= new Receiver();
		IntentFilter filter= new IntentFilter(O.actions.ACTION_ADD_TEMPLATE_NOTE_ACTIVITY);
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
