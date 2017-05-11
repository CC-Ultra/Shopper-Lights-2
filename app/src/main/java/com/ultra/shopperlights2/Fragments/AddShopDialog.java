package com.ultra.shopperlights2.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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
 * <p></p>
 * <p><sub>(28.04.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class AddShopDialog extends DialogFragment
	 {
	 private String title;
	 private AutoCompleteTextView shopNameInput,shopCityInput;
	 private EditText adrInput;
	 private long shopId=0;
	 private String action;

	 private class OkListener implements View.OnClickListener
		 {
		 @Override
		 public void onClick(View v)
			 {
			 String shopName= shopNameInput.getText().toString();
			 String shopCity= shopCityInput.getText().toString();
			 String shopAdr= adrInput.getText().toString();
			 if(shopName.length() == 0 || shopAdr.length() == 0 || shopCity.length() == 0)
				 Toast.makeText(getContext(),"Не все поля заполнены",Toast.LENGTH_SHORT).show();
			 else
				 {
				 Shop shop;
				 ShopDao shopDao= App.session.getShopDao();
				 if(shopId ==0)
					 shop= new Shop();
				 else
					 shop= shopDao.load(shopId);
				 int size= shopDao.queryBuilder().where(
														ShopDao.Properties.Title.eq(shopName),
														ShopDao.Properties.City.eq(shopCity),
														ShopDao.Properties.Adr.eq(shopAdr)
														).list().size();
				 if(shopId==0)
					 {
					 if(size>0)
						 {
						 Toast.makeText(getContext(),"Это имя уже занято",Toast.LENGTH_SHORT).show();
						 return;
						 }
					 }
				 else
					 {
					 boolean a,b,c;
					 a= shop.getTitle().equals(shopName);
					 b= shop.getCity().equals(shopCity);
					 c= shop.getAdr().equals(shopAdr);
					 if(size>0 && (!a || !b || !c) )
						 {
						 Toast.makeText(getContext(),"Это имя уже занято",Toast.LENGTH_SHORT).show();
						 return;
						 }
					 }
				 shop.setTitle(shopName);
				 shop.setCity(shopCity);
				 shop.setAdr(shopAdr);
				 if(shopId==0)
					 shopDao.insert(shop);
				 else
					 shopDao.update(shop);
				 dismiss();
				 getContext().sendBroadcast(new Intent(action) );
				 }
			 }
		 }

	 public void init(String _action,String _title)
		 {
		 action=_action;
		 title=_title;
		 }
	 public void init(String _action,String _title,long _id)
		 {
		 action=_action;
		 shopId=_id;
		 title=_title;
		 }
	 private ArrayList<String> getShopStrs()
		 {
		 ArrayList result= new ArrayList();
		 List<Shop> shopList= App.session.getShopDao().loadAll();
		 TreeSet<String> strings= new TreeSet<>();
		 for(Shop shop : shopList)
			 strings.add(shop.getTitle() );
		 result.addAll(strings);
		 return result;
		 }
	 private ArrayList<String> getCityStrs()
		 {
		 ArrayList result= new ArrayList();
		 List<Shop> shopList= App.session.getShopDao().loadAll();
		 TreeSet<String> strings= new TreeSet<>();
		 for(Shop shop : shopList)
			 strings.add(shop.getCity() );
		 result.addAll(strings);
		 return result;
		 }

	 @Nullable
	 @Override
	 public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,Bundle savedInstanceState)
		 {
		 getDialog().setTitle(title);
		 View mainView= inflater.inflate(R.layout.add_shop_dialog_layout,container,false);
		 if(savedInstanceState!=null)
			 {
			 shopId= savedInstanceState.getLong(O.mapKeys.savedState.SAVED_STATE_SHOP_ID,0);
			 action= savedInstanceState.getString(O.mapKeys.savedState.SAVED_STATE_ACTION);
			 }

		 Button okBtn= (Button)mainView.findViewById(R.id.btnOk);
		 shopNameInput= (AutoCompleteTextView) mainView.findViewById(R.id.autoTitle);
		 shopCityInput= (AutoCompleteTextView)mainView.findViewById(R.id.autoCity);
		 adrInput= (EditText)mainView.findViewById(R.id.adrInput);

		 ArrayAdapter<String> adapterTitle= new ArrayAdapter<>(getContext(),android.R.layout.simple_dropdown_item_1line,getShopStrs() );
		 ArrayAdapter<String> adapterCity= new ArrayAdapter<>(getContext(),android.R.layout.simple_dropdown_item_1line,getCityStrs() );
		 shopNameInput.setAdapter(adapterTitle);
		 shopCityInput.setAdapter(adapterCity);

		 if(shopId!=0)
			 {
			 Shop shop= App.session.getShopDao().load(shopId);
			 shopNameInput.setText(shop.getTitle() );
			 shopCityInput.setText(shop.getCity() );
			 adrInput.setText(shop.getAdr() );
			 }
		 okBtn.setOnClickListener(new OkListener() );
		 return mainView;
		 }
	 @Override
	 public void onSaveInstanceState(Bundle outState)
		 {
		 if(shopId!=0)
			 outState.putLong(O.mapKeys.savedState.SAVED_STATE_SHOP_ID,shopId);
		 outState.putString(O.mapKeys.savedState.SAVED_STATE_ACTION,action);
		 super.onSaveInstanceState(outState);
		 }
	 }
