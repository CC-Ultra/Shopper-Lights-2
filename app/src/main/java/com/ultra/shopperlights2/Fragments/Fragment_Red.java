package com.ultra.shopperlights2.Fragments;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.ultra.shopperlights2.Activities.*;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Utils.DateUtil;
import com.ultra.shopperlights2.Utils.O;

import java.util.ArrayList;
import java.util.Date;

/**
 * <p>Фрагмент красного экрана</p>
 * <p><sub>(07.08.2016)</sub></p>
 * @author CC-Ultra
 */
public class Fragment_Red extends Fragment
	{
	private static Date dateFrom,dateTo;
	private Spinner inputOptions;
	private TextView txtDateFrom,txtDateTo;

	private class StartListener implements View.OnClickListener
		{
		/**
		 * Выбор направления intent-а, внесение в него дат и старт активности
		 */
		@Override
		public void onClick(View v)
			{
			if(dateFrom==null || dateTo==null)
				{
				Toast.makeText(getContext(),"Выбери диапазон дат",Toast.LENGTH_SHORT).show();
				return;
				}
			if(!dateFrom.before(dateTo) )
				{
				Toast.makeText(getContext(),"Введи корректные даты",Toast.LENGTH_SHORT).show();
				return;
				}
			Intent jumper= new Intent();
			switch(inputOptions.getSelectedItemPosition() )
				{
				case O.interaction.STAT_CODE_EDIT_HISTORY:
					jumper= new Intent(getContext(),EditHistoryActivity.class);
					break;
				case O.interaction.STAT_CODE_TAG_STAT:
					jumper= new Intent(getContext(),TagStatActivity.class);
					break;
				case O.interaction.STAT_CODE_MOST_REQUIRED:
					jumper= new Intent(getContext(),MostRequiredActivity.class);
					break;
				case O.interaction.STAT_CODE_SEARCH:
					jumper= new Intent(getContext(),SearchActivity.class);
					break;
				case O.interaction.STAT_CODE_COST_DINAMICS:
					jumper= new Intent(getContext(),PriceDynamicsListActivity.class);
					break;
				}
			jumper.putExtra(O.mapKeys.extra.DATE_FROM,dateFrom.getTime() );
			jumper.putExtra(O.mapKeys.extra.DATE_TO,dateTo.getTime() );
			getContext().startActivity(jumper);
			}
		}

	/**
	 * Здесь запоминается дата после установки ее в диалоге и устанавливается текстовое поле
	 */
	private class DateDialogListener implements DatePickerDialog.OnDateSetListener
		{
		boolean from; //from или to

		DateDialogListener(boolean _from)
			{
			from=_from;
			}

		@Override
		public void onDateSet(DatePicker view,int year,int month,int dayOfMonth)
			{
			if(from)
				{
				dateFrom= DateUtil.getDateFromDMY(dayOfMonth,month,year);
				txtDateFrom.setText(DateUtil.getDateStr(dateFrom) );
				}
			else
				{
				dateTo= DateUtil.getDateFromDMY(dayOfMonth,month,year);
				txtDateTo.setText(DateUtil.getDateStr(dateTo) );
				}
			}
		}

	/**
	 * получаю {@link DateUtil.DateDMY} из соответственной даты и стартую по нему диалог
	 */
	private class ClockButtonListener implements View.OnClickListener
		{
		@Override
		public void onClick(View v)
			{
			DatePickerDialog dialog=null;
			if(v.getId()==R.id.btnDateFrom)
				{
				DateUtil.DateDMY dateDMY= DateUtil.getDMYfromDate(dateFrom);
				dialog= new DatePickerDialog(getContext(),new DateDialogListener(true),dateDMY.year,dateDMY.month,dateDMY.day);
				}
			else if(v.getId()==R.id.btnDateTo)
				{
				DateUtil.DateDMY dateDMY= DateUtil.getDMYfromDate(dateTo);
				dialog= new DatePickerDialog(getContext(),new DateDialogListener(false),dateDMY.year,dateDMY.month,dateDMY.day);
				}
			if(dialog!=null)
				dialog.show();
			}
		}

	private void initOptions()
		{
		ArrayList<String> optionsList= new ArrayList<>();
		optionsList.add("Редактировать историю");
		optionsList.add("Статистика по тегам");
		optionsList.add("Самые востребованные продукты");
		optionsList.add("Поиск по ключевому слову");
		optionsList.add("Динамика цен");
		ArrayAdapter<String> adapter= new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,optionsList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		inputOptions.setAdapter(adapter);
		}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
		{
		View mainView= inflater.inflate(R.layout.red_screen,container,false);

		ImageButton buttonDateFrom= (ImageButton)mainView.findViewById(R.id.btnDateFrom);
		ImageButton buttonDateTo= (ImageButton)mainView.findViewById(R.id.btnDateTo);
		txtDateFrom= (TextView)mainView.findViewById(R.id.txtDateFrom);
		txtDateTo= (TextView)mainView.findViewById(R.id.txtDateTo);
		inputOptions= (Spinner)mainView.findViewById(R.id.inputOptions);
		Button btnStart= (Button)mainView.findViewById(R.id.btnStart);

		btnStart.setOnClickListener(new StartListener() );
		buttonDateFrom.setOnClickListener(new ClockButtonListener() );
		buttonDateTo.setOnClickListener(new ClockButtonListener() );
		initOptions();

		return mainView;
		}

	/**
	 * работает когда возвращаешься в активность
	 */
	@Override
	public void onResume()
		{
		super.onResume();
		if(dateFrom!=null)
			txtDateFrom.setText(DateUtil.getDateStr(dateFrom) );
		if(dateTo!=null)
			txtDateTo.setText(DateUtil.getDateStr(dateTo) );
		}
	}
