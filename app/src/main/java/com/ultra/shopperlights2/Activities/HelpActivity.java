package com.ultra.shopperlights2.Activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.ultra.shopperlights2.R;

/**
 * <p>Экран help-а</p>
 * Здесь есть номер текущего экрана, 2 кнопки, меняющие это значение и два массива, по которым по номеру инициализируются
 * два {@code TextView}
 * <p><sub>(11.07.2017)</sub></p>
 * @author CC-Ultra
 */

public class HelpActivity extends AppCompatActivity
	{
	private String headers[],bodies[];
	private int currentScreen=0,n;

	private class Listener implements View.OnClickListener
		{
		@Override
		public void onClick(View v)
			{
			if(v.getId()==R.id.btnNext)
				currentScreen= (currentScreen+1)%n;
			else if(v.getId()==R.id.btnPrev)
				{
				if(currentScreen==0)
					currentScreen=n-1;
				else
					currentScreen--;
				}
			initViews();
			}
		}

	private void initViews()
		{
		TextView header= (TextView)findViewById(R.id.header);
		TextView body= (TextView)findViewById(R.id.body);

		header.setText(headers[currentScreen] );
		body.setText(bodies[currentScreen] );
		}

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState)
		{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_layout);

		Button btnNext=(Button) findViewById(R.id.btnNext);
		Button btnPrev=(Button) findViewById(R.id.btnPrev);

		btnNext.setOnClickListener(new Listener() );
		btnPrev.setOnClickListener(new Listener() );

		headers= getResources().getStringArray(R.array.help_headers);
		bodies= getResources().getStringArray(R.array.help_bodies);
		n=headers.length;
		initViews();
		}
	}
