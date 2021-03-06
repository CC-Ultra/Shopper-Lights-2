package com.ultra.shopperlights2.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.Callbacks.ChangeYellowFragmentCallback;
import com.ultra.shopperlights2.Dialogs.AddShopDialog;
import com.ultra.shopperlights2.Dialogs.TransportDialog;
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
 * <p>Фрагмент желтого экрана в режиме выбора магазина</p>
 * <p><sub>(09.05.2017)</sub></p>
 * @author CC-Ultra
 */

public class Fragment_Yellow_Shop extends Fragment
	{
	private ChangeYellowFragmentCallback callback;
	private Spinner inputTitle,inputCity,inputAdr;
	private ArrayAdapter<String> adapterTitle,adapterCity,adapterAdr;
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

	/**
	 * здесь запуск транспортного диалога
	 */
	private class TransportListener implements View.OnClickListener
		{
		@Override
		public void onClick(View v)
			{
			TransportDialog dialog= new TransportDialog();
			dialog.init(getContext(),(ViewGroup)v.getParent() );
			dialog.createAndShow();
			}
		}

	/**
	 * старт режима покупки с неизвестным магазином
	 */
	private class QuickPurchasesListener implements View.OnClickListener
		{
		@Override
		public void onClick(View v)
			{
			Purchase purchase= new Purchase();
			purchase.setShopId(0);
			purchase.setCompleted(false);
			purchase.setDate(new Date() );
			App.session.getPurchaseDao().insert(purchase);
			callback.changeYellowFragment(true);
			}
		}

	/**
	 * нормальный вход в режим покупки
	 */
	private class StartPurchasesListener implements View.OnClickListener
		{
		@Override
		public void onClick(View v)
			{
			String titleStr= inputTitle.getSelectedItem().toString();
			String cityStr= inputCity.getSelectedItem().toString();
			String adrStr= inputAdr.getSelectedItem().toString();
			Shop shop= App.session.getShopDao().queryBuilder().where(
																	ShopDao.Properties.Alive.eq(true),
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

	/**
	 * Запуск диалога добавления магазина
	 */
	private class AddShopListener implements View.OnClickListener
		{
		@Override
		public void onClick(View v)
			{
			AddShopDialog dialog= new AddShopDialog();
			dialog.init(getContext(),(ViewGroup)v.getParent(),O.actions.ACTION_FRAGMENT_YELLOW_SHOP,"Создать магазин");
			dialog.createAndShow();
			}
		}
	private class CitySelectListener implements AdapterView.OnItemSelectedListener
		{
		@Override
		public void onItemSelected(AdapterView<?> parent,View view,int position,long id)
			{
			//чищу адаптер адресов и делаю disabled их спиннер
			ArrayList<String> adrs= new ArrayList<>();
			adrs.add("");
			inputAdr.setEnabled(false);
			adapterAdr.clear();
			adapterAdr.addAll(adrs);
			inputAdr.setSelection(0);
			inputTitle.setSelection(0);
			String selectedCity= inputCity.getSelectedItem().toString();
			if(selectedCity.length() == 0)
				{ //если выбрана пустота, адаптер названий тоже чищу
				ArrayList<String> titles= new ArrayList<>();
				titles.add("");
				adapterTitle.clear();
				adapterTitle.addAll(titles);
				inputTitle.setEnabled(false);
				}
			else
				{ //или инициализация адаптера названий
				ArrayList<String> titles= new ArrayList<>();
				titles.add("");
				inputTitle.setEnabled(true);
				List<Shop> shops= App.session.getShopDao().queryBuilder().where(ShopDao.Properties.Alive.eq(true),
															ShopDao.Properties.City.eq(selectedCity) ).list();
				TreeSet<String> titleSet= new TreeSet<>();
				for(Shop shop : shops)
					titleSet.add(shop.getTitle() );
				titles.addAll(titleSet);
				adapterTitle.clear();
				adapterTitle.addAll(titles);
				}
			}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
		}
	private class TitleSelectListener implements AdapterView.OnItemSelectedListener
		{
		@Override
		public void onItemSelected(AdapterView<?> parent,View view,int position,long id)
			{
			inputAdr.setSelection(0);
			if(inputTitle.getSelectedItem().toString().length() != 0)
				{ //инициализация адаптера адресов
				String title= inputTitle.getSelectedItem().toString();
				String city= inputCity.getSelectedItem().toString();
				List<Shop> shops= App.session.getShopDao().queryBuilder().where(ShopDao.Properties.Title.eq(title),
						ShopDao.Properties.Alive.eq(true),ShopDao.Properties.City.eq(city) ).list();
				inputAdr.setEnabled(true);
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
				{ //чищу адаптер адресов, если выбрано пустое название
				ArrayList<String> adrs= new ArrayList<>();
				inputAdr.setEnabled(false);
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
			if(inputAdr.getSelectedItem().toString().length() != 0) //здесь только блокировка/разблокировка "старт"-кнопки
				startBtn.setEnabled(true);
			else
				startBtn.setEnabled(false);
			}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
		}

	public void initFragment(ChangeYellowFragmentCallback _callback)
		{
		callback=_callback;
		}

	/**
	 * инициализация трех адаптеров, из которых адаптер города инициализируется городами, а остальные два будут проинициализированы
	 * позже
	 */
	public void updateLists()
		{
		ArrayList<String> shops= new ArrayList<>();
		ArrayList<String> cities= new ArrayList<>();
		ArrayList<String> adrs= new ArrayList<>();
		shops.add("");
		cities.add("");
		adrs.add("");
		TreeSet<String> citySet= new TreeSet<>();
		for(Shop shop : App.session.getShopDao().queryBuilder().where(ShopDao.Properties.Alive.eq(true) ).list() )
			citySet.add(shop.getCity() );
		cities.addAll(citySet);
		adapterCity= new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,cities);
		adapterTitle= new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,shops);
		adapterAdr= new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,adrs);
		inputTitle.setAdapter(adapterTitle);
		inputCity.setAdapter(adapterCity);
		inputAdr.setAdapter(adapterAdr);
		adapterTitle.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterCity.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapterAdr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState)
		{
		View mainView= inflater.inflate(R.layout.choose_shop_layout,container,false);

		inputTitle= (Spinner) mainView.findViewById(R.id.inputTitle);
		inputCity= (Spinner) mainView.findViewById(R.id.inputCity);
		inputAdr= (Spinner) mainView.findViewById(R.id.inputAdr);
		startBtn= (Button) mainView.findViewById(R.id.btnOk);
		Button btnAddShop= (Button) mainView.findViewById(R.id.btnAdd);
		Button btnQuickPurchase= (Button) mainView.findViewById(R.id.btnQuickPurchase);
		Button btnTransport= (Button) mainView.findViewById(R.id.btnTransport);

		startBtn.setEnabled(false);
		startBtn.setOnClickListener(new StartPurchasesListener() );
		btnAddShop.setOnClickListener(new AddShopListener() );
		btnQuickPurchase.setOnClickListener(new QuickPurchasesListener() );
		btnTransport.setOnClickListener(new TransportListener() );
		inputTitle.setOnItemSelectedListener(new TitleSelectListener() );
		inputCity.setOnItemSelectedListener(new CitySelectListener() );
		inputAdr.setOnItemSelectedListener(new AdrSelectListener() );

		updateLists();

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
