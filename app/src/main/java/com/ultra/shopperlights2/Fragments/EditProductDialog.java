package com.ultra.shopperlights2.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.*;
import com.ultra.shopperlights2.Utils.Calc;
import com.ultra.shopperlights2.Utils.O;

import java.util.ArrayList;
import java.util.List;

import static com.ultra.shopperlights2.Utils.O.TAG;

/**
 * <p></p>
 * <p><sub>(12.05.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class EditProductDialog extends DialogFragment
	{
	private boolean editTitleFlag= false;
	private Product product;
	private EditText inputN,inputQuality,inputPrice,inputWeight,inputWeightPrice;
	private AutoCompleteTextView inputManufacturer;
	private Spinner inputWeightUnit;
	private long productId=0;
	private String action;
	private View mainView;
	private WeightListener weightListener= new WeightListener();
	private PriceListener priceListener= new PriceListener();
	private EditText inputTitle;

	private class SaveListener implements View.OnClickListener
		{
		@Override
		public void onClick(View v)
			{
			if(editTitleFlag && inputTitle.getText().toString().length() == 0)
				{
				inputTitle.setError("Введи название");
				return;
				}
			if(editTitleFlag)
				product.setTitle(inputTitle.getText().toString() );
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
			getContext().sendBroadcast(new Intent(action) );
			dismiss();
			}
		}
	private class PriceListener implements TextWatcher
		{
		@Override
		public void beforeTextChanged(CharSequence s,int start,int count,int after) {}
		@Override
		public void onTextChanged(CharSequence s,int start,int before,int count) {}
		@Override
		public void afterTextChanged(Editable s)
			{
			String priceStr= s.toString();
			String weightStr= inputWeight.getText().toString();
			if(priceStr.equals("") || priceStr.equals(".") )
				priceStr="0.";
			if(weightStr.equals("") || weightStr.equals(".") )
				priceStr="1";
			float priceF= Float.parseFloat(priceStr);
			float weightF= Float.parseFloat(weightStr);
			if(weightF==0)
				weightF=1F;
			float result= Calc.round(priceF/weightF);
			inputWeightPrice.setText(""+ result);
			}
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
			String weightStr= s.toString();
			String weightPriceStr= inputWeightPrice.getText().toString();
			if(weightStr.equals(".") || weightStr.equals("") )
				weightStr="0.";
			if(weightPriceStr.equals(".") || weightPriceStr.equals("") )
				weightPriceStr="0.";
			float weightF= Float.parseFloat(weightStr);
			float weightPriceF= Float.parseFloat(weightPriceStr);
			float result= Calc.round(weightF*weightPriceF);
			inputPrice.removeTextChangedListener(priceListener);
			inputPrice.setText(""+ result);
			inputPrice.addTextChangedListener(priceListener);
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
				inputWeight.addTextChangedListener(weightListener);
				inputPrice.addTextChangedListener(priceListener);
				}
			else
				{
				inputWeightPrice.setText("");
				inputWeightPrice.setVisibility(View.GONE);
				inputPrice.removeTextChangedListener(priceListener);
				inputWeight.removeTextChangedListener(weightListener);
				}
			}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
		}

	public void init(String _action,long _id,boolean _editTitleFlag)
		{
		editTitleFlag=_editTitleFlag;
		action=_action;
		productId=_id;
		}
	public void init(String _action,long _id)
		{
		action=_action;
		productId=_id;
		}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,Bundle savedInstanceState)
		{
		getDialog().setTitle("Изменить продукт");
		mainView= inflater.inflate(R.layout.yellow_product_form_fragment,container,false);
		if(savedInstanceState!=null)
			{
			productId= savedInstanceState.getLong(O.mapKeys.savedState.SAVED_STATE_PRODUCT_ID);
			action= savedInstanceState.getString(O.mapKeys.savedState.SAVED_STATE_ACTION);
			}

		Button btnSave= (Button)mainView.findViewById(R.id.btnSave);
		inputTitle= (EditText)mainView.findViewById(R.id.inputTitle);
		inputN= (EditText)mainView.findViewById(R.id.inputN);
		inputQuality= (EditText)mainView.findViewById(R.id.inputQuality);
		inputPrice= (EditText)mainView.findViewById(R.id.inputPrice);
		inputWeight= (EditText)mainView.findViewById(R.id.inputWeight);
		inputWeightPrice= (EditText)mainView.findViewById(R.id.inputWeightPrice);
		inputManufacturer= (AutoCompleteTextView) mainView.findViewById(R.id.inputManufacturer);
		inputWeightUnit= (Spinner)mainView.findViewById(R.id.inputWeightUnit);
		TextView titleTxt= (TextView)mainView.findViewById(R.id.titleTxt);

		product= App.session.getProductDao().load(productId);
		Log.d(TAG,"onCreateView: titleTxt:"+ titleTxt);
		titleTxt.setText(product.getTitle() );
		if(editTitleFlag)
			{
			titleTxt.setVisibility(View.GONE);
			inputTitle.setVisibility(View.VISIBLE);
			inputTitle.setText(product.getTitle() );
			}
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
		outState.putString(O.mapKeys.savedState.SAVED_STATE_ACTION,action);
		super.onSaveInstanceState(outState);
		}
	}