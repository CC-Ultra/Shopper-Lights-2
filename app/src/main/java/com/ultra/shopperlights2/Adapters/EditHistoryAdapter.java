package com.ultra.shopperlights2.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ultra.shopperlights2.Activities.EditPurchaseActivity;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.Purchase;
import com.ultra.shopperlights2.Units.Shop;
import com.ultra.shopperlights2.Utils.Calc;
import com.ultra.shopperlights2.Utils.DateUtil;
import com.ultra.shopperlights2.Utils.O;

import java.util.ArrayList;

/**
 * <p>Адаптер списка истории покупок</p>
 * <p><sub>(11.06.2017)</sub></p>
 * @author CC-Ultra
 */

public class EditHistoryAdapter extends RecyclerView.Adapter<EditHistoryAdapter.Holder>
	{
	private Context context;
	private float totalPrice;
	private ArrayList<Purchase> elements;

	private class JumpListener implements View.OnClickListener
		{
		long purchaseId;

		void setPurchaseId(long _purchaseId)
			{
			purchaseId=_purchaseId;
			}

		@Override
		public void onClick(View v)
			{
			Intent jumper= new Intent(context,EditPurchaseActivity.class);
			jumper.putExtra(O.mapKeys.extra.PURCHASE_ID,purchaseId);
			context.startActivity(jumper);
			}
		}
	class Holder extends RecyclerView.ViewHolder
		{
		TextView txtDate,txtShopTitle,txtPrice,txtShopAdr,txtPercent;
		View mainView;
		JumpListener jumpListener;

		public Holder(View itemView)
			{
			super(itemView);
			mainView=itemView;
			txtDate= (TextView)mainView.findViewById(R.id.txtDate);
			txtShopTitle= (TextView)mainView.findViewById(R.id.shopTitle);
			txtPrice= (TextView)mainView.findViewById(R.id.totalPrice);
			txtShopAdr= (TextView)mainView.findViewById(R.id.shopAdr);
			txtPercent= (TextView) mainView.findViewById(R.id.percent);
			jumpListener= new JumpListener();
			mainView.setOnClickListener(jumpListener);
			}
		}

	public EditHistoryAdapter(Context _context,ArrayList<Purchase> _elements,float _totalPrice)
		{
		context=_context;
		elements=_elements;
		totalPrice=_totalPrice;
		if(totalPrice==0)
			totalPrice=1;
		}

	@Override
	public Holder onCreateViewHolder(ViewGroup parent,int viewType)
		{
		View mainView= LayoutInflater.from(parent.getContext() ).inflate(R.layout.edit_history_list_element,parent,false);
		return new Holder(mainView);
		}
	@Override
	public void onBindViewHolder(Holder holder,int position)
		{
		Purchase element= elements.get(position);
		holder.jumpListener.setPurchaseId(element.getId() );
		holder.txtPrice.setText(""+ Calc.round(element.getPrice() ) );
		Shop loadedShop= App.session.getShopDao().load(element.getShopId());
		if(loadedShop==null)
			{
			holder.txtShopTitle.setText("Неизвестный магазин");
			holder.txtShopAdr.setText("Неизвестно");
			}
		else
			{
			holder.txtShopTitle.setText(loadedShop.getTitle() );
			holder.txtShopAdr.setText(loadedShop.getCity() +", "+ loadedShop.getAdr() );
			}
		holder.txtDate.setText(DateUtil.getDateStr(element.getDate() ) );
		float percent= Calc.round(100*(element.getPrice()/totalPrice) );
		holder.txtPercent.setText(percent +"%");
		}
	@Override
	public int getItemCount()
		{
		return elements.size();
		}
	}
