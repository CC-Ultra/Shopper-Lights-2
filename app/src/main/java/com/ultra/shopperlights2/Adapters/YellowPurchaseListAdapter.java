package com.ultra.shopperlights2.Adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.Callbacks.YellowScreenDelElement;
import com.ultra.shopperlights2.Callbacks.YellowScreenInitFragment;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p></p>
 * <p><sub>(10.05.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class YellowPurchaseListAdapter extends RecyclerView.Adapter<YellowPurchaseListAdapter.Holder>
	{
	private long purchaseId;
	private int selected=-1;
	private YellowScreenDelElement delCallback;
	private YellowScreenInitFragment initCallback;
	private ArrayList<Product> elements;

	private class SelectListener implements View.OnClickListener
		{
		private Product product;

		void setProduct(Product _product)
			{
			product=_product;
			}

		@Override
		public void onClick(View v)
			{
			deselect();
			selected= elements.indexOf(product);
			notifyItemChanged(selected);
			initCallback.initFragment(product.getId() );
			}
		}
	private class DelListener implements View.OnClickListener
		{
		Product product;

		void setProduct(Product _product)
			{
			product=_product;
			}

		@Override
		public void onClick(View v)
			{
			int position= elements.indexOf(product);
			delElement(position);
			delCallback.delElement(product.getTitle() );
			deselect();
			}
		}
	class Holder extends RecyclerView.ViewHolder
		{
		View mainView;
		TextView titleTxt;
		ImageButton btnDel;
		DelListener delListener;
		SelectListener selectListener;

		public Holder(View itemView)
			{
			super(itemView);
			mainView=itemView;
			titleTxt= (TextView)itemView.findViewById(R.id.txt);
			btnDel= (ImageButton)itemView.findViewById(R.id.btnDel);
			delListener= new DelListener();
			selectListener= new SelectListener();
			itemView.setOnClickListener(selectListener);
			btnDel.setOnClickListener(delListener);
			}
		}

	public YellowPurchaseListAdapter(ArrayList<Product> _elements,long _purchaseId,YellowScreenDelElement _delCallback,YellowScreenInitFragment _initCallback)
		{
		purchaseId=_purchaseId;
		delCallback=_delCallback;
		initCallback=_initCallback;
		elements=_elements;
		}
	private void deselect()
		{
		int lastSelected=selected;
		selected=-1;
		notifyItemChanged(lastSelected);
		}
	public void addElement(Note note)
		{
		Product product= new Product();
		product.setTitle(note.getTitle() );
		product.setN(note.getN() );
		product.setComplete(false);
		product.setPurchaseId(purchaseId);
		App.session.getProductDao().insert(product);
		for(Tag tag : note.getTags())
			{
			TagToProduct tagToProduct= new TagToProduct();
			tagToProduct.setProductId(product.getId() );
			tagToProduct.setTagId(tag.getId() );
			App.session.getTagToProductDao().insert(tagToProduct);
			}
		notifyItemInserted(elements.size() );
		elements.add(product);
		}
	private void delElement(int position)
		{
		Product product= elements.get(position);
		DaoSession session= App.session;
		List<TagToProduct> tTPs= session.getTagToProductDao().queryBuilder().where(TagToProductDao.Properties.ProductId.eq(product.getId())).list();
		for(TagToProduct tagToProduct : tTPs)
			session.getTagToProductDao().delete(tagToProduct);
		session.getProductDao().delete(product);
		elements.remove(position);
		notifyItemRemoved(position);
		}

	@Override
	public Holder onCreateViewHolder(ViewGroup parent,int viewType)
		{
		View mainView= LayoutInflater.from(parent.getContext() ).inflate(R.layout.yellow_purchase_list_element,parent,false);
		return new Holder(mainView);
		}
	@Override
	public void onBindViewHolder(Holder holder,int position)
		{
		Product product= elements.get(position);
		holder.titleTxt.setText(product.getTitle() );
		holder.selectListener.setProduct(product);
		holder.delListener.setProduct(product);
		if(position==selected)
			holder.mainView.setBackgroundColor(Color.parseColor("#fff200") );
		else if(product.isComplete() )
			holder.mainView.setBackgroundColor(Color.parseColor("#00dd00") );
		else
			holder.mainView.setBackgroundColor(Color.parseColor("#222222") );
		}
	@Override
	public int getItemCount()
		{
		return elements.size();
		}
	}
