package com.ultra.shopperlights2.Test;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.*;
import com.ultra.shopperlights2.Utils.O;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p></p>
 * <p><sub>(05.05.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class TestActivity extends AppCompatActivity
	{
	TextView txt;
	private class Listener implements View.OnClickListener
		{
		@Override
		public void onClick(View v)
			{
			finish();
			Log.d(O.TAG,"onClick: 123");
			Log.d(O.TAG,"onClick: 456");
			Log.d(O.TAG,"onClick: 789");
//			txt.setText("есть.");
			}
		}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
		{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_layout);

		Button btn= (Button)findViewById(R.id.btn);
		txt= (TextView)findViewById(R.id.txt);

		btn.setOnClickListener(new Listener() );
		}
	}
