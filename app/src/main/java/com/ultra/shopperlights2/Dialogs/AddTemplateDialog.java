package com.ultra.shopperlights2.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.Template;
import com.ultra.shopperlights2.Units.TemplateDao;
import com.ultra.shopperlights2.Utils.O;

/**
 * <p>Класс-обертка над диалогом добавления/редактирования шаблонов</p>
 * <p><sub>(28.04.2017)</sub></p>
 * @author CC-Ultra
 */

public class AddTemplateDialog
	 {
	 private Context context;
	 private AlertDialog dialog;
	 private ViewGroup parent;
	 private String title;
	 private EditText inputTemplateName;
	 private long templateId=0;
	 private String action;

	 private class OkListener implements View.OnClickListener
		 {
		 @Override
		 public void onClick(View v)
			 {
			 String templateName= inputTemplateName.getText().toString();
			 if(templateName.length() == 0)
				 inputTemplateName.setError("Введи имя шаблона");
			 else
				 {
				 Template template;
				 TemplateDao templateDao= App.session.getTemplateDao();
				 if(templateId ==0)
					 template= new Template();
				 else
					 template= templateDao.load(templateId);
				 //если создается новый шаблон, а такой уже есть, ИЛИ
				 //если изменяется, а имеющийся шаблон отличается от загруженного по id
				 int size= templateDao.queryBuilder().where(TemplateDao.Properties.Title.eq(templateName) ).list().size();
				 if( (templateId==0 && size>0) || (templateId !=0 && size>0 && !template.getTitle().equals(templateName) ) )
					 {
					 inputTemplateName.setError("Это имя уже занято");
					 return;
					 }
				 template.setTitle(templateName);
				 if(templateId==0)
					 templateDao.insert(template);
				 else
					 templateDao.update(template);
				 dialog.dismiss();
				 context.sendBroadcast(new Intent(action) );
				 }
			 }
		 }

	 /**
	  * создать шаблон
	  */
	 public void init(Context _context,ViewGroup _parent,String _action,String _title)
		 {
		 context=_context;
		 parent=_parent;
		 action=_action;
		 title=_title;
		 }

	 /**
	  * изменить шаблон
	  */
	 public void init(Context _context,ViewGroup _parent,String _action,String _title,long _id)
		 {
		 context=_context;
		 parent=_parent;
		 action=_action;
		 templateId=_id;
		 title=_title;
		 }

	 /**
	  * Инициализация mainView и передача ее диалогу
	  */
	 public void createAndShow()
		 {
		 View mainView= LayoutInflater.from(context).inflate(R.layout.add_template_dialog_layout,parent,false);
		 AlertDialog.Builder builder= new AlertDialog.Builder(context);
		 builder.setTitle(title);

		 Button okBtn= (Button)mainView.findViewById(R.id.btnOk);
		 inputTemplateName= (EditText)mainView.findViewById(R.id.titleInput);

		 if(templateId!=0)
			 {
			 Template template= App.session.getTemplateDao().load(templateId);
			 inputTemplateName.setText(template.getTitle() );
			 }
		 okBtn.setOnClickListener(new OkListener() );

		 builder.setView(mainView);
		 dialog= builder.create();
		 dialog.show();
		 }
	 }
