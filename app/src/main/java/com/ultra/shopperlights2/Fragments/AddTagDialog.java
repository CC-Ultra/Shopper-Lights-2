package com.ultra.shopperlights2.Fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.Tag;
import com.ultra.shopperlights2.Units.TagDao;
import com.ultra.shopperlights2.Utils.O;

/**
 * <p></p>
 * <p><sub>(27.04.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class AddTagDialog extends DialogFragment
	 {
	 private int r,g,b;
	 private View colorView;
	 private EditText tagNameInput;
	 private String title;
	 private long tagId=0;
	 private String action;

	 private class SeekBarListener implements SeekBar.OnSeekBarChangeListener
		 {
		 int seekbarColor;

		 public SeekBarListener(int _color)
			 {
			 seekbarColor=_color;
			 }

		 @Override
		 public void onProgressChanged(SeekBar seekBar,int progress,boolean fromUser)
			 {
			 switch(seekbarColor)
				 {
				 case O.COLOR_CODE_RED:
				 	 r=progress;
					 break;
				 case O.COLOR_CODE_GREEN:
				 	 g=progress;
					 break;
				 case O.COLOR_CODE_BLUE:
				 	 b=progress;
				 }
			 int resultColor= Color.rgb(r,g,b);
			 colorView.setBackgroundColor(resultColor);
			 }

		 @Override
		 public void onStartTrackingTouch(SeekBar seekBar) {}
		 @Override
		 public void onStopTrackingTouch(SeekBar seekBar) {}
		 }
	 private class OkListener implements View.OnClickListener
		 {
		 @Override
		 public void onClick(View v)
			 {
			 String tagName= tagNameInput.getText().toString();
			 if(tagName.length() == 0)
				 Toast.makeText(getContext(),"Введите имя тега",Toast.LENGTH_SHORT).show();
			 else
				 {
				 TagDao tagDao= App.session.getTagDao();
				 Tag tag;
				 if(tagId==0)
				 	tag= new Tag();
				 else
				 	tag= tagDao.load(tagId);
				 int size= tagDao.queryBuilder().where(TagDao.Properties.Title.eq(tagName) ).list().size();
				 if( (tagId==0 && size>0) || (tagId!=0 && size>0 && !tag.getTitle().equals(tagName) ) )
					 {
					 Toast.makeText(getContext(),"Имя тега занято",Toast.LENGTH_SHORT).show();
					 return;
					 }
				 tag.setTitle(tagName);
				 tag.setColor(Color.rgb(r,g,b) );
				 if(tagId==0)
					 tagDao.insert(tag);
				 else
					 tagDao.update(tag);
				 dismiss();
				 getContext().sendBroadcast(new Intent(action) );
				 }
			 }
		 }

	 public void init(String _action,String _title)
		 {
		 action=_action;
		 title=_title;
		 }
	 public void init(String _action,String _title,long _id)
		 {
		 action=_action;
		 tagId=_id;
		 title=_title;
		 }

	 @Nullable
	 @Override
	 public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,Bundle savedInstanceState)
		 {
		 getDialog().setTitle(title);
		 View mainView= inflater.inflate(R.layout.add_tag_dialog_layout,container,false);
		 if(savedInstanceState!=null)
			 {
			 tagId= savedInstanceState.getLong(O.mapKeys.savedState.SAVED_STATE_TAG_ID,0);
			 action= savedInstanceState.getString(O.mapKeys.savedState.SAVED_STATE_ACTION);
			 }

		 SeekBar seekBar_Red= (SeekBar)mainView.findViewById(R.id.seekBarRed);
		 SeekBar seekBar_Green= (SeekBar)mainView.findViewById(R.id.seekBarGreen);
		 SeekBar seekBar_Blue= (SeekBar)mainView.findViewById(R.id.seekBarBlue);
		 Button okBtn= (Button)mainView.findViewById(R.id.btnOk);
		 colorView= mainView.findViewById(R.id.colorView);
		 tagNameInput= (EditText)mainView.findViewById(R.id.titleInput);

		 seekBar_Red.setMax(255);
		 seekBar_Green.setMax(255);
		 seekBar_Blue.setMax(255);
		 seekBar_Red.getProgressDrawable().setColorFilter(Color.RED,PorterDuff.Mode.SRC_ATOP);
		 seekBar_Green.getProgressDrawable().setColorFilter(Color.GREEN,PorterDuff.Mode.SRC_ATOP);
		 seekBar_Blue.getProgressDrawable().setColorFilter(Color.BLUE,PorterDuff.Mode.SRC_ATOP);
		 seekBar_Red.setOnSeekBarChangeListener(new SeekBarListener(O.COLOR_CODE_RED) );
		 seekBar_Green.setOnSeekBarChangeListener(new SeekBarListener(O.COLOR_CODE_GREEN) );
		 seekBar_Blue.setOnSeekBarChangeListener(new SeekBarListener(O.COLOR_CODE_BLUE) );
		 okBtn.setOnClickListener(new OkListener() );

		 if(tagId==0)
			 colorView.setBackgroundColor(Color.rgb(0,0,0) );
		 else
			 {
			 Tag tag= App.session.getTagDao().load(tagId);
			 tagNameInput.setText(tag.getTitle() );
			 colorView.setBackgroundColor(tag.getColor() );
			 seekBar_Red.setProgress(Color.red(tag.getColor() ) );
			 seekBar_Green.setProgress(Color.green(tag.getColor() ) );
			 seekBar_Blue.setProgress(Color.blue(tag.getColor() ) );
			 }
		 return mainView;
		 }
	 @Override
	 public void onSaveInstanceState(Bundle outState)
		 {
		 if(tagId!=0)
			 outState.putLong(O.mapKeys.savedState.SAVED_STATE_TAG_ID,tagId);
		 outState.putString(O.mapKeys.savedState.SAVED_STATE_ACTION,action);
		 super.onSaveInstanceState(outState);
		 }
	 }
