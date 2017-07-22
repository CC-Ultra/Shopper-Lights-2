package com.ultra.shopperlights2.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.ultra.shopperlights2.Dialogs.AddNoteDialog;
import com.ultra.shopperlights2.Dialogs.EditProductDialog;
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
 * <p>Фрагмент желтого экрана в режиме покупки</p>
 * <p><sub>(07.08.2016)</sub></p>
 * @author CC-Ultra
 */
public class Fragment_Yellow_Purchase extends Fragment implements YellowScreenDelElement,EditProductCallback,DialogDecision
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
	 private static int lastTimeSrcType= O.interaction.SRC_TYPE_CODE_GROUPS;
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
			 { //получаю список незаполненных продуктов
			 List<Product> products= App.session.getProductDao().queryBuilder().where(ProductDao.Properties.PurchaseId.eq(purchaseId),
																				ProductDao.Properties.Complete.eq(false)).list();
			 if(v.getId()==R.id.completePurchase && products.size() != 0)
				 Toast.makeText(getContext(),"Есть незаполненные продукты: "+ products.size() +" шт",Toast.LENGTH_SHORT).show();
			 //разница в кнопках открывается в реализации DialogDecision. В параметрах передаю id нажатой кнопки
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
				 dialog.init(getContext(),(ViewGroup)v.getParent(),O.actions.ACTION_FRAGMENT_YELLOW_PURCHASE,"Добавить продукт");
				 dialog.createAndShow();
				 }
			 }
		 }
	 private class DrawerButtonClickListener implements View.OnClickListener
		{
		 @Override
		 public void onClick(View v)
			{
			 drawer.openDrawer();
			 }
		 }
	 private class DrawerItemClickListener implements Drawer.OnDrawerItemClickListener
		{
		 @Override
		 public boolean onItemClick(View view,int position,IDrawerItem drawerItem)
			{
			 long drawerItemId= drawerItem.getIdentifier(); //по этому его удалять из списка drawer-а
			 drawer.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN); //чтобы не автозакрывался
			//в теге хранится id
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
			 int srcN= getSources().size();
			 if(inputSrc.getSelectedItemPosition()>srcN) //чтобы inputSrc всегда указывал на существующую позицию
				 inputSrc.setSelection(srcN);
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
			 List<Product> products= purchase.getProducts();
			 if(products.size()==0)
				 {
				 session.getPurchaseDao().delete(purchase);
				 Toast.makeText(getContext(),"Пустая покупка, удалено",Toast.LENGTH_SHORT).show();
				 return;
				 }
			 float totalPrice=0;
			 for(Product product : products)
				 totalPrice+= product.getPrice() * (product.getN()==0 ? 1 : product.getN() );
			 Toast.makeText(getContext(),"Оплачено: "+ Calc.round(totalPrice),Toast.LENGTH_SHORT).show();
			 purchase.setPrice(totalPrice);
			 session.getPurchaseDao().update(purchase);
			 //удаление связей записи. Прохожусь по замкнутым записям, удаляю все TagToNote связи и упоминания в группах
			 for(Note note : session.getNoteDao().queryBuilder().where(NoteDao.Properties.Locked.eq(true) ).list() )
				 {
				 if(!mapUsed.containsKey(note.getTitle() ) && mapEthereal.containsKey(note.getTitle() ) )
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
			 //некоторые обязательные действия
			 clearTempTemplate();
			 callback.changeYellowFragment(false);
			 drawer.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
			 }
		 }
	 private void cancelPurchase()
		 {
		 DaoSession session= App.session;
		 List<Product> products= session.getProductDao().queryBuilder().where(ProductDao.Properties.PurchaseId.eq(purchaseId) ).list();
		 //удаляю все связанные с покупкой продукты
		 for(Product product : products)
			 session.getProductDao().delete(product);
		 //удаляю покупку
		 Purchase purchase= session.getPurchaseDao().load(purchaseId);
		 session.getPurchaseDao().delete(purchase);
		 //разблокирую все записи
		 List<Note> notes= session.getNoteDao().queryBuilder().where(NoteDao.Properties.Locked.eq(true) ).list();
		 for(Note note : notes)
			 {
			 note.setLocked(false);
			 session.getNoteDao().update(note);
			 }
		 //обязательные действия
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

	 /**
	  * определение интерфейса {@link YellowScreenDelElement}
	  * @param title наименование удаляемого продукта-записи
	  */
	 @Override
	 public void delElement(String title)
		 {
		 Note note= App.session.getNoteDao().queryBuilder().where(NoteDao.Properties.Title.eq(title),
				 										NoteDao.Properties.Locked.eq(true) ).list().get(0);
		 note.setLocked(false);
		 App.session.getNoteDao().update(note);
		 if(!note.isEthereal() )
			 mapUsed.put(title,false);
		 else
			 mapEthereal.put(title,false);
		 }

	 /**
	  * определение интерфейса {@link EditProductCallback}
	  * @param parent нужен для диалога
	  * @param id изменяемый продукт
	  */
	 @Override
	 public void initDialog(ViewGroup parent,long id)
		 {
		 EditProductDialog dialog= new EditProductDialog();
		 dialog.init(getContext(),parent,O.actions.ACTION_FRAGMENT_YELLOW_PURCHASE,id);
		 dialog.createAndShow();
		 }

	 /**
	  * @return Список источников по известной переменной lastTimeSrcType
	  */
	 private ArrayList<String> getSources()
		 {
		 ArrayList<String> result= new ArrayList<>();
		 switch(lastTimeSrcType)
			 {
			 case O.interaction.SRC_TYPE_CODE_GROUPS:
			 	//все группы, а потом ""
				 for(Group group : App.session.getGroupDao().queryBuilder().orderAsc(GroupDao.Properties.Priority).list())
					 result.add(group.getTitle() );
				 result.add("");
				 break;
			 case O.interaction.SRC_TYPE_CODE_TEMPLATES:
				 for(Template template : App.session.getTemplateDao().loadAll() )
					 { //все шаблоны кроме временного
					 if(template.getTitle().equals(O.TEMP_TEMPLATE_NAME) )
						 continue;
					 result.add(template.getTitle() );
					 }
				 break;
			 case O.interaction.SRC_TYPE_CODE_HISTORY:
			 	//гружу до 50 покупок из истории по убыванию даты
				 List<Purchase> purchases= App.session.getPurchaseDao().queryBuilder().
						 										orderDesc(PurchaseDao.Properties.Date).limit(50).list();
				 int i=0;
				 for(Purchase purchase : purchases)
					 {
					 if(purchase.getProducts().size() == 0) //если это транспорт
						 continue;
					 long id=purchase.getId();
					 String shop;
					 long shopId=purchase.getShopId();
					 if(shopId!=0)
						 shop= App.session.getShopDao().load(shopId).getTitle();
					 else
						 shop="неизвестный";
					 String date= DateUtil.getDateStr(purchase.getDate() );
					 result.add(id +" "+ shop +" ("+ date +")");
					 i++;
					 if(i==10) //не больше 10 покупок на спиннер
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
			 inputSrc.setAdapter(adapterSources);
			 }
		 }

	 /**
	  * получить список записей и инициализировать им список drawer-а
	  */
	 private void selectSrcSpinner()
		 {
		 if(inputSrc!=null)
			 {
			 inputSrcType.setSelection(lastTimeSrcType);
 			 ArrayList<Note> notes= new ArrayList<>();
			 if(inputSrc.getSelectedItem()==null)
				 return;
			 String selectedItem=inputSrc.getSelectedItem().toString();
			 switch(lastTimeSrcType)
				 {
				 case O.interaction.SRC_TYPE_CODE_GROUPS:
					 if(selectedItem.length() == 0) //выбор вне группы
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
				 	//извлекаю из строки спиннера id покупки
					 long selectedPurchaseId= Long.parseLong(selectedItem.split(" ")[0] );
					 for(Product product : App.session.getPurchaseDao().load(selectedPurchaseId).getProducts() )
						 {
						 Note note= new Note();
						 note.setTitle(product.getTitle() );
						 note.setN(product.getN() );
						 note.setEthereal(true); //эфирность
						 tempTemplate.getNotes().add(note); //добавляю в список временного шаблона
						 note.setTemplateId(tempTemplate.getId() );
						 note.setProductId(product.getId() ); //запоминаю id продукта, чтобы потом подгрузить информацию по нему
						 App.session.getNoteDao().insert(note);
						 notes.add(note);
						 }
					 break;
				 }
			 initDrawerList(notes);
			 }
		 }

	 /**
	  * Отобразить все пришедшие записи, которых нет в mapUsed и mapEthereal. id записи хранится в теге
	  */
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
	 private void initMaps()
		 {
		 mapUsed= new HashMap<>();
		 mapEthereal= new HashMap<>();
		 for(Note note : App.session.getNoteDao().loadAll() )
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
		 //прохожусь по всем продуктам покупки и пложительно отмечаю их на картах
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

		 purchaseId= App.session.getPurchaseDao().queryBuilder().where(PurchaseDao.Properties.Completed.eq(false) ).list().get(0).getId();
		 initMaps();
		 initTempTemplate();

		 ImageButton buttonOpen= (ImageButton)mainView.findViewById(R.id.btn_openDrawer);
		 recyclerList= (RecyclerView)mainView.findViewById(R.id.list);
		 Button btnComplete= (Button)mainView.findViewById(R.id.completePurchase);
		 ImageView btnCancel= (ImageView)mainView.findViewById(R.id.cancelPurchase);
		 statusTxt= (TextView)mainView.findViewById(R.id.completeStatus);
		 totalPriceTxt= (TextView)mainView.findViewById(R.id.totalPrice);

		 if(drawer!=null)
			 {
			 drawer.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
			 View header= drawer.getHeader();
			 ImageView drawerClose= (ImageView)header.findViewById(R.id.btn_close);
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
