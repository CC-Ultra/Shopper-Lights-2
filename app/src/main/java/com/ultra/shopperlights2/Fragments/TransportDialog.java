package com.ultra.shopperlights2.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.Purchase;
import com.ultra.shopperlights2.Utils.Calc;

import java.util.Date;

/**
 * <p></p>
 * <p><sub>(04.06.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class TransportDialog extends DialogFragment
	{
	private EditText inputPrice;

	private class OkListener implements View.OnClickListener
		{
		@Override
		public void onClick(View v)
			{
			String priceStr= inputPrice.getText().toString();
			if(priceStr.length()==0)
				{
				inputPrice.setError("Введи стоимость проезда");
				return;
				}
			if(priceStr.equals(".") || Float.parseFloat(priceStr)==0)
				{
				inputPrice.setError("Некорректный ввод");
				return;
				}
			float priceF= Float.parseFloat(priceStr);
			Purchase purchase= new Purchase();
			purchase.setCompleted(true);
			purchase.setPrice(priceF);
			purchase.setDate(new Date() );
			purchase.setShopId(0);
			App.session.getPurchaseDao().insert(purchase);
			Toast.makeText(getContext(),"Оплачено: "+ Calc.round(priceF),Toast.LENGTH_SHORT).show();
			dismiss();
			}
		}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState)
		{
		getDialog().setTitle("Транспорт");
		View mainView= inflater.inflate(R.layout.transport_dialog_layout,container,false);

		inputPrice= (EditText)mainView.findViewById(R.id.inputPrice);
		Button btnSubmit= (Button)mainView.findViewById(R.id.btnOk);

		btnSubmit.setOnClickListener(new OkListener() );
		return mainView;
		}
	}
