package com.ultra.shopperlights2.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.Callbacks.DialogDecision;
import com.ultra.shopperlights2.Callbacks.YellowScreenDelElement;
import com.ultra.shopperlights2.Callbacks.EditProductCallback;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.*;
import com.ultra.shopperlights2.Utils.ConfirmDialog;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Адаптер для списка продуктов на желтом экране</p>
 * <p><sub>(10.05.2017)</sub></p>
 * @author CC-Ultra
 */

public class YellowPurchaseListAdapter extends RecyclerView.Adapter<YellowPurchaseListAdapter.Holder>
	{
	private String action;
	private Context context;
	private long purchaseId;
	private YellowScreenDelElement delCallback;
	private EditProductCallback editProductCallback;
	private ArrayList<Product> elements;

	private class DelDecision implements DialogDecision
		{
		Product product;

		DelDecision(Product _product)
			{
			product=_product;
			}

		@Override
		public void sayNo(int noId) {}
		@Override
		public void sayYes(int yesId)
			{
			int position= elements.indexOf(product);
			delElement(position);
			context.sendBroadcast(new Intent(action) ); //обновить список на желтом экране покупки
			delCallback.delElement(product.getTitle() );
			}
		}
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
			int position= elements.indexOf(product);
			editProductCallback.initDialog( (ViewGroup)v.getParent(),product.getId() );
			notifyItemChanged(position);
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
			DelDecision decision= new DelDecision(product);
			ConfirmDialog.ask(context,decision,0,0);
			}
		}
	class Holder extends RecyclerView.ViewHolder
		{
		View mainView;
		TextView titleTxt;
		ImageView btnDel;
		DelListener delListener;
		SelectListener selectListener;

		public Holder(View itemView)
			{
			super(itemView);
			mainView=itemView;
			titleTxt= (TextView)itemView.findViewById(R.id.txt);
			btnDel= (ImageView)itemView.findViewById(R.id.btnDel);
			delListener= new DelListener();
			selectListener= new SelectListener();
			itemView.setOnClickListener(selectListener);
			btnDel.setOnClickListener(delListener);
			}
		}

	public YellowPurchaseListAdapter(Context _context,ArrayList<Product> _elements,long _purchaseId,String _action,
									 YellowScreenDelElement _delCallback,EditProductCallback _initCallback)
		{
		action=_action;
		context=_context;
		purchaseId=_purchaseId;
		delCallback=_delCallback;
		editProductCallback=_initCallback;
		elements=_elements;
		}

	/**
	 * Метод для добавления элемента извне. Принимает на вход запись, а сохраняет продукт
	 */
	public void addElement(Note note)
		{
		Product product= new Product();
		if(note.getProductId()!=0) //если продукт был взят из истории
			{
			Product p2= App.session.getProductDao().load(note.getProductId() );
			product.setTitle(p2.getTitle() );
			product.setManufacturerId(p2.getManufacturerId() );
			product.setN(p2.getN() );
			product.setWeight(p2.getWeight() );
			product.setWeightUnit(p2.getWeightUnit() );
			product.setEthereal(true);
			product.setQuality(p2.getQuality() );
			product.setComplete(false);
			product.setPurchaseId(purchaseId);
			}
		else
			{
			product.setTitle(note.getTitle() );
			product.setN(note.getN() );
			product.setComplete(false);
			product.setEthereal(note.isEthereal() );
			product.setPurchaseId(purchaseId);
			}
		App.session.getProductDao().insert(product);
		List<Tag> tags;
		if(note.getProductId()!=0)
			tags= App.session.getProductDao().load(note.getProductId() ).getTags();
		else
			tags= note.getTags();
		for(Tag tag : tags)
			{
			product.getTags().add(tag);
			TagToProduct tagToProduct= new TagToProduct();
			tagToProduct.setProductId(product.getId() );
			tagToProduct.setTagId(tag.getId() );
			App.session.getTagToProductDao().insert(tagToProduct);
			}
		notifyItemInserted(elements.size() );
		elements.add(product);
		}

	/**
	 * Удалить элемент, продукт, связи с тегами
	 */
	private void delElement(int position)
		{
		Product product= elements.get(position);
		DaoSession session= App.session;
		List<TagToProduct> ttps= session.getTagToProductDao().queryBuilder().where(TagToProductDao.Properties.ProductId.eq(product.getId() ) ).list();
		for(TagToProduct tagToProduct : ttps)
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
	@SuppressWarnings("deprecation")
	@Override
	public void onBindViewHolder(Holder holder,int position)
		{
		Product product= elements.get(position);
		holder.titleTxt.setText(product.getTitle() );
		holder.selectListener.setProduct(product);
		holder.delListener.setProduct(product);
		if(product.isComplete() )
			{
			holder.mainView.setBackgroundColor(context.getResources().getColor(R.color.lightYellowActive) );
			holder.titleTxt.setTextColor(context.getResources().getColor(R.color.pure_black));
			}
		else
			holder.mainView.setBackgroundColor(context.getResources().getColor(R.color.list_element_solid) );
		}
	@Override
	public int getItemCount()
		{
		return elements.size();
		}
	}
