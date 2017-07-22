package com.ultra.shopperlights2.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ultra.shopperlights2.Adapters.EditPurchaseAdapter;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.Callbacks.EditPurchasePriceUpdate;
import com.ultra.shopperlights2.Callbacks.EditProductCallback;
import com.ultra.shopperlights2.Dialogs.EditProductDialog;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.Product;
import com.ultra.shopperlights2.Units.ProductDao;
import com.ultra.shopperlights2.Units.Purchase;
import com.ultra.shopperlights2.Units.Shop;
import com.ultra.shopperlights2.Utils.Calc;
import com.ultra.shopperlights2.Utils.DateUtil;
import com.ultra.shopperlights2.Utils.O;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Активность для редактирования покупки</p>
 * <p><sub>(11.06.2017)</sub></p>
 * @author CC-Ultra
 */

public class EditPurchaseActivity extends AppCompatActivity implements EditPurchasePriceUpdate,EditProductCallback
	{
	private RecyclerView productsList;
	private long purchaseId;
	private Purchase purchase;
	private TextView txtTotalPrice;
	private Receiver receiver;

	private class Receiver extends BroadcastReceiver
		{
		@Override
		public void onReceive(Context context,Intent intent)
			{
			updatePrice();
			initAdapter();
			}
		}

	/**
	 * @param parent нужен для диалога
	 * @param id изменяемый продукт
	 */
	@Override
	public void initDialog(ViewGroup parent,long id)
		{
		EditProductDialog dialog= new EditProductDialog();
		dialog.init(EditPurchaseActivity.this,parent,O.actions.ACTION_EDIT_PURCHASE_ACTIVITY,id,true);
		dialog.createAndShow();
		}
	@Override
	public void updatePrice()
		{
		float totalPrice=0;
		for(Product product : purchase.getProducts())
			totalPrice+= product.getPrice() * (product.getN()==0 ? 1 : product.getN() );
		purchase.setPrice(totalPrice);
		App.session.getPurchaseDao().update(purchase);
		txtTotalPrice.setText(""+ Calc.round(totalPrice) );
		}
	private void initAdapter()
		{
		List<Product> p= App.session.getProductDao().queryBuilder().where(ProductDao.Properties.PurchaseId.eq(purchaseId) ).
																			orderDesc(ProductDao.Properties.Price).list();
		ArrayList<Product> products= new ArrayList<>(p);
		EditPurchaseAdapter adapter= new EditPurchaseAdapter(this,products,this,this);
		productsList.setLayoutManager(new LinearLayoutManager(this) );
		productsList.setAdapter(adapter);
		}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
		{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_purchase_layout);

		purchaseId= getIntent().getLongExtra(O.mapKeys.extra.PURCHASE_ID,0);
		purchase= App.session.getPurchaseDao().load(purchaseId);

		productsList= (RecyclerView)findViewById(R.id.list);
		TextView txtDate= (TextView)findViewById(R.id.txtDate);
		TextView txtShopTitle= (TextView)findViewById(R.id.txtShopTitle);
		TextView txtShopAdr= (TextView)findViewById(R.id.txtShopAdr);
		txtTotalPrice= (TextView)findViewById(R.id.txtTotalPrice);

		txtDate.setText(DateUtil.getDateStr(purchase.getDate() ) );
		Shop shop= App.session.getShopDao().load(purchase.getShopId() );
		if(shop==null)
			{
			txtShopTitle.setText("Неизвестный магазин");
			txtShopAdr.setText("Неизвестно");
			}
		else
			{
			txtShopTitle.setText(shop.getTitle());
			txtShopAdr.setText(shop.getCity() +", "+ shop.getAdr() );
			}
		initAdapter();
		updatePrice();
		IntentFilter filter= new IntentFilter(O.actions.ACTION_EDIT_PURCHASE_ACTIVITY);
		receiver= new Receiver();
		registerReceiver(receiver,filter);
		}
	@Override
	protected void onDestroy()
		{
		super.onDestroy();
		unregisterReceiver(receiver);
		}
	}
