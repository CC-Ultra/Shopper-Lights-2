package com.ultra.shopperlights2.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.Tag;
import com.ultra.shopperlights2.Units.TagDao;
import com.ultra.shopperlights2.Utils.O;

/**
 * <p>Класс-обертка над диалогом добавления/редактирования тега.</p>
 * <p><sub>(27.04.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class AddTagDialog
	 {
	 private Context context;
	 private AlertDialog dialog;
	 private ViewGroup parent;
	 private int r,g,b;
	 private View colorView;
	 private EditText inputTagName;
	 private String title;
	 private long tagId=0;
	 private String action;

	 /**
	  * Каждый {@code SeekBarListener} инициализирован своим цветом, прогресс которого и изменяется. Каждый делает свой цветной
	  * вклад в строящийся в конце цвет
	  */
	 private class SeekBarListener implements SeekBar.OnSeekBarChangeListener
		 {
		 int seekbarColor;

		 SeekBarListener(int _color)
			 {
			 seekbarColor=_color;
			 }

		 @Override
		 public void onProgressChanged(SeekBar seekBar,int progress,boolean fromUser)
			 {
			 switch(seekbarColor)
				 {
				 case O.interaction.COLOR_CODE_RED:
				 	 r=progress;
					 break;
				 case O.interaction.COLOR_CODE_GREEN:
				 	 g=progress;
					 break;
				 case O.interaction.COLOR_CODE_BLUE:
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
			 String tagName= inputTagName.getText().toString();
			 if(tagName.length() == 0)
				 inputTagName.setError("Введите имя тега");
			 else
				 {
				 TagDao tagDao=App.session.getTagDao();
				 Tag tag;
				 if(tagId == 0)
					 tag=new Tag();
				 else
					 tag=tagDao.load(tagId);
				 int size=tagDao.queryBuilder().where(TagDao.Properties.Title.eq(tagName)).list().size();
				 //если это создание тега и такой уже есть ИЛИ
				 //это редктирование, такой тег уже есть и он отличается от загруженного по id
				 if((tagId == 0 && size>0) || (tagId != 0 && size>0 && !tag.getTitle().equals(tagName)))
					 {
					 inputTagName.setError("Имя тега занято");
					 return;
					 }
				 tag.setTitle(tagName);
				 tag.setColor(Color.rgb(r,g,b));
				 if(tagId == 0)
					 tagDao.insert(tag);
				 else
					 tagDao.update(tag);
				 dialog.dismiss();
				 context.sendBroadcast(new Intent(action) ); //чтобы обновить список в вызывающей активности/фрагменте
				 }
			 }
		 }

	 /**
	  * создать тег
	  */
	 public void init(Context _context,ViewGroup _parent,String _action,String _title)
		 {
		 context=_context;
		 parent=_parent;
		 action=_action;
		 title=_title;
		 }

	 /**
	  * изменить тег
	  */
	 public void init(Context _context,ViewGroup _parent,String _action,String _title,long _id)
		 {
		 context=_context;
		 parent=_parent;
		 action=_action;
		 tagId=_id;
		 title=_title;
		 }

	 /**
	  * Инициализация mainView и передача ее диалогу. Попутно инициализация seekBar-ов, цветной view, listener-ов и т.д.
	  */
	 public void createAndShow()
		 {
		 View mainView= LayoutInflater.from(context).inflate(R.layout.add_tag_dialog_layout,parent,false);
		 AlertDialog.Builder builder= new AlertDialog.Builder(context);
		 builder.setTitle(title);

		 SeekBar seekBar_Red= (SeekBar)mainView.findViewById(R.id.seekBarRed);
		 SeekBar seekBar_Green= (SeekBar)mainView.findViewById(R.id.seekBarGreen);
		 SeekBar seekBar_Blue= (SeekBar)mainView.findViewById(R.id.seekBarBlue);
		 Button okBtn= (Button)mainView.findViewById(R.id.btnOk);
		 colorView= mainView.findViewById(R.id.colorView);
		 inputTagName= (EditText)mainView.findViewById(R.id.titleInput);

		 seekBar_Red.setMax(255);
		 seekBar_Green.setMax(255);
		 seekBar_Blue.setMax(255);
		 seekBar_Red.getProgressDrawable().setColorFilter(Color.RED,PorterDuff.Mode.SRC_ATOP);
		 seekBar_Green.getProgressDrawable().setColorFilter(Color.GREEN,PorterDuff.Mode.SRC_ATOP);
		 seekBar_Blue.getProgressDrawable().setColorFilter(Color.BLUE,PorterDuff.Mode.SRC_ATOP);
		 seekBar_Red.setOnSeekBarChangeListener(new SeekBarListener(O.interaction.COLOR_CODE_RED) );
		 seekBar_Green.setOnSeekBarChangeListener(new SeekBarListener(O.interaction.COLOR_CODE_GREEN) );
		 seekBar_Blue.setOnSeekBarChangeListener(new SeekBarListener(O.interaction.COLOR_CODE_BLUE) );
		 okBtn.setOnClickListener(new OkListener() );

		 if(tagId==0)
			 colorView.setBackgroundColor(Color.rgb(0,0,0) );
		 else
			 {
			 Tag tag= App.session.getTagDao().load(tagId);
			 inputTagName.setText(tag.getTitle() );
			 colorView.setBackgroundColor(tag.getColor() );
			 seekBar_Red.setProgress(Color.red(tag.getColor() ) );
			 seekBar_Green.setProgress(Color.green(tag.getColor() ) );
			 seekBar_Blue.setProgress(Color.blue(tag.getColor() ) );
			 }

		 builder.setView(mainView);
		 dialog= builder.create();
		 dialog.show();
		 }
	 }
