package com.ultra.shopperlights2.Widgets;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import com.ultra.shopperlights2.R;

/**
 * <p>Вертикальный виджет</p>
 * <p><sub>(10.07.2017)</sub></p>
 * @author CC-Ultra
 */

public class AppWidgetPort extends AppWidgetBasic
	{
	@Override
	public void onUpdate(Context context,AppWidgetManager appWidgetManager,int[] appWidgetIds)
		{
		super.onUpdate(context,appWidgetManager,appWidgetIds,R.layout.widget_port);
		}
	}
