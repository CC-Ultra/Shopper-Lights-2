package com.ultra.shopperlights2.Units;

import java.util.ArrayList;

/**
 * <p></p>
 * <p><sub>(28.08.2016)</sub></p>
 *
 * @author CC-Ultra
 */
public class NoteRecord
	{
	 private String title;
	 private int n;
	 public boolean isGroup=false,isOpen=false;
	 private ArrayList<Tag> tags;

	 public ArrayList<Tag> getTags()
		{
		 return tags;
		 }
	 public String getTitle()
		{
		 return title;
		 }
	 public int getN()
		{
		 return n;
		 }
	 public void setTitle(String _title)
		{
		 title=_title;
		 }
	 public void setN(int _n)
		{
		 n=_n;
		 }
	 public void setTags(ArrayList<Tag> _tags)
		{
		 tags=_tags;
		 }
	 }
