package com.ultra.shopperlights2.Fragments;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Utils.O;

import static com.ultra.shopperlights2.Utils.O.TAG;


/**
 * <p>Фрагмент, на котором только стрелка</p>
 * <p><sub>(06.05.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class ViewPagerFragment_Arrow extends ViewPagerFragment_Basic
	{
	/**
	 * Listener, который через pendingIntent шлет сигнал о переключении на другую страницу
	 */
	private class OpenListener implements View.OnClickListener
		{
		@Override
		public void onClick(View v)
			{
			Intent intent= new Intent();
			intent.putExtra(O.mapKeys.extra.PAGER_ORDER,true);
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

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState)
		{
		mainView= inflater.inflate(R.layout.arrow_to_lights_button_fragment,container,false);
		if(savedInstanceState!=null)
			pendingIntent= savedInstanceState.getParcelable(O.mapKeys.savedState.SAVED_STATE_PENDING_INTENT);

		ImageButton btnOpen= (ImageButton)mainView.findViewById(R.id.btnOpen);
		btnOpen.setOnClickListener(new OpenListener() );

		return mainView;
		}
	@Override
	public void onSaveInstanceState(Bundle outState)
		{
		outState.putParcelable(O.mapKeys.savedState.SAVED_STATE_PENDING_INTENT,pendingIntent);
		super.onSaveInstanceState(outState);
		}
	}
