package com.ultra.shopperlights2.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.Callbacks.UpdateListCallback;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.Manufacturer;
import com.ultra.shopperlights2.Units.ManufacturerDao;
import com.ultra.shopperlights2.Units.Product;
import com.ultra.shopperlights2.Utils.O;

import java.util.ArrayList;
import java.util.List;

/**
 * <p></p>
 * <p><sub>(11.05.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class ProductFormFragment extends Fragment
	{
	private UpdateListCallback callback;
	private long productId;
	private Product product;
	private EditText inputN,inputQuality,inputPrice,inputWeight,inputWeightPrice;
	private AutoCompleteTextView inputManufacturer;
	private Spinner inputWeightUnit;
	private View mainView;

	private class SaveListener implements View.OnClickListener
		{
		@Override
		public void onClick(View v)
			{
			product.setComplete(true);
			if(inputN.length()!=0)
				product.setN(Integer.parseInt(inputN.getText().toString() ) );
			if(inputQuality.length()!=0)
				product.setQuality(Float.parseFloat(inputQuality.getText().toString() ) );
			if(inputPrice.length()!=0)
				product.setPrice(Float.parseFloat(inputPrice.getText().toString() ) );
			if(inputWeight.length()!=0)
				product.setWeight(Float.parseFloat(inputWeight.getText().toString() ) );
			if(inputManufacturer.length()!=0)
				{
				String manufacturerStr= inputManufacturer.getText().toString();
				List<Manufacturer> manufacturers= App.session.getManufacturerDao().queryBuilder().where(ManufacturerDao.Properties.Title.eq(manufacturerStr) ).list();
				Manufacturer manufacturer;
				if(manufacturers.size()==0)
					{
					manufacturer= new Manufacturer();
					manufacturer.setTitle(manufacturerStr);
					App.session.getManufacturerDao().insert(manufacturer);
					}
				else
					manufacturer= manufacturers.get(0);
				product.setManufacturerId(manufacturer.getId() );
				}
			product.setWeightUnit(inputWeightUnit.getSelectedItem().toString() );
			App.session.getProductDao().update(product);
			callback.updateLists();
			Toast.makeText(getContext(),"Продукт "+ product.getTitle() +" сохранен",Toast.LENGTH_SHORT).show();
			}
		}
	private class DummyListener implements TextWatcher
		{
		@Override
		public void beforeTextChanged(CharSequence s,int start,int count,int after) {}
		@Override
		public void onTextChanged(CharSequence s,int start,int before,int count) {}
		@Override
		public void afterTextChanged(Editable s) {}
		}
	private class WeightListener implements TextWatcher
		{
		@Override
		public void beforeTextChanged(CharSequence s,int start,int count,int after) {}
		@Override
		public void onTextChanged(CharSequence s,int start,int before,int count) {}
		@Override
		public void afterTextChanged(Editable s)
			{
			if(inputWeightPrice.length()!=0)
				{
				String str=s.toString();
				if(str.equals(".") )
					str="0.";
				float q= Float.parseFloat(inputWeightPrice.getText().toString() );
				inputPrice.setText(""+ (Float.parseFloat(str) * q) );
				}
			}
		}
	private class WeightUnitsListener implements AdapterView.OnItemSelectedListener
		{
		@Override
		public void onItemSelected(AdapterView<?> parent,View view,int position,long id)
			{
			if(position==1)
				{
				inputWeightPrice.setVisibility(View.VISIBLE);
				inputWeight=null;
				inputWeight= (EditText)mainView.findViewById(R.id.inputWeight);
				inputWeight.addTextChangedListener(new WeightListener() );
				}
			else
				{
				inputWeightPrice.setText("");
				inputWeightPrice.setVisibility(View.GONE);
				inputWeight=null;
				inputWeight= (EditText)mainView.findViewById(R.id.inputWeight);
				inputWeight.addTextChangedListener(new DummyListener() );
				}
			}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
		}
	void initFragment(long _productId,UpdateListCallback _callback)
		{
		productId=_productId;
		callback=_callback;
		}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState)
		{
		mainView= inflater.inflate(R.layout.yellow_product_form_fragment,container,false);
		if(savedInstanceState!=null)
			productId=savedInstanceState.getLong(O.mapKeys.savedState.SAVED_STATE_PRODUCT_ID);

		Button btnSave= (Button)mainView.findViewById(R.id.btnSave);
		inputN= (EditText)mainView.findViewById(R.id.inputN);
		inputQuality= (EditText)mainView.findViewById(R.id.inputQuality);
		inputPrice= (EditText)mainView.findViewById(R.id.inputPrice);
		inputWeight= (EditText)mainView.findViewById(R.id.inputWeight);
		inputWeightPrice= (EditText)mainView.findViewById(R.id.inputWeightPrice);
		inputManufacturer= (AutoCompleteTextView) mainView.findViewById(R.id.inputManufacturer);
		inputWeightUnit= (Spinner)mainView.findViewById(R.id.inputWeightUnit);
		TextView titleTxt= (TextView)mainView.findViewById(R.id.titleTxt);

		product= App.session.getProductDao().load(productId);
		titleTxt.setText(product.getTitle() );
		if(product.getN()!=0)
			inputN.setText(""+ product.getN() );
		if(product.getManufacturerId()!=0)
			inputManufacturer.setText(App.session.getManufacturerDao().load(product.getManufacturerId() ).getTitle() );
		if(product.getWeight()!=0)
			inputWeight.setText(product.getWeight() +"");
		if(product.getPrice()!=0)
			inputPrice.setText(""+ product.getPrice() );
		if(product.getQuality()!=0)
			inputQuality.setText(product.getQuality() +"");
		ArrayList<String> spinnerSrc= new ArrayList<>();
		spinnerSrc.add("");
		spinnerSrc.add("кг");
		spinnerSrc.add("л");
		spinnerSrc.add("шт");
		ArrayAdapter<String> spinnerAdapter= new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,spinnerSrc);
		spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		inputWeightUnit.setAdapter(spinnerAdapter);
		inputWeightUnit.setOnItemSelectedListener(new WeightUnitsListener() );
		if(product.getWeightUnit()!=null)
			inputWeightUnit.setSelection(spinnerSrc.indexOf(product.getWeightUnit() ) );
		List<Manufacturer> manufacturersSrc= App.session.getManufacturerDao().loadAll();
		ArrayList<String> manufacturers= new ArrayList<>();
		for(Manufacturer manufacturer : manufacturersSrc)
			manufacturers.add(manufacturer.getTitle() );
		ArrayAdapter<String> autocompleteAdapter= new ArrayAdapter<>(getContext(),android.R.layout.simple_dropdown_item_1line,manufacturers);
		inputManufacturer.setAdapter(autocompleteAdapter);
		btnSave.setOnClickListener(new SaveListener() );

		return mainView;
		}
	@Override
	public void onSaveInstanceState(Bundle outState)
		{
		outState.putLong(O.mapKeys.savedState.SAVED_STATE_PRODUCT_ID,productId);
		super.onSaveInstanceState(outState);
		}
	}
