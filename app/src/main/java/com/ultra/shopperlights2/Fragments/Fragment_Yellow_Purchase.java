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
import com.ultra.shopperlights2.Callbacks.*;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.*;
import com.ultra.shopperlights2.Utils.Calc;
import com.ultra.shopperlights2.Utils.ConfirmDialog;
import com.ultra.shopperlights2.Utils.DateUtil;
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
public class Fragment_Yellow_Purchase extends Fragment implements YellowScreenDelElement,InitDialogFragment,DialogDecision
	 {
	 private ChangeYellowFragmentCallback callback;
	 private YellowPurchaseListAdapter adapter;
	 private Drawer drawer;
	 private Spinner inputSrcType,inputSrc;
	 private Button btnAddNote;
	 private TextView statusTxt,totalPriceTxt,srcTxt;
	 private RecyclerView recyclerList;
	 private long purchaseId;
	 private HashMap<String,Boolean> mapUsed, mapEthereal;
	 private BroadcastReceiver receiver;
	 private static int lastTimeChoise,lastTimeSrcType= O.interaction.SRC_TYPE_CODE_GROUPS;
	 private String srcTypes[]= {"группы","шаблоны","история"};
	 private Template tempTemplate;

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
			 if(!note.isEthereal() )
				 mapUsed.put(note.getTitle(),true);
			 else
				 mapEthereal.put(note.getTitle(),true);
			 drawer.removeItem(drawerItemId);
			 drawer.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
			 initStatusTxt();
			 return true;
			 }
		 }
	 private class SrcTypeSelectListener implements AdapterView.OnItemSelectedListener
		 {
		 @Override
		 public void onItemSelected(AdapterView<?> parent,View view,int position,long id)
			 {
			 lastTimeSrcType=position;
			 lastTimeChoise=0;
			 switch(lastTimeSrcType)
				 {
				 case O.interaction.SRC_TYPE_CODE_GROUPS:
					 srcTxt.setText("группы");
					 btnAddNote.setVisibility(View.VISIBLE);
					 break;
				 case O.interaction.SRC_TYPE_CODE_TEMPLATES:
					 srcTxt.setText("шаблоны");
					 btnAddNote.setVisibility(View.INVISIBLE);
					 break;
				 case O.interaction.SRC_TYPE_CODE_HISTORY:
					 srcTxt.setText("история");
					 btnAddNote.setVisibility(View.INVISIBLE);
					 break;
				 }
			 initSrcSpinner();
			 }
		 @Override
		 public void onNothingSelected(AdapterView<?> parent) {}
		 }
	 private class SrcSelectListener implements AdapterView.OnItemSelectedListener
		 {
		 @Override
		 public void onItemSelected(AdapterView<?> parent,View view,int position,long id)
			 {
			 Log.d(O.TAG,"SrcSelectListener.onItemSelected: ");
			 lastTimeChoise=position;
			 selectSrcSpinner();
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
				 if(mapEthereal.containsKey(note.getTitle() ) )
					 continue;
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
			 clearTempTemplate();
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
		 clearTempTemplate();
		 callback.changeYellowFragment(false);
		 drawer.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		 }
	 private void clearTempTemplate()
		 {
		 for(Note note : tempTemplate.getNotes())
			 App.session.getNoteDao().delete(note);
		 tempTemplate.getNotes().clear();
		 }
	 @Override
	 public void delElement(String title)
		 {
		 Note note= App.session.getNoteDao().queryBuilder().where(NoteDao.Properties.Title.eq(title),NoteDao.Properties.Locked.eq(true) ).list().get(0);
		 note.setLocked(false);
		 App.session.getNoteDao().update(note);
		 if(!note.isEthereal() )
			 mapUsed.put(title,false);
		 else
			 mapEthereal.put(title,false);
		 }
	 @Override
	 public void initDialog(long id)
		 {
		 EditProductDialog dialog= new EditProductDialog();
		 dialog.init(O.actions.ACTION_FRAGMENT_YELLOW_PURCHASE,id);
		 FragmentTransaction transaction= getFragmentManager().beginTransaction();
		 dialog.show(transaction,"");
		 }
	 private ArrayList<String> getSources()
		 {
		 ArrayList<String> result= new ArrayList<>();
		 switch(lastTimeSrcType)
			 {
			 case O.interaction.SRC_TYPE_CODE_GROUPS:
				 for(Group group : App.session.getGroupDao().queryBuilder().orderAsc(GroupDao.Properties.Priority).list())
					 result.add(group.getTitle() );
				 result.add("");
				 break;
			 case O.interaction.SRC_TYPE_CODE_TEMPLATES:
				 for(Template template : App.session.getTemplateDao().loadAll() )
					 {
					 if(template.getTitle().equals(O.TEMP_TEMPLATE_NAME) )
						 continue;
					 result.add(template.getTitle() );
					 }
				 break;
			 case O.interaction.SRC_TYPE_CODE_HISTORY:
				 List<Purchase> purchases= App.session.getPurchaseDao().queryBuilder().orderDesc(PurchaseDao.Properties.Date).limit(50).offset(1).list();
				 int i=0;
				 for(Purchase purchase : purchases)
					 {
					 Log.d(O.TAG,"getSources: purchase.id="+ purchase.getId() );
					 if(purchase.getProducts().size() == 0)
						 continue;
					 long id=purchase.getId();
					 String shop;
					 long shopId=purchase.getShopId();
					 if(shopId!=0)
						 shop=App.session.getShopDao().load(shopId).getTitle();
					 else
						 shop="Unknown";
					 String date=DateUtil.getDateStr(purchase.getDate() );
					 result.add(id +" "+ shop +" ("+ date +")");
					 i++;
					 if(i==10)
						 break;
					 }
				 break;
			 }
		 return result;
		 }
	 public void updateLists()
		 {
		 initAdapter();
		 initStatusTxt();
		 selectSrcSpinner();
		 List<Product> productsCompleted= App.session.getProductDao().queryBuilder().where(ProductDao.Properties.PurchaseId.eq(purchaseId),
				 ProductDao.Properties.Complete.eq(true) ).list();
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
	 private void initStatusTxt()
		 {
		 List<Product> productsAll= App.session.getProductDao().queryBuilder().where(ProductDao.Properties.PurchaseId.eq(purchaseId) ).list();
		 List<Product> productsCompleted= App.session.getProductDao().queryBuilder().where(ProductDao.Properties.PurchaseId.eq(purchaseId),
				 ProductDao.Properties.Complete.eq(true) ).list();
		 statusTxt.setText(productsCompleted.size() +"/"+ productsAll.size() );
		 }
	 private void initSrcTypeSpinner()
		 {
		 if(inputSrcType!=null)
			 {
			 ArrayAdapter<String> adapterSrcTypes= new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,srcTypes);
			 adapterSrcTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			 inputSrcType.setAdapter(adapterSrcTypes);
			 }
		 }
	 private void initSrcSpinner()
		 {
		 if(inputSrc!=null)
			 {
			 ArrayList<String> sources= getSources();
			 ArrayAdapter<String> adapterSources= new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,sources);
			 adapterSources.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			 inputSrc.setSelection(0);
			 inputSrc.setAdapter(adapterSources);
			 }
		 }
	 private void selectSrcSpinner()
		 {
		 if(inputSrc!=null)
			 {
			 Log.d(O.TAG,"selectSrcSpinner: lastTimeChoise="+ lastTimeChoise +"\tlastTimeSrcType="+ lastTimeSrcType);
//			 if(lastTimeSrcType!=0)
				 inputSrcType.setSelection(lastTimeSrcType);
//			 if(lastTimeChoise!=0)
				 inputSrc.setSelection(lastTimeChoise);
			 ArrayList<Note> notes= new ArrayList<>();
			 String selectedItem=inputSrc.getSelectedItem().toString();
			 switch(lastTimeSrcType)
				 {
				 case O.interaction.SRC_TYPE_CODE_GROUPS:
					 if(selectedItem.length() == 0)
						 notes.addAll(App.session.getNoteDao().queryBuilder().where(NoteDao.Properties.GroupId.eq(0),
								 		NoteDao.Properties.Ethereal.eq(false) ).list() );
					 else
						 {
						 Group group= App.session.getGroupDao().queryBuilder().where(GroupDao.Properties.Title.eq(selectedItem) )
								 	.list().get(0);
						 notes.addAll(group.getNotes() );
						 }
					 break;
				 case O.interaction.SRC_TYPE_CODE_TEMPLATES:
					 notes.addAll(App.session.getTemplateDao().queryBuilder().where(TemplateDao.Properties.Title.eq(selectedItem) )
							 		.list().get(0).getNotes() );
					 break;
				 case O.interaction.SRC_TYPE_CODE_HISTORY:
					 long selectedPurchaseId= Long.parseLong(selectedItem.split(" ")[0] );
					 for(Product product : App.session.getPurchaseDao().load(selectedPurchaseId).getProducts() )
						 {
						 Note note= new Note();
						 note.setTitle(product.getTitle() );
						 note.setN(product.getN() );
						 note.setEthereal(true);
						 tempTemplate.getNotes().add(note);
						 note.setTemplateId(tempTemplate.getId() );
						 note.setProductId(product.getId() );
						 App.session.getNoteDao().insert(note);
						 Log.d(O.TAG,"selectSrcSpinner: notes="+ notes);
						 notes.add(note);
						 }
					 break;
				 }
			 initDrawerList(notes);
			 }
		 }
	 private void initDrawerList(ArrayList<Note> src)
		{
		 drawer.removeAllItems();
		 for(Note note : src)
			 {
			 if(mapUsed.containsKey(note.getTitle() ) && mapUsed.get(note.getTitle() ) )
				 continue;
			 if(mapEthereal.containsKey(note.getTitle() ) && mapEthereal.get(note.getTitle() ) )
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
	 private void initMaps()
		 {
		 mapUsed= new HashMap<>();
		 mapEthereal= new HashMap<>();
		 ArrayList<Note> notes=new ArrayList<>(App.session.getNoteDao().loadAll());
		 for(Note note : notes)
			 if(!note.isEthereal() )
				 mapUsed.put(note.getTitle(),false);
			 else
				 mapEthereal.put(note.getTitle(),false);
		 }
	 private void initTempTemplate()
		 {
		 List<Template> TTList=App.session.getTemplateDao().queryBuilder().where(TemplateDao.Properties.Title.eq(O.TEMP_TEMPLATE_NAME)).list();
		 if(TTList.size()==0)
			 {
			 tempTemplate= new Template();
			 tempTemplate.setTitle(O.TEMP_TEMPLATE_NAME);
			 App.session.getTemplateDao().insert(tempTemplate);
			 Log.d(O.TAG,"initAdapter: tempTemplate created");
			 }
		 else
			 tempTemplate= TTList.get(0);
		 }
	 private void initAdapter()
		 {
		 List<Product> products= App.session.getProductDao().queryBuilder().where(ProductDao.Properties.PurchaseId.eq(purchaseId)).list();
		 for(Product product : products)
			 if(!product.isEthereal() )
				 {
				 if(mapUsed.containsKey(product.getTitle() ) )
					 mapUsed.put(product.getTitle(),true);
				 }
			 else
				 {
				 if(mapEthereal.containsKey(product.getTitle() ) )
					 mapEthereal.put(product.getTitle(),true);
				 }
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
		 initMaps();
		 initTempTemplate();
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
			 btnAddNote= (Button)header.findViewById(R.id.btn_add);
			 inputSrcType= (Spinner)header.findViewById(R.id.srcTypeInput);
			 inputSrc= (Spinner)header.findViewById(R.id.srcInput);
			 srcTxt= (TextView)header.findViewById(R.id.srcTxt);

			 btnAddNote.setOnClickListener(new DrawerHeaderClickListener() );
			 drawerClose.setOnClickListener(new DrawerHeaderClickListener() );
			 drawer.setOnDrawerItemClickListener(new DrawerItemClickListener() );
			 inputSrc.setOnItemSelectedListener(new SrcSelectListener() );
			 inputSrcType.setOnItemSelectedListener(new SrcTypeSelectListener() );
			 initSrcTypeSpinner();
			 initSrcSpinner();
			 selectSrcSpinner();
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
		 Log.d(O.TAG,"onResume: ");
		 updateLists();
		 }
	 @Override
	 public void onDestroy()
		 {
		 super.onDestroy();
		 getContext().unregisterReceiver(receiver);
		 }
	 }
