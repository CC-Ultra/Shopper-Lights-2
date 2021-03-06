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
import android.widget.Toast;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.Group;
import com.ultra.shopperlights2.Units.GroupDao;
import com.ultra.shopperlights2.Utils.O;

/**
 * <p>Класс-обертка над диалогом добавления/редактирования группы</p>
 * <p><sub>(28.04.2017)</sub></p>
 * @author CC-Ultra
 */

public class AddGroupDialog
	 {
	 private Context context;
	 private AlertDialog dialog;
	 private ViewGroup parent;
	 private String title;
	 private EditText inputGroupName, inputGroupPriority;
	 private long groupId=0;
	 private String action;

	 private class OkListener implements View.OnClickListener
		 {
		 @Override
		 public void onClick(View v)
			 {
			 String groupName= inputGroupName.getText().toString();
			 String groupPriority= inputGroupPriority.getText().toString();
			 if(groupName.length() == 0 || groupPriority.length() == 0)
				 Toast.makeText(context,"Не все поля заполнены",Toast.LENGTH_SHORT).show();
			 else //если оба поля нормально заполнены
				 {
				 Group group;
				 GroupDao groupDao= App.session.getGroupDao();
				 if(groupId==0)
					 group= new Group();
				 else
					 group= groupDao.load(groupId);
				 //size - количество групп с таким именем
				 int size= groupDao.queryBuilder().where(GroupDao.Properties.Title.eq(groupName) ).list().size();
				 //если это создание группы и кто-то уже есть с таким именем
				 //если это редактирование группы и имя совпадает с тем, что уже было
				 if( (groupId==0 && size>0) || (groupId!=0 && !group.getTitle().equals(groupName) ) )
					 {
					 inputGroupName.setError("Это имя уже занято");
					 return;
					 }
				 group.setTitle(groupName);
				 group.setPriority(Integer.parseInt(groupPriority) );
				 if(groupId==0)
					 groupDao.insert(group);
				 else
					 groupDao.update(group);
				 dialog.dismiss();
				 context.sendBroadcast(new Intent(action) ); //чтобы обновился список в вызывающей активности/фрагменте
				 }
			 }
		 }

	 /**
	  * добавить группу
	  */
	 public void init(Context _context,ViewGroup _parent,String _action,String _title)
		 {
		 context=_context;
		 parent=_parent;
		 action=_action;
		 title=_title;
		 }

	 /**
	  * Изменить группу
	  */
	 public void init(Context _context,ViewGroup _parent,String _action,String _title,long _id)
		 {
		 context=_context;
		 parent=_parent;
		 action=_action;
		 groupId=_id;
		 title=_title;
		 }

	 /**
	  * инициализация mainView и передача ее диалогу
	  */
	 public void createAndShow()
		 {
		 View mainView= LayoutInflater.from(context).inflate(R.layout.add_group_dialog_layout,parent,false);
		 AlertDialog.Builder builder= new AlertDialog.Builder(context);
		 builder.setTitle(title);

		 Button okBtn= (Button)mainView.findViewById(R.id.btnOk);
		 inputGroupName= (EditText)mainView.findViewById(R.id.titleInput);
		 inputGroupPriority= (EditText)mainView.findViewById(R.id.priorityInput);

		 if(groupId!=0)
			 {
			 Group group= App.session.getGroupDao().load(groupId);
			 inputGroupName.setText(group.getTitle() );
			 inputGroupPriority.setText(group.getPriority() +"");
			 }
		 okBtn.setOnClickListener(new OkListener() );

		 builder.setView(mainView);
		 dialog= builder.create();
		 dialog.show();
		 }
	 }
