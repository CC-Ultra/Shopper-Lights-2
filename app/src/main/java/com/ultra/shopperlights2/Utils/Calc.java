package com.ultra.shopperlights2.Utils;

import android.content.Context;

/**
 * <p></p>
 * <p><sub>(06.05.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class Calc
	{
	public static float round(float x)
		{
		int k= (int)(x*100);
		return (float)k / 100;
		}
	public static int dpToPx(Context context,int dp)
		{
		float scale= context.getResources().getDisplayMetrics().density;
		return (int)(dp * scale + 0.5f);
		}
	}
