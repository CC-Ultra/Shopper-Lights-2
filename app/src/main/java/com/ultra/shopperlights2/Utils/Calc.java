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
	public static int dpToPx(Context context,int dp)
		{
		float scale= context.getResources().getDisplayMetrics().density;
		return (int)(dp * scale + 0.5f);
		}
	}
