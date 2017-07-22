package com.ultra.shopperlights2.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.Purchase;
import com.ultra.shopperlights2.Utils.Calc;
import java.util.Date;

/**
 * <p>Класс-обертка над транспортным диалогом</p>
 * <p><sub>(04.06.2017)</sub></p>
 * @author CC-Ultra
 */

public class TransportDialog
	{
	private Context context;
	private AlertDialog dialog;
	private ViewGroup parent;
	private EditText inputPrice;

	private class OkListener implements View.OnClickListener
		{
		@Override
		public void onClick(View v)
			{
			submitAction();
			}
		}
	private class SubmitEditTextListener implements TextView.OnEditorActionListener
		{
		@Override
		public boolean onEditorAction(TextView v,int actionId,KeyEvent event)
			{
			submitAction();
			return true;
			}
		}

	private void submitAction()
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
		Toast.makeText(context,"Оплачено: "+ Calc.round(priceF),Toast.LENGTH_SHORT).show();
		dialog.dismiss();
		}
	public void init(Context _context,ViewGroup _parent)
		{
		context=_context;
		parent=_parent;
		}

	/**
	 * Инициализация mainView и передача ее диалогу
	 */
	public void createAndShow()
		{
		View mainView= LayoutInflater.from(context).inflate(R.layout.transport_dialog_layout,parent,false);
		AlertDialog.Builder builder= new AlertDialog.Builder(context);
		builder.setTitle("Транспорт");

		inputPrice= (EditText)mainView.findViewById(R.id.inputPrice);
		Button btnSubmit= (Button)mainView.findViewById(R.id.btnOk);

		btnSubmit.setOnClickListener(new OkListener() );

		builder.setView(mainView);
		dialog= builder.create();
		dialog.show();
		}
	}
