package com.ultra.shopperlights2.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.Group;
import com.ultra.shopperlights2.Units.GroupDao;
import com.ultra.shopperlights2.Units.Template;
import com.ultra.shopperlights2.Units.TemplateDao;
import com.ultra.shopperlights2.Utils.O;

/**
 * <p></p>
 * <p><sub>(28.04.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class AddTemplateDialog extends DialogFragment
	 {
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
		 templateId=_id;
		 title=_title;
		 }

	 @Nullable
	 @Override
	 public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,Bundle savedInstanceState)
		 {
		 View mainView= inflater.inflate(R.layout.add_template_dialog_layout,container,false);
		 if(savedInstanceState!=null)
			 {
			 templateId= savedInstanceState.getLong(O.mapKeys.savedState.SAVED_STATE_TEMPLATE_ID,0);
			 action= savedInstanceState.getString(O.mapKeys.savedState.SAVED_STATE_ACTION);
			 title= savedInstanceState.getString(O.mapKeys.savedState.SAVED_STATE_TITLE);
			 }
		 getDialog().setTitle(title);

		 Button okBtn= (Button)mainView.findViewById(R.id.btnOk);
		 inputTemplateName= (EditText)mainView.findViewById(R.id.titleInput);

		 if(templateId!=0)
			 {
			 Template template= App.session.getTemplateDao().load(templateId);
			 inputTemplateName.setText(template.getTitle() );
			 }
		 okBtn.setOnClickListener(new OkListener() );
		 return mainView;
		 }
	 @Override
	 public void onSaveInstanceState(Bundle outState)
		 {
		 if(templateId !=0)
			 outState.putLong(O.mapKeys.savedState.SAVED_STATE_TEMPLATE_ID,templateId);
		 outState.putString(O.mapKeys.savedState.SAVED_STATE_ACTION,action);
		 outState.putString(O.mapKeys.savedState.SAVED_STATE_TITLE,title);
		 super.onSaveInstanceState(outState);
		 }
	 }
