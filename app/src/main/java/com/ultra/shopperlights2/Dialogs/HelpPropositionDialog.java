package com.ultra.shopperlights2.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.ultra.shopperlights2.Activities.HelpActivity;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Utils.O;

/**
 * <p>Класс-обертка над диалогом предложения почитать help</p>
 * <p><sub>(28.04.2017)</sub></p>
 * @author CC-Ultra
 */

public class HelpPropositionDialog
	 {
	 private Context context;
	 private AlertDialog dialog;
	 private ViewGroup parent;

	 /**
	  * нажатие на любую кнопку приведет к тому, что диалог больше не появится
	  */
	 private class Listener implements View.OnClickListener
		 {
		 @Override
		 public void onClick(View v)
			 {
			 if(v.getId()==R.id.btnHelp)
				 {
				 Intent jumper= new Intent(context,HelpActivity.class);
				 context.startActivity(jumper);
				 }
			 else if(v.getId() == R.id.btnHelpless)
				 Toast.makeText(context,"Ладно",Toast.LENGTH_SHORT).show();
			 SharedPreferences prefs= context.getSharedPreferences(O.PREFS_FILENAME,Context.MODE_PRIVATE);
			 prefs.edit().putBoolean(O.PREFS_FIRST_START,false).commit();
			 dialog.dismiss();
			 }
		 }
	 public void init(Context _context,ViewGroup _parent)
		 {
		 context=_context;
		 parent=_parent;
		 }

	 public void createAndShow()
		 {
		 View mainView= LayoutInflater.from(context).inflate(R.layout.before_help_dialog_layout,parent,false);
		 AlertDialog.Builder builder= new AlertDialog.Builder(context);

		 Button btnNegative= (Button)mainView.findViewById(R.id.btnHelpless);
		 Button btnPositive= (Button)mainView.findViewById(R.id.btnHelp);
		 btnPositive.setOnClickListener(new Listener() );
		 btnNegative.setOnClickListener(new Listener() );

		 builder.setView(mainView);
		 dialog= builder.create();
		 dialog.show();
		 }
	 }
