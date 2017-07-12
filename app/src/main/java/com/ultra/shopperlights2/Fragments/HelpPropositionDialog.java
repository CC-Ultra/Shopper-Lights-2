package com.ultra.shopperlights2.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.ultra.shopperlights2.Activities.HelpActivity;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.Group;
import com.ultra.shopperlights2.Units.GroupDao;
import com.ultra.shopperlights2.Utils.O;

/**
 * <p></p>
 * <p><sub>(28.04.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class HelpPropositionDialog extends DialogFragment
	 {
	 private class Listener implements View.OnClickListener
		 {
		 @Override
		 public void onClick(View v)
			 {
			 if(v.getId()==R.id.btnHelp)
				 {
				 Intent jumper= new Intent(getContext(),HelpActivity.class);
				 getContext().startActivity(jumper);
				 }
			 else if(v.getId() == R.id.btnHelpless)
				 Toast.makeText(getContext(),"Ладно",Toast.LENGTH_SHORT).show();
			 SharedPreferences prefs= getContext().getSharedPreferences(O.PREFS_FILENAME,Context.MODE_PRIVATE);
			 prefs.edit().putBoolean(O.PREFS_FIRST_START,false).commit();
			 dismiss();
			 }
		 }

	 @Nullable
	 @Override
	 public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,Bundle savedInstanceState)
		 {
		 View mainView= inflater.inflate(R.layout.before_help_dialog_layout,container,false);

		 Button btnNegative= (Button)mainView.findViewById(R.id.btnHelpless);
		 Button btnPositive= (Button)mainView.findViewById(R.id.btnHelp);
		 btnPositive.setOnClickListener(new Listener() );
		 btnNegative.setOnClickListener(new Listener() );
		 return mainView;
		 }
	 }
