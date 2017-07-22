package com.ultra.shopperlights2.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.Shop;
import com.ultra.shopperlights2.Units.ShopDao;
import com.ultra.shopperlights2.Utils.O;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * <p>Класс-обертка над диалогом добавления/редактирования магазина</p>
 * <p><sub>(28.04.2017)</sub></p>
 * @author CC-Ultra
 */

public class AddShopDialog
	 {
	 private Context context;
	 private AlertDialog dialog;
	 private ViewGroup parent;
	 private String title;
	 private AutoCompleteTextView inputShopName, inputShopCity;
	 private EditText inputAdr;
	 private long shopId=0;
	 private String action;

	 private class OkListener implements View.OnClickListener
		 {
		 @Override
		 public void onClick(View v)
			 {
			 String shopName= inputShopName.getText().toString();
			 String shopCity= inputShopCity.getText().toString();
			 String shopAdr= inputAdr.getText().toString();
			 if(shopName.length() == 0 || shopAdr.length() == 0 || shopCity.length() == 0)
				 Toast.makeText(context,"Не все поля заполнены",Toast.LENGTH_SHORT).show();
			 else
				 { //все поля заполнены
				 Shop shop;
				 ShopDao shopDao= App.session.getShopDao();
				 if(shopId ==0)
					 shop= new Shop();
				 else
					 shop= shopDao.load(shopId);
				 //size - количество магазинов, совпадающих с введенными данными
				 int size= shopDao.queryBuilder().where(
														ShopDao.Properties.Title.eq(shopName),
														ShopDao.Properties.City.eq(shopCity),
														ShopDao.Properties.Adr.eq(shopAdr)
														).list().size();
				 if(shopId==0)
					 {
					 if(size>0) //добавление магазина и такой уже есть
						 {
						 inputShopName.setError("Это имя уже занято");
						 return;
						 }
					 }
				 else //изменение магазина
					 {
					 boolean a,b,c;
					 a= shop.getTitle().equals(shopName);
					 b= shop.getCity().equals(shopCity);
					 c= shop.getAdr().equals(shopAdr);
					 if(size>0 && (!a || !b || !c) ) //такой магазин есть в базе и он отличается от загруженного через id
						 {
						 inputShopName.setError("Это имя уже занято");
						 return;
						 }
					 }
				 shop.setTitle(shopName);
				 shop.setCity(shopCity);
				 shop.setAlive(true);
				 shop.setAdr(shopAdr);
				 if(shopId==0)
					 shopDao.insert(shop);
				 else
					 shopDao.update(shop);
				 dialog.dismiss();
				 context.sendBroadcast(new Intent(action) ); //чтобы обновить список в вызывающей активности/фрагменте
				 }
			 }
		 }

	 /**
	  * создать магазин
	  */
	 public void init(Context _context,ViewGroup _parent,String _action,String _title)
		 {
		 context=_context;
		 parent=_parent;
		 action=_action;
		 title=_title;
		 }

	 /**
	  * редактировать магазин
	  */
	 public void init(Context _context,ViewGroup _parent,String _action,String _title,long _id)
		 {
		 context=_context;
		 parent=_parent;
		 action=_action;
		 shopId=_id;
		 title=_title;
		 }

	 /**
	  * Получение списка магазинов
	  */
	 private ArrayList<String> getShopStrs()
		 {
		 ArrayList<String> result= new ArrayList<>();
		 List<Shop> shopList= App.session.getShopDao().loadAll();
		 TreeSet<String> strings= new TreeSet<>();
		 for(Shop shop : shopList)
			 strings.add(shop.getTitle() );
		 result.addAll(strings);
		 return result;
		 }

	 /**
	  * Получение списка городов
	  */
	 private ArrayList<String> getCityStrs()
		 {
		 ArrayList<String> result= new ArrayList<>();
		 List<Shop> shopList= App.session.getShopDao().loadAll();
		 TreeSet<String> strings= new TreeSet<>();
		 for(Shop shop : shopList)
			 strings.add(shop.getCity() );
		 result.addAll(strings);
		 return result;
		 }

	 /**
	  * Инициализация mainView и передача ее диалогу. Попутно инициализация адаптеров автодополнения, установка Listener-ов и т.д.
	  */
	 public void createAndShow()
		 {
		 View mainView= LayoutInflater.from(context).inflate(R.layout.add_shop_dialog_layout,parent,false);
		 AlertDialog.Builder builder= new AlertDialog.Builder(context);
		 builder.setTitle(title);

		 Button okBtn= (Button)mainView.findViewById(R.id.btnOk);
		 inputShopName= (AutoCompleteTextView) mainView.findViewById(R.id.autoTitle);
		 inputShopCity= (AutoCompleteTextView)mainView.findViewById(R.id.autoCity);
		 inputAdr= (EditText)mainView.findViewById(R.id.adrInput);

		 ArrayAdapter<String> adapterTitle= new ArrayAdapter<>(context,android.R.layout.simple_dropdown_item_1line,getShopStrs() );
		 ArrayAdapter<String> adapterCity= new ArrayAdapter<>(context,android.R.layout.simple_dropdown_item_1line,getCityStrs() );
		 inputShopName.setAdapter(adapterTitle);
		 inputShopCity.setAdapter(adapterCity);

		 if(shopId!=0)
			 {
			 Shop shop= App.session.getShopDao().load(shopId);
			 inputShopName.setText(shop.getTitle() );
			 inputShopCity.setText(shop.getCity() );
			 inputAdr.setText(shop.getAdr() );
			 }
		 okBtn.setOnClickListener(new OkListener() );

		 builder.setView(mainView);
		 dialog= builder.create();
		 dialog.show();
		 }
	 }
