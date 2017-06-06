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
import com.ultra.shopperlights2.Adapters.GTSAdapter;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.Fragments.AddGroupDialog;
import com.ultra.shopperlights2.Fragments.AddShopDialog;
import com.ultra.shopperlights2.Fragments.AddTagDialog;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.GroupDao;
import com.ultra.shopperlights2.Units.GreenRecyclerListElement;
import com.ultra.shopperlights2.Utils.O;

import java.util.ArrayList;

/**
 * <p></p>
 * <p><sub>(06.05.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class GTSListActivity extends AppCompatActivity
	{
	private RecyclerView recyclerList;
	private int type;
	private BroadcastReceiver receiver;

	private class Receiver extends BroadcastReceiver
		{
		@Override
		public void onReceive(Context context,Intent intent)
			{
			updateLists();
			}
		}
	private class AddListener implements View.OnClickListener
		{
		@Override
		public void onClick(View v)
			{
			switch(type)
				{
				case O.interaction.ELEMENT_TYPE_GROUP:
					{
					AddGroupDialog dialog= new AddGroupDialog();
					dialog.init(O.actions.ACTION_EDIT_LIST_ACTIVITY,"Создать группу");
					FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
					dialog.show(transaction,"");
					break;
					}
				case O.interaction.ELEMENT_TYPE_TAG:
					{
					AddTagDialog dialog= new AddTagDialog();
					dialog.init(O.actions.ACTION_EDIT_LIST_ACTIVITY,"Создать тег");
					FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
					dialog.show(transaction,"");
					break;
					}
				case O.interaction.ELEMENT_TYPE_SHOP:
					{
					AddShopDialog dialog= new AddShopDialog();
					dialog.init(O.actions.ACTION_EDIT_LIST_ACTIVITY,"Создать магазин");
					FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
					dialog.show(transaction,"");
					break;
					}
				}
			}
		}

	public void updateLists()
		{
		GTSAdapter adapter= new GTSAdapter(this,getList(),O.actions.ACTION_EDIT_LIST_ACTIVITY,getSupportFragmentManager() );
		recyclerList.setAdapter(adapter);
		recyclerList.setLayoutManager(new LinearLayoutManager(this) );
		}
	private ArrayList<GreenRecyclerListElement> getList()
		{
		ArrayList<GreenRecyclerListElement> result= new ArrayList<>();
		switch(type)
			{
			case O.interaction.ELEMENT_TYPE_GROUP:
				result.addAll(App.session.getGroupDao().queryBuilder().orderAsc(GroupDao.Properties.Priority).list() );
				break;
			case O.interaction.ELEMENT_TYPE_TAG:
				result.addAll(App.session.getTagDao().loadAll() );
				break;
			case O.interaction.ELEMENT_TYPE_SHOP:
				result.addAll(App.session.getShopDao().loadAll() );
				break;
			}
		return result;
		}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
		{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_list_layout);

		type= getIntent().getIntExtra(O.mapKeys.extra.LIST_ELEMENT_TYPE, O.interaction.ELEMENT_TYPE_GROUP);

		recyclerList= (RecyclerView)findViewById(R.id.list);
		Button btn= (Button)findViewById(R.id.btn_add);

		updateLists();
		switch(type)
			{
			case O.interaction.ELEMENT_TYPE_GROUP:
				btn.setText("Добавить группу");
				break;
			case O.interaction.ELEMENT_TYPE_TAG:
				btn.setText("Добавить тег");
				break;
			case O.interaction.ELEMENT_TYPE_SHOP:
				btn.setText("Добавить магазин");
				break;
			}
		btn.setOnClickListener(new AddListener() );
		receiver= new Receiver();
		IntentFilter filter= new IntentFilter(O.actions.ACTION_EDIT_LIST_ACTIVITY);
		registerReceiver(receiver,filter);
		}
	@Override
	protected void onDestroy()
		{
		super.onDestroy();
		unregisterReceiver(receiver);
		}
	}
