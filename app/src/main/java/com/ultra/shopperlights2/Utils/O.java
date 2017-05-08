package com.ultra.shopperlights2.Utils;

/**
 * <p></p>
 * <p><sub>(07.08.2016)</sub></p>
 *
 * @author CC-Ultra
 */
public class O
	{
	 public static final String TAG="c123";

	 public static final int COLOR_CODE_RED=0;
	 public static final int COLOR_CODE_GREEN=1;
	 public static final int COLOR_CODE_BLUE=2;

	 public static class interaction
		{
		 public static final int SCREEN_CODE_GREEN=0;
		 public static final int SCREEN_CODE_YELLOW=1;
		 public static final int SCREEN_CODE_RED=2;
		 public static final int ELEMENT_TYPE_GROUP=0;
		 public static final int ELEMENT_TYPE_TAG=1;
		 public static final int ELEMENT_TYPE_SHOP=2;
		 }

	 public static class db
		{
		 public static final String DB_FILENAME="ShopperLights.db";
		 }

	 public static class dimens
		{
		 public static final int LIGHTS_PAGER_WIDTH_PORT=100;
		 public static final int LIGHTS_PAGER_WIDTH_LAND=155;
		 public static final int ARROW_PAGER_WIDTH=25;
		 }

	 public static class mapKeys
		{
		 public static class extra
			{
			 public static final String SAVED_STATE_PENDING_INTENT="Pending intent";
			 public static final String START_COLOR="Start color";
			 public static final String LIST_ELEMENT_TYPE="List element type";
			 }

		 public static class prefs
			{
			 ;
			 }
		 }
	 }
