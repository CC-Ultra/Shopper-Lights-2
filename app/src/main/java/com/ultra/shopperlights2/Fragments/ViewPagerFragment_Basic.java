package com.ultra.shopperlights2.Fragments;

import android.app.PendingIntent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * <p></p>
 * <p><sub>(06.05.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class ViewPagerFragment_Basic extends Fragment
	{
	protected View mainView;
	protected PendingIntent pendingIntent;

	public void init(PendingIntent _pendingIntent)
		{
		pendingIntent=_pendingIntent;
		}
	}
