package com.ultra.shopperlights2.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.Frequency;

import java.util.ArrayList;

/**
 * <p></p>
 * <p><sub>(17.06.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class MostRequiredAdapter extends RecyclerView.Adapter<MostRequiredAdapter.Holder>
	{
	ArrayList<Frequency> elements;
	class Holder extends RecyclerView.ViewHolder
		{
		View mainView;
		TextView title,n;

		public Holder(View itemView)
			{
			super(itemView);
			mainView=itemView;
			title= (TextView)mainView.findViewById(android.R.id.text1);
			n= (TextView)mainView.findViewById(android.R.id.text2);
			}
		}

	public MostRequiredAdapter(ArrayList<Frequency> _elements)
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
		Frequency element= elements.get(position);
		holder.title.setText(element.getTitle() );
		String word;
		int n= element.getN();
		if(n%10 > 1 && n%10 < 5 && (n/10)%10 !=1)
			word=" раза";
		else
			word=" раз";
		holder.n.setText(n + word);
		holder.mainView.setBackgroundResource(R.drawable.list_element_border_red);
		}
	@Override
	public int getItemCount()
		{
		return elements.size();
		}
	}
