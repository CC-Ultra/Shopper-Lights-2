package com.ultra.shopperlights2.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.Manufacturer;
import com.ultra.shopperlights2.Units.PriceDynamicsUnit;
import com.ultra.shopperlights2.Units.Product;
import com.ultra.shopperlights2.Utils.Calc;
import com.ultra.shopperlights2.Utils.DateUtil;

import java.util.ArrayList;

/**
 * <p></p>
 * <p><sub>(18.06.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class PartialMatchListAdapter extends RecyclerView.Adapter<PartialMatchListAdapter.Holder>
	{
	private ArrayList<PriceDynamicsUnit> elements;

	class Holder extends RecyclerView.ViewHolder
		{
		TextView manufacturer,date,percent,priceDesc,price,weight,title;
		public Holder(View itemView)
			{
			super(itemView);
			title= (TextView)itemView.findViewById(R.id.txtTitle);
			manufacturer= (TextView)itemView.findViewById(R.id.txtManufacturer);
			date= (TextView)itemView.findViewById(R.id.txtDate);
			percent= (TextView)itemView.findViewById(R.id.txtPercent);
			priceDesc= (TextView)itemView.findViewById(R.id.txtPriceDesc);
			price= (TextView)itemView.findViewById(R.id.txtPrice);
			weight= (TextView)itemView.findViewById(R.id.txtWeight);
			}
		}

	public PartialMatchListAdapter(ArrayList<PriceDynamicsUnit> _elements)
		{
		elements=_elements;
		}

	@Override
	public Holder onCreateViewHolder(ViewGroup parent,int viewType)
		{
		View mainView= LayoutInflater.from(parent.getContext() ).inflate(R.layout.partial_match_list_element,parent,false);
		return new Holder(mainView);
		}
	@Override
	public void onBindViewHolder(Holder holder,int position)
		{
		PriceDynamicsUnit element= elements.get(position);
		Product product= element.getProduct();
		float quality= product.getQuality();
		holder.percent.setText( (quality==0 ? "" : quality +"%") );
		String weightUnits= product.getWeightUnit();
		holder.weight.setText(product.getWeight() +" "+ product.getWeightUnit() );
		holder.title.setText(product.getTitle() );
		if(weightUnits.equals("кг") )
			{
			holder.priceDesc.setText("цена за кг:");
			float weight= (product.getWeight()==0 ? 1 : product.getWeight() );
			holder.price.setText(""+ Calc.round(product.getPrice() / weight) );
			}
		else
			{
			holder.priceDesc.setText("цена:");
			holder.price.setText(""+ Calc.round(product.getPrice() ) );
			}
		holder.date.setText(DateUtil.getDateStr(element.getDate() ) );
		Manufacturer loadedManufacturer= App.session.getManufacturerDao().load(product.getManufacturerId());
		if(loadedManufacturer==null)
			holder.manufacturer.setText("Неизвестный производитель");
		else
			holder.manufacturer.setText(loadedManufacturer.getTitle() );
		}
	@Override
	public int getItemCount()
		{
		return elements.size();
		}
	}
