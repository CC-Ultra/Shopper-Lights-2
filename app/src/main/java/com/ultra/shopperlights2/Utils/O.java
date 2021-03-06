package com.ultra.shopperlights2.Utils;

/**
 * <p>Класс хранилище констант</p>
 * <p><sub>(07.08.2016)</sub></p>
 * @author CC-Ultra
 */
public class O
	 {
	 public static final String TAG="c123";

	 public static final String PREFS_FILENAME="Ultra_prefs";
	 public static final String PREFS_FIRST_START="FirstStart";
	 public static final String TEMP_TEMPLATE_NAME="955653Template";
	 public static final String TRANSPORT_TAG_NAME="955653Transport";

	 public static class actions
		 {
		 public static final String ACTION_EDIT_PURCHASE_ACTIVITY= "com.ultra.shopperlights2.Activities.EditPurchaseActivity";
		 public static final String ACTION_ADD_TEMPLATE_NOTE_ACTIVITY= "com.ultra.shopperlights2.Activities.AddTemplateNoteActivity";
		 public static final String ACTION_ADD_TEMPLATE_ACTIVITY= "com.ultra.shopperlights2.Activities.AddTemplateActivity";
		 public static final String ACTION_EDIT_LIST_ACTIVITY= "com.ultra.shopperlights2.Activities.GTSListActivity";
		 public static final String ACTION_FRAGMENT_GREEN= "com.ultra.shopperlights2.Fragments.Fragment_Green";
		 public static final String ACTION_FRAGMENT_YELLOW_SHOP= "com.ultra.shopperlights2.Fragments.Fragment_Yellow_Shop";
		 public static final String ACTION_FRAGMENT_YELLOW_PURCHASE= "com.ultra.shopperlights2.Fragments.Fragment_Yellow_Purchase";
		 }

	 public static class interaction
		{
		 public static final int STAT_CODE_EDIT_HISTORY=0;
		 public static final int STAT_CODE_TAG_STAT=1;
		 public static final int STAT_CODE_MOST_REQUIRED=2;
		 public static final int STAT_CODE_SEARCH=3;
		 public static final int STAT_CODE_COST_DINAMICS=4;

		 public static final int COLOR_CODE_RED=0;
		 public static final int COLOR_CODE_GREEN=1;
		 public static final int COLOR_CODE_BLUE=2;

		 public static final int SRC_TYPE_CODE_GROUPS=0;
		 public static final int SRC_TYPE_CODE_TEMPLATES=1;
		 public static final int SRC_TYPE_CODE_HISTORY=2;

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
		 public static final int GREEN_DROPDOWN_NO_TAB=2;
		 public static final int LIGHTS_PAGER_WIDTH_PORT=120;
		 public static final int LIGHTS_PAGER_WIDTH_LAND=235;
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
			 public static final String SAVED_STATE_TEMPLATE_ID="Template id";
			 public static final String SAVED_STATE_ACTION="Action";
			 public static final String SAVED_STATE_TITLE="Title";
			 public static final String SAVED_STATE_PRODUCT_ID="Product id";
			 public static final String SAVED_STATE_PENDING_INTENT="Pending intent";
			 }
		 public static class extra
			{
			 public static final String PRODUCT_DATES="Product dates";
			 public static final String PRODUCT_IDS="Product ids";
			 public static final String TAG_ID="Tag id";
			 public static final String PURCHASE_ID="Purchase id";
			 public static final String DATE_FROM="Date from";
			 public static final String DATE_TO="Date to";
			 public static final String TEMPLATE_ID="Template Id";
			 public static final String PAGER_ORDER="Pager order";
			 public static final String SCREEN_CODE="Screen code";
			 public static final String START_COLOR="Start color";
			 public static final String LIST_ELEMENT_TYPE="List element type";
			 }
		 }
	 }
