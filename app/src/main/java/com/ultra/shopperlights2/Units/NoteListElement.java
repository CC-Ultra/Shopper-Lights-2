package com.ultra.shopperlights2.Units;

/**
 * <p></p>
 * <p><sub>(27.04.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class NoteListElement
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
	 }
