package com.ultra.shopperlights2.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ultra.shopperlights2.Activities.TagDetailsActivity;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.Tag;
import com.ultra.shopperlights2.Utils.Calc;
import com.ultra.shopperlights2.Utils.O;

import java.util.ArrayList;
import java.util.Date;

/**
 * <p>Адаптер для списка статистики по тегам</p>
 * <p><sub>(12.06.2017)</sub></p>
 * @author CC-Ultra
 */

public class TagStatAdapter extends RecyclerView.Adapter<TagStatAdapter.Holder>
	{
	private ArrayList<Tag> elements;
	private Context context;
	private float totalPrice;
	private Date dateFrom,dateTo;

	private class TagDetailsJumpListener implements View.OnClickListener
		{
		long tagId;

		void setTagId(long _tagId)
			{
			tagId=_tagId;
			}

		@Override
		public void onClick(View v)
			{
			Intent jumper= new Intent(context,TagDetailsActivity.class);
			jumper.putExtra(O.mapKeys.extra.TAG_ID,tagId);
			jumper.putExtra(O.mapKeys.extra.DATE_FROM,dateFrom.getTime() );
			jumper.putExtra(O.mapKeys.extra.DATE_TO,dateTo.getTime() );
			context.startActivity(jumper);
			}
		}

	class Holder extends RecyclerView.ViewHolder
		{
		TextView title,adr,date,price,percent;
		TagDetailsJumpListener listener;

		public Holder(View itemView)
			{
			super(itemView);
			title= (TextView)itemView.findViewById(R.id.txtDate); //так надо
			date= (TextView)itemView.findViewById(R.id.shopTitle);
			adr= (TextView)itemView.findViewById(R.id.shopAdr);
			price= (TextView)itemView.findViewById(R.id.totalPrice);
			percent= (TextView)itemView.findViewById(R.id.percent);
			listener= new TagDetailsJumpListener();
			itemView.setOnClickListener(listener);
			adr.setVisibility(View.INVISIBLE);
			date.setVisibility(View.INVISIBLE);
			}
		}

	public TagStatAdapter(Context _context,ArrayList<Tag> _elements,float _totalPrice,Date _dateFrom,Date _dateTo)
		{
		dateFrom=_dateFrom;
		dateTo=_dateTo;
		totalPrice=_totalPrice;
		elements=_elements;
		context=_context;
		}
	@Override
	public Holder onCreateViewHolder(ViewGroup parent,int viewType)
		{
		View mainView= LayoutInflater.from(parent.getContext() ).inflate(R.layout.edit_history_list_element,parent,false);
		return new Holder(mainView);
		}

	/**
	 * транспортный тег обрабатывается отдельно
	 */
	@Override
	public void onBindViewHolder(Holder holder,int position)
		{
		Tag element= elements.get(position);
		String title= element.getTitle();
		if(title.equals(O.TRANSPORT_TAG_NAME) )
			title="транспорт";
		holder.price.setText(""+ Calc.round(element.getTotalPrice() ) );
		holder.title.setText(title);
		holder.title.setTextColor(element.getColor() );
		if(totalPrice!=0)
			holder.percent.setText(Calc.round(element.getTotalPrice()/totalPrice*100) +"%");
		else
			holder.percent.setText("0%");
		holder.listener.setTagId(element.getId() );
		}
	@Override
	public int getItemCount()
		{
		return elements.size();
		}
	}
