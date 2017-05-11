package com.ultra.shopperlights2.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.Callbacks.ChangeYellowFragmentCallback;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.Purchase;
import com.ultra.shopperlights2.Units.Shop;
import com.ultra.shopperlights2.Units.ShopDao;
import com.ultra.shopperlights2.Utils.O;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeSet;
import java.util.List;

/**
 * <p></p>
 * <p><sub>(09.05.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class Fragment_Yellow_Shop extends Fragment
	{
	private ChangeYellowFragmentCallback callback;
	private Spinner inputTitle,inputCity,inputAdr;
	private ArrayAdapter<String> adapterTitle,adapterCity,adapterAdr;
	private View mainView;
	private FragmentManager fragmentManager;
	private Button startBtn;
	private BroadcastReceiver receiver;

	private class Receiver extends BroadcastReceiver
		{
		@Override
		public void onReceive(Context context,Intent intent)
			{
			updateLists();
			}
		}
	private class StartPurchasesListener implements View.OnClickListener
		{
		@Override
		public void onClick(View v)
			{
			String titleStr= inputTitle.getSelectedItem().toString();
			String cityStr= inputCity.getSelectedItem().toString();
			String adrStr= inputAdr.getSelectedItem().toString();
			Shop shop= App.session.getShopDao().queryBuilder().where(
																	ShopDao.Properties.Title.eq(titleStr),
																	ShopDao.Properties.City.eq(cityStr),
																	ShopDao.Properties.Adr.eq(adrStr)
																	).list().get(0);
			Purchase purchase= new Purchase();
			purchase.setShopId(shop.getId() );
			purchase.setCompleted(false);
			purchase.setDate(new Date() );
			App.session.getPurchaseDao().insert(purchase);
			callback.changeYellowFragment(true);
			}
		}
	private class AddShopListener implements View.OnClickListener
		{
		@Override
		public void onClick(View v)
			{
			AddShopDialog dialog= new AddShopDialog();
			dialog.init(O.actions.ACTION_FRAGMENT_YELLOW_SHOP,"Создать магазин");
			FragmentTransaction transaction= fragmentManager.beginTransaction();
			dialog.show(transaction,"");
			}
		}
	private class TitleSelectListener implements AdapterView.OnItemSelectedListener
		{
		@Override
		public void onItemSelected(AdapterView<?> parent,View view,int position,long id)
			{
			ArrayList<String> adrs= new ArrayList<>();
			adrs.add("");
			adapterAdr.clear();
			adapterAdr.addAll(adrs);
			String selectedTitle= inputTitle.getSelectedItem().toString();
			if(selectedTitle.length() == 0)
				{
				ArrayList<String> cities= new ArrayList<>();
				cities.add("");
				adapterCity.clear();
				adapterCity.addAll(cities);
				}
			else
				{
				ArrayList<String> cities= new ArrayList<>();
				cities.add("");
				List<Shop> shops= App.session.getShopDao().queryBuilder().where(ShopDao.Properties.Title.eq(selectedTitle)).list();
				TreeSet<String> citySet= new TreeSet<>();
				for(Shop shop : shops)
					citySet.add(shop.getCity() );
				cities.addAll(citySet);
				adapterCity.clear();
				adapterCity.addAll(cities);
				}
			}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
		}
	private class CitySelectListener implements AdapterView.OnItemSelectedListener
		{
		@Override
		public void onItemSelected(AdapterView<?> parent,View view,int position,long id)
			{
			if(inputCity.getSelectedItem().toString().length() != 0)
				{
				String title= inputTitle.getSelectedItem().toString();
				String city= inputCity.getSelectedItem().toString();
				List<Shop> shops= App.session.getShopDao().queryBuilder().where(ShopDao.Properties.Title.eq(title),ShopDao.Properties.City.eq(city) ).list();
				ArrayList<String> adrs= new ArrayList<>();
				adrs.add("");
				TreeSet<String> adrSet= new TreeSet<>();
				for(Shop shop : shops)
					adrSet.add(shop.getAdr() );
				adrs.addAll(adrSet);
				adapterAdr.clear();
				adapterAdr.addAll(adrs);
				}
			else
				{
				ArrayList<String> adrs= new ArrayList<>();
				adrs.add("");
				adapterAdr.clear();
				adapterAdr.addAll(adrs);
				}
			}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
		}
	private class AdrSelectListener implements AdapterView.OnItemSelectedListener
		{
		@Override
		public void onItemSelected(AdapterView<?> parent,View view,int position,long id)
			{
			if(inputAdr.getSelectedItem().toString().length() != 0)
				startBtn.setEnabled(true);
			else
				startBtn.setEnabled(false);
			}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
		}

	public void initFragment(ChangeYellowFragmentCallback _callback,FragmentManager _fragmentManager)
		{
		fragmentManager=_fragmentManager;
		callback=_callback;
		}
	public void updateLists()
		{
		ArrayList<String> shops= new ArrayList<>();
		ArrayList<String> cities= new ArrayList<>();
		ArrayList<String> adrs= new ArrayList<>();
		shops.add("");
		cities.add("");
		adrs.add("");
		TreeSet<String> shopSet= new TreeSet<>();
		for(Shop shop : App.session.getShopDao().loadAll() )
			shopSet.add(shop.getTitle() );
		shops.addAll(shopSet);
		adapterTitle= new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,shops);
		adapterCity= new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,cities);
		adapterAdr= new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,adrs);
		inputTitle.setAdapter(adapterTitle);
		inputCity.setAdapter(adapterCity);
		inputAdr.setAdapter(adapterAdr);
		}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState)
		{
		mainView= inflater.inflate(R.layout.choose_shop_layout,container,false);

		inputTitle= (Spinner)mainView.findViewById(R.id.inputTitle);
		inputCity= (Spinner)mainView.findViewById(R.id.inputCity);
		inputAdr= (Spinner)mainView.findViewById(R.id.inputAdr);
		startBtn= (Button)mainView.findViewById(R.id.btnOk);
		Button addShopBtn= (Button)mainView.findViewById(R.id.btnAdd);

		startBtn.setEnabled(false);
		startBtn.setOnClickListener(new StartPurchasesListener() );
		addShopBtn.setOnClickListener(new AddShopListener() );
		inputTitle.setOnItemSelectedListener(new TitleSelectListener() );
		inputCity.setOnItemSelectedListener(new CitySelectListener() );
		inputAdr.setOnItemSelectedListener(new AdrSelectListener() );
		updateLists();
		adapterTitle.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterAdr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		receiver= new Receiver();
		IntentFilter filter= new IntentFilter(O.actions.ACTION_FRAGMENT_YELLOW_SHOP);
		getContext().registerReceiver(receiver,filter);

		return mainView;
		}

	@Override
	public void onDestroy()
		{
		super.onDestroy();
		getContext().unregisterReceiver(receiver);
		}
	}
