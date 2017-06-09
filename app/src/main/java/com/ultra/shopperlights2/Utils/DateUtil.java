package com.ultra.shopperlights2.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p></p>
 * <p><sub>(07.06.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class DateUtil
	{
	public static String getDateStr(Date date)
		{
		SimpleDateFormat format= new SimpleDateFormat("d MMM y");
		return format.format(date);
		}
	}
