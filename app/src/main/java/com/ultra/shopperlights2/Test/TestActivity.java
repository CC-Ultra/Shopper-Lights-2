package com.ultra.shopperlights2.Test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Utils.O;

import java.util.ArrayList;

/**
 * <p></p>
 * <p><sub>(05.05.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class TestActivity extends AppCompatActivity
	{
	EditText simpleTxt;
	AutoCompleteTextView autoTxt;

	private class SimpleListener implements TextWatcher
		{
		@Override
		public void beforeTextChanged(CharSequence s,int start,int count,int after)
			{
			Log.d(O.TAG,"beforeTextChanged: s="+ s +"\tstart="+ start +"\tcount="+ count +"\tafter="+ after);
			}

		@Override
		public void onTextChanged(CharSequence s,int start,int before,int count)
			{
			Log.d(O.TAG,"onTextChanged: s="+ s +"\tstart="+ start +"\tbefore="+ before +"\tcount="+ count);
			}

		@Override
		public void afterTextChanged(Editable s)
			{
			autoTxt.setHint(s.toString() );
			Log.d(O.TAG,"afterTextChanged: s="+ s);
			}
		}
	private class Listener implements View.OnClickListener
		{
		@Override
		public void onClick(View v)
			{
			Toast.makeText(TestActivity.this,"123",Toast.LENGTH_SHORT).show();
			}
		}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
		{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_layout);

		ArrayList<String> x= new ArrayList<>();
		x.add("123");
		x.add("1123");
		x.add("11123");
		x.add("1122");

		autoTxt= (AutoCompleteTextView)findViewById(R.id.autotext);
		simpleTxt= (EditText)findViewById(R.id.simpleTxt);

		ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,x);
		autoTxt.setAdapter(adapter);
		simpleTxt.addTextChangedListener(new SimpleListener() );
		}
	}
