package com.ultra.shopperlights2.Widgets;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import com.ultra.shopperlights2.R;

/**
 * <p>Горизонтальный виджет</p>
 * <p><sub>(10.07.2017)</sub></p>
 * @author CC-Ultra
 */

public class AppWidgetLand extends AppWidgetBasic
	{
	@Override
	public void onUpdate(Context context,AppWidgetManager appWidgetManager,int[] appWidgetIds)
		{
		super.onUpdate(context,appWidgetManager,appWidgetIds,R.layout.widget_land);
		}
	}
