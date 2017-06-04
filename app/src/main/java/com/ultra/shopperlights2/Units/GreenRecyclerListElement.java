package com.ultra.shopperlights2.Units;

import com.ultra.shopperlights2.Utils.O;

/**
 * <p></p>
 * <p><sub>(27.04.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class GreenRecyclerListElement
	 {
	 private boolean tabbed;

	 public void setTabbed(boolean _tabbed)
		 {
		 tabbed=_tabbed;
		 }
	 public boolean isTabbed()
		 {
		 return tabbed;
		 }
	 public boolean isGroup()
		 {
		 if(this instanceof Note)
		 	return false;
		 else if(this instanceof Group)
		 	return true;
		 return false;
		 }
	 public int getGTSType()
		 {
		 if(this instanceof Tag)
		 	return O.interaction.ELEMENT_TYPE_TAG;
		 else if(this instanceof Group)
		 	return O.interaction.ELEMENT_TYPE_GROUP;
		 else if(this instanceof Shop)
		 	return O.interaction.ELEMENT_TYPE_SHOP;
		 return -1;
		 }
	 }
