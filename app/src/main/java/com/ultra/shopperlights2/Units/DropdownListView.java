package com.ultra.shopperlights2.Units;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ToggleButton;
import com.ultra.shopperlights2.R;

/**
 * TODO: document your custom view class.
 */
public class DropdownListView extends LinearLayout
	 {
	 private String title;
	 private ToggleButton btnTitle;
	 private ListView list;

	 private class ToggleListener implements OnClickListener
		 {
		 @Override
		 public void onClick(View v)
			 {
			 if(btnTitle.isChecked() )
				 list.setVisibility(VISIBLE);
			 else
				 list.setVisibility(GONE);
			 }
		 }

	 public DropdownListView(Context context)
		 {
		 super(context);
		 init(null,0);
		 }
	 public DropdownListView(Context context,AttributeSet attrs)
		 {
		 super(context,attrs);
		 init(attrs,0);
		 }
	 private void init(AttributeSet attrs,int defStyle)
		 {
		 // Load attributes
		 final TypedArray a=null;
//		 getContext().obtainStyledAttributes(attrs,R.styleable.DropdownListView,defStyle,0);

		 title=a.getString(R.styleable.DropdownListView_title1);
		 a.recycle();
		 LayoutInflater inflater= (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 inflater.inflate(R.layout.dropdown_listview, this, true);
		 btnTitle= (ToggleButton)findViewById(R.id.btnTitle);
		 list= (ListView)findViewById(R.id.list);

		 list.setVisibility(GONE);
		 btnTitle.setChecked(false);
		 btnTitle.setText(title);
		 btnTitle.setTextOff(title);
		 btnTitle.setTextOn(title);
		 btnTitle.setOnClickListener(new ToggleListener() );
		 }
	 public String getTitle()
		 {
		 return title;
		 }
	 public void setTitle(String _title)
		 {
		 title=_title;
		 btnTitle.setTextOff(title);
		 btnTitle.setTextOn(title);
		 }
	 public void setAdapter(ListAdapter adapter)
		 {
		 list.setAdapter(adapter);
		 }
	 }
