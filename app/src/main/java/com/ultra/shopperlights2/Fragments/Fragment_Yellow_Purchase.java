package com.ultra.shopperlights2.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.ultra.shopperlights2.Adapters.YellowPurchaseListAdapter;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.Callbacks.ChangeYellowFragmentCallback;
import com.ultra.shopperlights2.Callbacks.UpdateListCallback;
import com.ultra.shopperlights2.Callbacks.YellowScreenDelElement;
import com.ultra.shopperlights2.Callbacks.YellowScreenInitFragment;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.*;
import com.ultra.shopperlights2.Utils.O;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * <p></p>
 * <p><sub>(07.08.2016)</sub></p>
 *
 * @author CC-Ultra
 */
public class Fragment_Yellow_Purchase extends Fragment implements UpdateListCallback,YellowScreenDelElement,YellowScreenInitFragment
	 {
	 private ChangeYellowFragmentCallback callback;
	 private YellowPurchaseListAdapter adapter;
	 private Drawer drawer;
	 private Spinner groupInput;
	 private RecyclerView recyclerList;
	 private long purchaseId;
	 private HashMap<String,Boolean> usedMap;

	 private class DrawerHeaderClickListener implements View.OnClickListener
		 {
		 @Override
		 public void onClick(View v)
			 {
			 if(v.getId() == R.id.btn_close)
				 {
				 drawer.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
				 drawer.closeDrawer();
				 }
			 else if(v.getId()==R.id.btn_add)
				 {
				 AddNoteDialog dialog= new AddNoteDialog();
				 dialog.init(Fragment_Yellow_Purchase.this,"Добавить продукт");
				 FragmentTransaction transaction= getFragmentManager().beginTransaction();
				 dialog.show(transaction,"");
				 }
			 }
		 }
	 class DrawerButtonClickListener implements View.OnClickListener
		{
		 @Override
		 public void onClick(View v)
			{
			 drawer.openDrawer();
			 }
		 }
	 class DrawerItemClickListener implements Drawer.OnDrawerItemClickListener
		{
		 @Override
		 public boolean onItemClick(View view,int position,IDrawerItem drawerItem)
			{
			 long drawerItemId= drawerItem.getIdentifier();
			 drawer.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
			 Note note= App.session.getNoteDao().load(Long.parseLong(drawerItem.getTag().toString() ) );
			 note.setLocked(true);
			 App.session.getNoteDao().update(note);
			 adapter.addElement(note);
			 usedMap.put(note.getTitle(),true);
			 drawer.removeItem(drawerItemId);
			 drawer.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
			 return true;
			 }
		 }
	 private class GroupSelectListener implements AdapterView.OnItemSelectedListener
		 {
		 @Override
		 public void onItemSelected(AdapterView<?> parent,View view,int position,long id)
			 {
			 String groupStr= groupInput.getSelectedItem().toString();
			 if(groupStr.length()==0)
				 {
				 ArrayList<Note> notes= new ArrayList<>(App.session.getNoteDao().queryBuilder().where(NoteDao.Properties.GroupId.eq(0) ).list() );
				 initDrawerList(notes);
				 }
			 else
				 {
				 Group group= App.session.getGroupDao().queryBuilder().where(GroupDao.Properties.Title.eq(groupStr) ).list().get(0);
				 initDrawerList(new ArrayList<>(group.getNotes() ) );
				 }
			 }
		 @Override
		 public void onNothingSelected(AdapterView<?> parent) {}
		 }

	 @Override
	 public void delElement(String title)
		 {
		 Note note= App.session.getNoteDao().queryBuilder().where(NoteDao.Properties.Title.eq(title),NoteDao.Properties.Locked.eq(true) ).list().get(0);
		 note.setLocked(false);
		 App.session.getNoteDao().update(note);
		 usedMap.put(title,false);
		 }
	 @Override
	 public void initFragment(long id)
		 {
		 Toast.makeText(getContext(),""+ id,Toast.LENGTH_SHORT).show();;
		 }
	 @Override
	 public void updateLists()
		 {
		 ArrayList<String> groups= new ArrayList<>();
		 groups.add("");
		 for(Group group : App.session.getGroupDao().loadAll())
			 groups.add(group.getTitle() );
		 ArrayAdapter<String> spinnerAdapter= new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,groups);
		 spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 groupInput.setAdapter(spinnerAdapter);
		 }
	 public void initFragment(Drawer _drawer,ChangeYellowFragmentCallback _callback)
		{
		 callback=_callback;
		 drawer=_drawer;
		 Log.d(O.TAG,"initFragment: drawer="+ drawer.toString());
		 }
	 private void initDrawerList(ArrayList<Note> src)
		{
		 drawer.removeAllItems();
		 for(Note note : src)
			 {
			 if(usedMap.get(note.getTitle() ) )
				 continue;
			 PrimaryDrawerItem drawerItem= new PrimaryDrawerItem().withName(note.getTitle() );
			 drawerItem.withTag(note.getId() );
			 drawer.addItem(drawerItem);
			 }
		 drawer.deselect();
		 }
	 private void getPurchaseId()
		 {
		 purchaseId= App.session.getPurchaseDao().queryBuilder().where(PurchaseDao.Properties.Completed.eq(false)).list().get(0).getId();
		 }
	 private void initMap()
		 {
		 usedMap=new HashMap<>();
		 ArrayList<Note> notes=new ArrayList<>(App.session.getNoteDao().loadAll());
		 for(Note note : notes)
			 usedMap.put(note.getTitle(),false);
		 }
	 private void initAdapter()
		 {
		 List<Product> products=App.session.getProductDao().queryBuilder().where(ProductDao.Properties.PurchaseId.eq(purchaseId)).list();
		 for(Product product : products)
			 if(usedMap.containsKey(product.getTitle() ) )
				 usedMap.put(product.getTitle(),true);
		 adapter= new YellowPurchaseListAdapter(new ArrayList<>(products),purchaseId,this,this);
		 recyclerList.setAdapter(adapter);
		 recyclerList.setLayoutManager(new LinearLayoutManager(getContext() ) );
		 }

	 @Nullable
	 @Override
	 public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
		{
		 View mainView= inflater.inflate(R.layout.yellow_screen,container,false);

		 getPurchaseId();
		 initMap();
		 ImageButton buttonOpen= (ImageButton)mainView.findViewById(R.id.btn_openDrawer);
		 recyclerList= (RecyclerView)mainView.findViewById(R.id.list);
		 if(drawer!=null)
			 {
			 Log.d(O.TAG,"onCreateView: МЫ ВНУТРИ!");
			 View header= drawer.getHeader();
			 ImageButton drawerClose= (ImageButton)header.findViewById(R.id.btn_close);
			 Button btnAddNote= (Button)header.findViewById(R.id.btn_add);
			 groupInput= (Spinner)header.findViewById(R.id.groupInput);

			 btnAddNote.setOnClickListener(new DrawerHeaderClickListener() );
			 drawerClose.setOnClickListener(new DrawerHeaderClickListener() );
			 drawer.setOnDrawerItemClickListener(new DrawerItemClickListener() );
			 groupInput.setOnItemSelectedListener(new GroupSelectListener() );
			 updateLists();
			 }
		 buttonOpen.setOnClickListener(new DrawerButtonClickListener() );

		 return mainView;
		 }
	 @Override
	 public void onResume()
		 {
		 super.onResume();
		 initAdapter();
		 }
	 }
