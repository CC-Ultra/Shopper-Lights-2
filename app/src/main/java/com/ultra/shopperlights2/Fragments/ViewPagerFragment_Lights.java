package com.ultra.shopperlights2.Fragments;

import android.app.PendingIntent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Utils.O;

/**
 * <p></p>
 * <p><sub>(06.05.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class ViewPagerFragment_Lights extends ViewPagerFragment_Basic
	{
	private ImageButton light_Red,light_Yellow,light_Green;
	public static int lightMemory;
	private PendingIntent pendingIntent;
	private ViewPager pager; //удалить

	private class LightsListener implements View.OnClickListener
		{
		@Override
		public void onClick(View v)
			{
			light_Red.setImageResource(R.drawable.yellow_light);
			light_Yellow.setImageResource(R.drawable.yellow_light);
			light_Green.setImageResource(R.drawable.yellow_light);
			ImageButton vbtn= (ImageButton)v;
			vbtn.setImageResource(R.drawable.red_light);
			try
				{
				switch(v.getId() )
					{
					case R.id.btnScreen_red:
						pendingIntent.send(O.interaction.SCREEN_CODE_RED);
						break;
					case R.id.btnScreen_yellow:
						pendingIntent.send(O.interaction.SCREEN_CODE_YELLOW);
						break;
					case R.id.btnScreen_green:
						pendingIntent.send(O.interaction.SCREEN_CODE_GREEN);
						break;
					}
				}
			catch(PendingIntent.CanceledException e)
				{
				Log.d(O.TAG,"onClick: canceledException");
				}
			}
		}

	public void init(PendingIntent _pendingIntent,ViewPager _pager)
		{
		pager=_pager; //удалить
		pendingIntent=_pendingIntent;
		}
	private void setLightToBtn()
		{
		switch(lightMemory)
			{
			case O.interaction.SCREEN_CODE_RED:
				light_Red.setImageResource(R.drawable.red_light);
				break;
			case O.interaction.SCREEN_CODE_YELLOW:
				light_Yellow.setImageResource(R.drawable.red_light);
				break;
			case O.interaction.SCREEN_CODE_GREEN:
				light_Green.setImageResource(R.drawable.red_light);
				break;
			}
		}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState)
		{
		mainView= inflater.inflate(R.layout.lights_buttons_fragment,container,false);
		Log.d(O.TAG,"onCreateView: Lights "+ this.toString() );
		if(savedInstanceState!=null)
			pendingIntent= savedInstanceState.getParcelable(O.mapKeys.extra.SAVED_STATE_PENDING_INTENT);

		light_Red= (ImageButton)mainView.findViewById(R.id.btnScreen_red);
		light_Yellow= (ImageButton)mainView.findViewById(R.id.btnScreen_yellow);
		light_Green= (ImageButton)mainView.findViewById(R.id.btnScreen_green);

		light_Red.setOnClickListener(new LightsListener() );
		light_Yellow.setOnClickListener(new LightsListener() );
		light_Green.setOnClickListener(new LightsListener() );
		setLightToBtn();

		ImageButton btnClose= (ImageButton)mainView.findViewById(R.id.btn_close); //удалить
		btnClose.setOnClickListener(new View.OnClickListener() //удалить
			{
			@Override//удалить
			public void onClick(View v)//удалить
				{
				pager.setCurrentItem(0);//удалить
				}
			});
		return mainView;
		}

	@Override
	public void onSaveInstanceState(Bundle outState)
		{
		super.onSaveInstanceState(outState);
		outState.putParcelable(O.mapKeys.extra.SAVED_STATE_PENDING_INTENT,pendingIntent);
		}
	}
