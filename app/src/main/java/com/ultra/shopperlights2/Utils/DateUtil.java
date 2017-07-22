package com.ultra.shopperlights2.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>Класс для работы с датами</p>
 * <p><sub>(07.06.2017)</sub></p>
 * @author CC-Ultra
 */

public class DateUtil
	{
	public static class DateDMY
		{
		public int day,month,year;
		}

	public static DateDMY getDMYfromDate(Date date)
		{
		DateDMY result= new DateDMY();
		Calendar calendar= Calendar.getInstance();
		if(date==null)
			calendar.setTime(new Date() );
		else
			calendar.setTime(date);
		result.day= calendar.get(Calendar.DAY_OF_MONTH);
		result.month= calendar.get(Calendar.MONTH);
		result.year= calendar.get(Calendar.YEAR);
		return result;
		}
	public static Date getDateFromDMY(int day,int month,int year)
		{
		Calendar calendar= Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH,day);
		calendar.set(Calendar.MONTH,month);
		calendar.set(Calendar.YEAR,year);
		return calendar.getTime();
		}
	public static String getDateStr(Date date)
		{
		SimpleDateFormat format= new SimpleDateFormat("d MMM y");
		return format.format(date);
		}
	}
