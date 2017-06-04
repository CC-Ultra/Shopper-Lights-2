package com.ultra.shopperlights2.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.ultra.shopperlights2.Adapters.YellowPurchaseListAdapter;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.Callbacks.*;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.*;
import com.ultra.shopperlights2.Utils.Calc;
import com.ultra.shopperlights2.Utils.ConfirmDialog;
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
public class Fragment_Yellow_Purchase extends Fragment implements YellowScreenDelElement,YellowScreenInitDialogFragment,DialogDecision
	 {
	 private ChangeYellowFragmentCallback callback;
	 private YellowPurchaseListAdapter adapter;
	 private Drawer drawer;
	 private Spinner groupInput;
	 private TextView statusTxt,totalPriceTxt;
	 private RecyclerView recyclerList;
	 private long purchaseId;
	 private HashMap<String,Boolean> usedMap;
	 private BroadcastReceiver receiver;
	 private static String lastTimeGroup;

	 private class Receiver extends BroadcastReceiver
		 {
		 @Override
		 public void onReceive(Context context,Intent intent)
			 {
			 updateLists();
			 }
		 }
	 private class PurchaseButtonsListener implements View.OnClickListener
		 {
		 @Override
		 public void onClick(View v)
			 {
			 List<Product> products= App.session.getProductDao().queryBuilder().where(ProductDao.Properties.PurchaseId.eq(purchaseId),
					 ProductDao.Properties.Complete.eq(false)).list();
			 if(v.getId()==R.id.completePurchase && products.size() != 0)
				 Toast.makeText(getContext(),"Есть незаполненные продукты: "+ products.size() +" шт",Toast.LENGTH_SHORT).show();
			 ConfirmDialog.ask(getContext(),Fragment_Yellow_Purchase.this,v.getId(),0);
			 }
		 }

	 private class DrawerHeaderClickListener implements View.OnClickListener
		 {
		 @Override
		 public void onClick(View v)
			 {
			 if(v.getId() == R.id.btn_close)
				 {
				 drawer.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
				 drawer.closeDrawer();
				 updateLists();
				 }
			 else if(v.getId()==R.id.btn_add)
				 {
				 AddNoteDialog dialog= new AddNoteDialog();
				 dialog.init(O.actions.ACTION_FRAGMENT_YELLOW_PURCHASE,"Добавить продукт");
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
			 List<Product> productsAll= App.session.getProductDao().queryBuilder().where(ProductDao.Properties.PurchaseId.eq(purchaseId) ).list();
			 List<Product> productsCompleted= App.session.getProductDao().queryBuilder().where(ProductDao.Properties.PurchaseId.eq(purchaseId),
					ProductDao.Properties.Complete.eq(true) ).list();
			 statusTxt.setText(productsCompleted.size() +"/"+ productsAll.size() );
			 return true;
			 }
		 }
	 private class GroupSelectListener implements AdapterView.OnItemSelectedListener
		 {
		 @Override
		 public void onItemSelected(AdapterView<?> parent,View view,int position,long id)
			 {
			 selectGroupSpinner();
			 }
		 @Override
		 public void onNothingSelected(AdapterView<?> parent) {}
		 }

	 @Override
	 public void sayNo(int noId) {}
	 @Override
	 public void sayYes(int yesId)
		 {
		 if(yesId==R.id.completePurchase)
			 completePurchase();
		 else if(yesId==R.id.cancelPurchase)
			 cancelPurchase();
		 }
	 private void completePurchase()
		 {
		 try
			 {
			 DaoSession session= App.session;
			 Purchase purchase= session.getPurchaseDao().load(purchaseId);
			 purchase.setCompleted(true);
			 List<Product> products= session.getProductDao().queryBuilder().where(ProductDao.Properties.PurchaseId.eq(purchaseId)).list();
			 if(products.size()==0)
				 {
				 session.getPurchaseDao().delete(purchase);
				 Toast.makeText(getContext(),"Пустая покупка, удалено",Toast.LENGTH_SHORT).show();
				 return;
				 }
			 float totalPrice=0;
			 purchase.getProducts().clear();
			 for(Product product : products)
				 {
				 totalPrice+= product.getPrice() * (product.getN()==0 ? 1 : product.getN() );
				 purchase.getProducts().add(product);
				 }
			 Toast.makeText(getContext(),""+ Calc.round(totalPrice),Toast.LENGTH_SHORT).show();
			 purchase.setPrice(totalPrice);
			 session.getPurchaseDao().update(purchase);
			 for(Note note : session.getNoteDao().queryBuilder().where(NoteDao.Properties.Locked.eq(true) ).list() )
				 {
				 List<TagToNote> tagToNotes= session.getTagToNoteDao().queryBuilder().where(TagToNoteDao.Properties.NoteId.eq(note.getId() ) ).list();
				 for(TagToNote tagToNote : tagToNotes)
					 session.getTagToNoteDao().delete(tagToNote);
				 if(note.getGroupId()!=0)
					 {
					 Group group= session.getGroupDao().load(note.getGroupId() );
					 group.getNotes().remove(note);
					 session.getGroupDao().update(group);
					 }
				 session.getNoteDao().delete(note);
				 }
			 }
		 finally
			 {
			 callback.changeYellowFragment(false);
			 drawer.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
			 }
		 }
	 private void cancelPurchase()
		 {
		 DaoSession session= App.session;
		 List<Product> products= session.getProductDao().queryBuilder().where(ProductDao.Properties.PurchaseId.eq(purchaseId) ).list();
		 for(Product product : products)
			 session.getProductDao().delete(product);
		 Purchase purchase= session.getPurchaseDao().load(purchaseId);
		 session.getPurchaseDao().delete(purchase);
		 List<Note> notes= session.getNoteDao().queryBuilder().where(NoteDao.Properties.Locked.eq(true) ).list();
		 for(Note note : notes)
			 {
			 note.setLocked(false);
			 session.getNoteDao().update(note);
			 }
		 callback.changeYellowFragment(false);
		 drawer.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
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
	 public void initDialog(long id)
		 {
		 EditProductDialog dialog= new EditProductDialog();
		 dialog.init(O.actions.ACTION_FRAGMENT_YELLOW_PURCHASE,id);
		 FragmentTransaction transaction= getFragmentManager().beginTransaction();
		 dialog.show(transaction,"");
		 }
	 public void updateLists()
		 {
		 initMap();
		 ArrayList<String> groups= new ArrayList<>();
		 groups.add("");
		 DaoSession session= App.session;
		 for(Group group : session.getGroupDao().loadAll() )
			 groups.add(group.getTitle() );
		 ArrayAdapter<String> spinnerAdapter= new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,groups);
		 spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 if(groupInput!=null)
			{
			 groupInput.setAdapter(spinnerAdapter);
			 if(groups.contains(lastTimeGroup) )
				 groupInput.setSelection(groups.indexOf(lastTimeGroup) );
			 }
		 initAdapter();
		 selectGroupSpinner();
		 List<Product> productsAll= session.getProductDao().queryBuilder().where(ProductDao.Properties.PurchaseId.eq(purchaseId) ).list();
		 List<Product> productsCompleted= session.getProductDao().queryBuilder().where(ProductDao.Properties.PurchaseId.eq(purchaseId),
				 ProductDao.Properties.Complete.eq(true) ).list();
		 statusTxt.setText(productsCompleted.size() +"/"+ productsAll.size() );
		 float totalPrice=0;
		 for(Product product : productsCompleted)
			 totalPrice+= product.getPrice() * (product.getN()==0 ? 1 : product.getN() );
		 totalPriceTxt.setText(""+ Calc.round(totalPrice) );
		 }
	 public void initFragment(Drawer _drawer,ChangeYellowFragmentCallback _callback)
		{
		 callback=_callback;
		 drawer=_drawer;
		 }
	 private void selectGroupSpinner()
		 {
		 if(groupInput!=null)
			 {
			 lastTimeGroup= groupInput.getSelectedItem().toString();
			 if(lastTimeGroup.length()==0)
				 {
				 ArrayList<Note> notes= new ArrayList<>(App.session.getNoteDao().queryBuilder().where(NoteDao.Properties.GroupId.eq(0) ).list() );
				 initDrawerList(notes);
				 }
			 else
				 {
				 Group group= App.session.getGroupDao().queryBuilder().where(GroupDao.Properties.Title.eq(lastTimeGroup) ).list().get(0);
				 initDrawerList(new ArrayList<>(group.getNotes() ) );
				 }
			 }
		 }
	 private void initDrawerList(ArrayList<Note> src)
		{
		 drawer.removeAllItems();
		 for(Note note : src)
			 {
//			 if(usedMap == null)
//				 Log.d(TAG,"initDrawerList: usedMap");
//			 if(note==null)
//				 Log.d(TAG,"initDrawerList: note");
//			 if(usedMap.get(note.getTitle() )==null)
//				 Log.d(TAG,"initDrawerList: get");
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
		 purchaseId= App.session.getPurchaseDao().queryBuilder().where(PurchaseDao.Properties.Completed.eq(false) ).list().get(0).getId();
		 }
	 private void initMap()
		 {
		 usedMap= new HashMap<>();
		 ArrayList<Note> notes=new ArrayList<>(App.session.getNoteDao().loadAll());
		 for(Note note : notes)
			 usedMap.put(note.getTitle(),false);
		 }
	 private void initAdapter()
		 {
		 List<Product> products= App.session.getProductDao().queryBuilder().where(ProductDao.Properties.PurchaseId.eq(purchaseId)).list();
		 for(Product product : products)
			 if(usedMap.containsKey(product.getTitle() ) )
				 usedMap.put(product.getTitle(),true);
		 adapter= new YellowPurchaseListAdapter(getContext(),new ArrayList<>(products),purchaseId,O.actions.ACTION_FRAGMENT_YELLOW_PURCHASE,this,this);
		 recyclerList.setAdapter(adapter);
		 recyclerList.setLayoutManager(new LinearLayoutManager(getContext() ) );
		 }

	 @Nullable
	 @Override
	 public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
		{
		 View mainView= inflater.inflate(R.layout.yellow_screen,container,false);

		 getPurchaseId();
		 ImageButton buttonOpen= (ImageButton)mainView.findViewById(R.id.btn_openDrawer);
		 recyclerList= (RecyclerView)mainView.findViewById(R.id.list);
		 Button btnComplete= (Button)mainView.findViewById(R.id.completePurchase);
		 ImageButton btnCancel= (ImageButton)mainView.findViewById(R.id.cancelPurchase);
		 statusTxt= (TextView)mainView.findViewById(R.id.completeStatus);
		 totalPriceTxt= (TextView)mainView.findViewById(R.id.totalPrice);

		 if(drawer!=null)
			 {
			 drawer.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
			 View header= drawer.getHeader();
			 ImageButton drawerClose= (ImageButton)header.findViewById(R.id.btn_close);
			 Button btnAddNote= (Button)header.findViewById(R.id.btn_add);
			 groupInput= (Spinner)header.findViewById(R.id.groupInput);

			 btnAddNote.setOnClickListener(new DrawerHeaderClickListener() );
			 drawerClose.setOnClickListener(new DrawerHeaderClickListener() );
			 drawer.setOnDrawerItemClickListener(new DrawerItemClickListener() );
			 groupInput.setOnItemSelectedListener(new GroupSelectListener() );
//			 updateLists();
			 }
		 buttonOpen.setOnClickListener(new DrawerButtonClickListener() );
		 btnComplete.setOnClickListener(new PurchaseButtonsListener() );
		 btnCancel.setOnClickListener(new PurchaseButtonsListener() );
		 receiver= new Receiver();
		 IntentFilter filter= new IntentFilter(O.actions.ACTION_FRAGMENT_YELLOW_PURCHASE);
		 getContext().registerReceiver(receiver,filter);

		 return mainView;
		 }
	 @Override
	 public void onResume()
		 {
		 super.onResume();
		 updateLists();
		 }
	 @Override
	 public void onDestroy()
		 {
		 super.onDestroy();
		 getContext().unregisterReceiver(receiver);
		 }
	 }
