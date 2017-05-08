package com.ultra.shopperlights2.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import com.ultra.shopperlights2.R;


/**
 * <p></p>
 * <p><sub>(06.05.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class ViewPagerFragment_Arrow extends ViewPagerFragment_Basic
	{
	private ViewPager pager;//удалить
	public void init(ViewPager _pager) { pager=_pager; }//удалить
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState)
		{
		mainView= inflater.inflate(R.layout.arrow_to_lights_button_fragment,container,false);
		ImageButton btnOpen= (ImageButton) mainView.findViewById(R.id.btnOpen);//удалить
		btnOpen.setOnClickListener(new View.OnClickListener()//удалить
			{
			@Override//удалить
			public void onClick(View v)//удалить
				{
				pager.setCurrentItem(1);//удалить
				}
			});
		return mainView;
		}
	}
