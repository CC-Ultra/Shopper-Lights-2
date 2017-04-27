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

	 public static class interaction
		{
		 public static final int SCREEN_CODE_GREEN=0;
		 public static final int SCREEN_CODE_YELLOW=1;
		 public static final int SCREEN_CODE_RED=2;
		 }

	 public static class db
		{
		 public static final String DB_FILENAME="ShopperLights.db";
		 public static final int DB_VERSION=1;

		 public static class tables
			{
			 public static final String PRODUCTS="products";
			 public static final String SHOPS="shops";
			 public static final String MANUFACTURERS="manufacturers";
			 public static final String TAGS="tags";
			 public static final String GROUPS="groups";
			 public static final String NOTES="notes";
			 public static final String PRODUCT_TO_TAG="product_to_tag";
			 public static final String PRODUCT_TO_SHOP="product_to_shop";
			 }
		 public static class fields
			{
			 public static final String ID="_id";
			 public static final String ID_TAG="id_tag";
			 public static final String ID_SHOP="id_shop";
			 public static final String ID_GROUP="id_group";
			 public static final String ID_MANUFACTURER="id_manufacturer";
			 public static final String ID_PRODUCT="id_product";
			 public static final String TITLE="title";
			 public static final String DATE="date";
			 public static final String PERCENT="percent";
			 public static final String WEIGHT="weight";
			 public static final String NUM_OF="num";
			 public static final String COLOR="color";
			 public static final String ADR="adr";
			 }
		 }

	 public static class prefs
		{
		 ;
		 }

	 public static class mapKeys
		{
		 public static class extra
			{
			 public static final String START_COLOR="Start color";
			 }

		 public static class prefs
			{
			 ;
			 }
		 }
	 }
