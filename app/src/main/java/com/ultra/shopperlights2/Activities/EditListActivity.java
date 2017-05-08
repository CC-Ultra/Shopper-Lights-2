package com.ultra.shopperlights2.Activities;

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
import com.ultra.shopperlights2.Callbacks.UpdateListCallback;
import com.ultra.shopperlights2.Fragments.AddGroupDialog;
import com.ultra.shopperlights2.Fragments.AddShopDialog;
import com.ultra.shopperlights2.Fragments.AddTagDialog;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.GroupDao;
import com.ultra.shopperlights2.Units.RecyclerListElement;
import com.ultra.shopperlights2.Utils.O;

import java.util.ArrayList;

/**
 * <p></p>
 * <p><sub>(06.05.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class EditListActivity extends AppCompatActivity implements UpdateListCallback
	{
	private RecyclerView recyclerList;
	private int type;

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
					dialog.init(EditListActivity.this,"Создать группу");
					FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
					dialog.show(transaction,"");
					break;
					}
				case O.interaction.ELEMENT_TYPE_TAG:
					{
					AddTagDialog dialog= new AddTagDialog();
					dialog.init(EditListActivity.this,"Создать тег");
					FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
					dialog.show(transaction,"");
					break;
					}
				case O.interaction.ELEMENT_TYPE_SHOP:
					{
					AddShopDialog dialog= new AddShopDialog();
					dialog.init(EditListActivity.this,"Создать магазин");
					FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
					dialog.show(transaction,"");
					break;
					}
				}
			}
		}

	@Override
	public void updateList()
		{
		GTSAdapter adapter= new GTSAdapter(this,getList(),this,getSupportFragmentManager() );
		recyclerList.setAdapter(adapter);
		recyclerList.setLayoutManager(new LinearLayoutManager(this) );
		}
	private ArrayList<RecyclerListElement> getList()
		{
		ArrayList<RecyclerListElement> result= new ArrayList<>();
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

		updateList();
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
		}
	}