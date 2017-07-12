package com.ultra.shopperlights2.Widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import com.ultra.shopperlights2.Activities.MainActivity;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Utils.O;

/**
 * <p></p>
 * <p><sub>(10.07.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class AppWidgetBasic extends AppWidgetProvider
	{
	public void onUpdate(Context context,AppWidgetManager appWidgetManager,int[] appWidgetIds,int layoutId)
		{
		for(int appWidgetId : appWidgetIds)
			{
			Intent intentGreen= new Intent(context,MainActivity.class);
			intentGreen.putExtra(O.mapKeys.extra.START_COLOR,O.interaction.SCREEN_CODE_GREEN);
			PendingIntent pendingIntentGreen= PendingIntent.getActivity(context,3,intentGreen,0);
			Intent intentYellow= new Intent(context,MainActivity.class);
			intentYellow.putExtra(O.mapKeys.extra.START_COLOR,O.interaction.SCREEN_CODE_YELLOW);
			PendingIntent pendingIntentYellow= PendingIntent.getActivity(context,2,intentYellow,0);
			Intent intentRed= new Intent(context,MainActivity.class);
			intentRed.putExtra(O.mapKeys.extra.START_COLOR,O.interaction.SCREEN_CODE_RED);
			PendingIntent pendingIntentRed= PendingIntent.getActivity(context,1,intentRed,0);
			RemoteViews rw= new RemoteViews(context.getPackageName(),layoutId);
			rw.setOnClickPendingIntent(R.id.greenLigt,pendingIntentGreen);
			rw.setOnClickPendingIntent(R.id.yellowLigt,pendingIntentYellow);
			rw.setOnClickPendingIntent(R.id.redLigt,pendingIntentRed);
			appWidgetManager.updateAppWidget(appWidgetId,rw);
			}
		}
	}
