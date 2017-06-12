package com.ultra.shopperlights2.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.Product;
import com.ultra.shopperlights2.Utils.Calc;

import java.util.ArrayList;

/**
 * <p></p>
 * <p><sub>(12.06.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class TagDetailsAdapter extends RecyclerView.Adapter<TagDetailsAdapter.Holder>
	{
	ArrayList<Product> elements;

	class Holder extends RecyclerView.ViewHolder
		{
		TextView title,price;

		public Holder(View itemView)
			{
			super(itemView);
			title= (TextView)itemView.findViewById(android.R.id.text1);
			price= (TextView)itemView.findViewById(android.R.id.text2);
			itemView.setBackgroundResource(R.drawable.list_element_border);
			}
		}

	public TagDetailsAdapter(ArrayList<Product> _elements)
		{
		elements=_elements;
		}

	@Override
	public Holder onCreateViewHolder(ViewGroup parent,int viewType)
		{
		View mainView= LayoutInflater.from(parent.getContext() ).inflate(android.R.layout.simple_list_item_2,parent,false);
		return new Holder(mainView);
		}
	@Override
	public void onBindViewHolder(Holder holder,int position)
		{
		Product element= elements.get(position);
		holder.price.setText(""+Calc.round( element.getPrice()* (element.getN()==0 ? 1 : element.getN() ) ) );
		holder.title.setText(element.getTitle() );
		}
	@Override
	public int getItemCount()
		{
		return elements.size();
		}
	}
