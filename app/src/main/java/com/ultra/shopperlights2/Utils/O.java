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

	 public static class actions
		 {
		 public static final String ACTION_EDIT_LIST_ACTIVITY= "com.ultra.shopperlights2.Activities.EditListActivity";
		 public static final String ACTION_FRAGMENT_GREEN= "com.ultra.shopperlights2.Fragments.Fragment_Green";
		 public static final String ACTION_FRAGMENT_YELLOW_SHOP= "com.ultra.shopperlights2.Fragments.Fragment_Yellow_Shop";
		 public static final String ACTION_FRAGMENT_YELLOW_PURCHASE= "com.ultra.shopperlights2.Fragments.Fragment_Yellow_Purchase";
		 }

	 public static class interaction
		{
		 public static final int RESULT_CODE_ARROW=0;
		 public static final int RESULT_CODE_LIGHTS=1;
		 public static final int SCREEN_CODE_GREEN=0;
		 public static final int SCREEN_CODE_YELLOW=1;
		 public static final int SCREEN_CODE_RED=2;
		 public static final int ELEMENT_TYPE_GROUP=0;
		 public static final int ELEMENT_TYPE_TAG=1;
		 public static final int ELEMENT_TYPE_SHOP=2;
		 }

	 public static class dimens
		{
		 public static final int GREEN_DROPDOWN_TABBED_N_MARGIN=180;
		 public static final int GREEN_DROPDOWN_TABBED_TITLE_WIDTH=170;
		 public static final int GREEN_DROPDOWN_ELEMENT_HEIGHT=80;
		 public static final int GREEN_DROPDOWN_TAB=50;
		 public static final int LIGHTS_PAGER_WIDTH_PORT=100;
		 public static final int LIGHTS_PAGER_WIDTH_LAND=155;
		 public static final int ARROW_PAGER_WIDTH=25;
		 }

	 public static class mapKeys
		{
		 public static class savedState
			{
			 public static final String SAVED_STATE_SHOP_ID="Shop id";
			 public static final String SAVED_STATE_NOTE_ID="Note id";
			 public static final String SAVED_STATE_TAG_ID="Tag id";
			 public static final String SAVED_STATE_GROUP_ID="Group id";
			 public static final String SAVED_STATE_ACTION="Action";
			 public static final String SAVED_STATE_PRODUCT_ID="Product id";
			 public static final String SAVED_STATE_PENDING_INTENT="Pending intent";
			 }
		 public static class extra
			{
			 public static final String PAGER_ORDER="Pager order";
			 public static final String SCREEN_CODE="Screen code";
			 public static final String START_COLOR="Start color";
			 public static final String LIST_ELEMENT_TYPE="List element type";
			 }

		 public static class prefs
			{
			 ;
			 }
		 }
	 }
