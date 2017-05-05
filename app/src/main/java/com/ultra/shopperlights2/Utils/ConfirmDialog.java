package com.ultra.shopperlights2.Utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import com.ultra.shopperlights2.Callbacks.DialogDecision;

/**
 * <p>Простой диалог {@code y/n}, который вызывается в одну строчку, как какой-нибудь {@code Toast}</p>
 * Реализован на базе {@link AlertDialog} и требует для работы объекта, реализующего интерфейс callback-а {@link DialogDecision},
 * чтобы по нажатию на какую-то кнопку сделать нужное действие. Все. Остальное он берет на себя
 * <p><sub>(30.03.2016)</sub></p>
 * @author CC-Ultra
 * @see DialogDecision
 */
public class ConfirmDialog
	 {
	 AppCompatActivity activity;
	 DialogDecision parent;
	 int yesId,noId=-1;

	 /**
	  * {@link #yesId} связан с реализацией метода {@link DialogDecision#sayYes(int)}, указывает какому именно действию было
	  * сказано {@code Да}
	  */
	 static class YesListener implements DialogInterface.OnClickListener
		 {
		 DialogDecision parent;
		 int yesId;
		 YesListener(DialogDecision _parent,int _yesId)
			 {
			 parent=_parent;
			 yesId=_yesId;
			 }
		 @Override
		 public void onClick(DialogInterface dialog,int which)
			 {
			 parent.sayYes(yesId);
			 }
		 }

	 /**
	  * {@link #noId} связан с реализацией метода {@link DialogDecision#sayNo(int)}, указывает какому именно действию было
	  * сказано {@code Нет}
	  */
	 static class NoListener implements DialogInterface.OnClickListener
		 {
		 DialogDecision parent;
		 int noId;

		 NoListener(DialogDecision _parent,int _noId)
			 {
			 parent=_parent;
			 noId=_noId;
			 }
		 @Override
		 public void onClick(DialogInterface dialog,int which)
			 {
			 parent.sayNo(noId);
			 }
		 }

	 /**
	  * Конструирование и запуск нового {@link AlertDialog}
	  */
	 public static void ask(Context context,DialogDecision parent,int yesId,int noId)
		 {
		 AlertDialog.Builder adb= new AlertDialog.Builder(context);
		 adb.setMessage("Ты точно уверен?");
		 adb.setPositiveButton("Конечно!",new YesListener(parent,yesId) );
		 adb.setNegativeButton("Не надо",new NoListener(parent,noId) );
		 adb.create().show();
		 }
	 }