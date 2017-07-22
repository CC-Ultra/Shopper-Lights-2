package com.ultra.shopperlights2.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.Callbacks.DialogDecision;
import com.ultra.shopperlights2.Callbacks.EditPurchasePriceUpdate;
import com.ultra.shopperlights2.Callbacks.EditProductCallback;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.*;
import com.ultra.shopperlights2.Utils.ConfirmDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Адаптер списка продуктов покупки в истории</p>
 * <p><sub>(11.06.2017)</sub></p>
 * @author CC-Ultra
 */

public class EditPurchaseAdapter extends RecyclerView.Adapter<EditPurchaseAdapter.Holder>
	{
	private ArrayList<Product> elements;
	private Context context;
	private EditPurchasePriceUpdate priceUpdateCallback;
	private EditProductCallback editProductCallback;

	private class DelDecision implements DialogDecision
		{
		Product element;

		DelDecision(Product _element)
			{
			element=_element;
			}
		@Override
		public void sayNo(int noId){}

		/**
		 * Удаление продукта вместе со связями в {@code Purchase} и {@code TagToProduct}. После удаления, через callback
		 * обновляется цена покупки
		 */
		@Override
		public void sayYes(int yesId)
			{
			int position= elements.indexOf(element);
			delElement(position);
			DaoSession session= App.session;
			Purchase purchase= session.getPurchaseDao().load(element.getPurchaseId() );
			purchase.getProducts().remove(element);
			session.getPurchaseDao().update(purchase);
			List<TagToProduct> tagToProducts= session.getTagToProductDao().queryBuilder().
					where(TagToProductDao.Properties.ProductId.eq(element.getId() ) ).list();
			for(TagToProduct tagToProduct : tagToProducts)
				session.getTagToProductDao().delete(tagToProduct);
			session.getProductDao().delete(element);
			priceUpdateCallback.updatePrice();
			}
		}
	private class DelElementListener implements View.OnClickListener
		{
		DelDecision delDecision;

		void setElement(Product element)
			{
			delDecision= new DelDecision(element);
			}

		@Override
		public void onClick(View v)
			{
			ConfirmDialog.ask(context,delDecision,0,0);
			}
		}
	private class EditProductListener implements View.OnClickListener
		{
		Product element;

		void setElement(Product _element)
			{
			element=_element;
			}

		@Override
		public void onClick(View v)
			{
			editProductCallback.initDialog( (ViewGroup)v.getParent(),element.getId() );
			}
		}
	class Holder extends RecyclerView.ViewHolder
		{
		TextView title,n;
		TextView tag[]= new TextView[3];
		EditProductListener editListener;
		DelElementListener delListener;
		View mainView;

		public Holder(View itemView)
			{
			super(itemView);
			mainView=itemView;
			title= (TextView)mainView.findViewById(R.id.title);
			n= (TextView)mainView.findViewById(R.id.n);
			tag[0]= (TextView)mainView.findViewById(R.id.tag1);
			tag[1]= (TextView)mainView.findViewById(R.id.tag2);
			tag[2]= (TextView)mainView.findViewById(R.id.tag3);
			ImageView delBtn= (ImageView) mainView.findViewById(R.id.deleteBtn);
			editListener= new EditProductListener();
			delListener= new DelElementListener();
			delBtn.setOnClickListener(delListener);
			mainView.setOnClickListener(editListener);
			}
		}

	public EditPurchaseAdapter(Context _context,ArrayList<Product> _elements,EditPurchasePriceUpdate _priceUpdateCallback,
							   												EditProductCallback _editProductCallback)
		{
		editProductCallback=_editProductCallback;
		priceUpdateCallback=_priceUpdateCallback;
		elements=_elements;
		context=_context;
		}
	private void delElement(int position)
		{
		elements.remove(position);
		notifyItemRemoved(position);
		}
	@Override
	public Holder onCreateViewHolder(ViewGroup parent,int viewType)
		{
		View mainView= LayoutInflater.from(parent.getContext() ).inflate(R.layout.list_element_green_screen,parent,false);
		return new Holder(mainView);
		}
	@Override
	public void onBindViewHolder(Holder holder,int position)
		{
		Product element= elements.get(position);
		holder.mainView.setBackgroundResource(R.drawable.list_element_border_red);
		holder.title.setText(element.getTitle() );
		holder.n.setText(""+ element.getN() );
		if(element.getN()!=0)
			holder.n.setVisibility(View.VISIBLE);
		ArrayList<Tag> tags= new ArrayList<>(element.getTags() );
		int i=0;
		for(Tag tag : tags)
			{
			holder.tag[i].setVisibility(View.VISIBLE);
			holder.tag[i].setText(tag.getTitle() );
			holder.tag[i].setTextColor(tag.getColor() );
			i++;
			}
		holder.delListener.setElement(element);
		holder.editListener.setElement(element);
		}
	@Override
	public int getItemCount()
		{
		return elements.size();
		}
	}
