package com.ultra.shopperlights2.Fragments;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Utils.O;

import static com.ultra.shopperlights2.Utils.O.TAG;

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

	private class CloseListener implements View.OnClickListener
		{
		@Override
		public void onClick(View v)
			{
			Intent intent= new Intent();
			intent.putExtra(O.mapKeys.extra.PAGER_ORDER,false);
			try
				{
				pendingIntent.send(getContext(),O.interaction.RESULT_CODE_ARROW,intent);
				}
			catch(PendingIntent.CanceledException e)
				{
				Log.d(TAG,"onClick: CanceledException");
				}
			}
		}
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
				Intent intent= new Intent();
				switch(v.getId() )
					{
					case R.id.btnScreen_red:
						intent.putExtra(O.mapKeys.extra.SCREEN_CODE,O.interaction.SCREEN_CODE_RED);
						break;
					case R.id.btnScreen_yellow:
						intent.putExtra(O.mapKeys.extra.SCREEN_CODE,O.interaction.SCREEN_CODE_YELLOW);
						break;
					case R.id.btnScreen_green:
						intent.putExtra(O.mapKeys.extra.SCREEN_CODE,O.interaction.SCREEN_CODE_GREEN);
						break;
					}
				pendingIntent.send(getContext(),O.interaction.RESULT_CODE_LIGHTS,intent);
				}
			catch(PendingIntent.CanceledException e)
				{
				Log.d(TAG,"onClick: canceledException");
				}
			}
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
		if(savedInstanceState!=null)
			pendingIntent= savedInstanceState.getParcelable(O.mapKeys.savedState.SAVED_STATE_PENDING_INTENT);

		light_Red= (ImageButton)mainView.findViewById(R.id.btnScreen_red);
		light_Yellow= (ImageButton)mainView.findViewById(R.id.btnScreen_yellow);
		light_Green= (ImageButton)mainView.findViewById(R.id.btnScreen_green);
		ImageButton btnClose= (ImageButton)mainView.findViewById(R.id.btn_close);

		btnClose.setOnClickListener(new CloseListener() );
		light_Red.setOnClickListener(new LightsListener() );
		light_Yellow.setOnClickListener(new LightsListener() );
		light_Green.setOnClickListener(new LightsListener() );
		setLightToBtn();

		return mainView;
		}

	@Override
	public void onSaveInstanceState(Bundle outState)
		{
		outState.putParcelable(O.mapKeys.savedState.SAVED_STATE_PENDING_INTENT,pendingIntent);
		super.onSaveInstanceState(outState);
		}
	}
