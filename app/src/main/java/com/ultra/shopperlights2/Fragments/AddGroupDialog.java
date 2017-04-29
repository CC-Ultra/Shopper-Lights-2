package com.ultra.shopperlights2.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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

/**
 * <p></p>
 * <p><sub>(28.04.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class AddGroupDialog extends DialogFragment
	 {
	 private String title;
	 private EditText groupNameInput,groupPriorityInput;
	 private long groupId=0;

	 private class OkListener implements View.OnClickListener
		 {
		 @Override
		 public void onClick(View v)
			 {
			 String groupName= groupNameInput.getText().toString();
			 String groupPriority= groupPriorityInput.getText().toString();
			 if(groupName.length() == 0 || groupPriority.length() == 0)
				 Toast.makeText(getContext(),"Не все поля заполнены",Toast.LENGTH_SHORT).show();
			 else
				 {
				 Group group;
				 GroupDao groupDao= App.session.getGroupDao();
				 if(groupId ==0)
					 group= new Group();
				 else
					 group= groupDao.load(groupId);
				 group.setTitle(groupName);
				 group.setPriority(Integer.parseInt(groupPriority) );
				 if(groupId ==0)
					 groupDao.insert(group);
				 else
					 groupDao.update(group);
				 dismiss();
				 }
			 }
		 }

	 public void init(String _title)
		 {
		 title=_title;
		 }
	 public void init(String _title,long _id)
		 {
		 groupId=_id;
		 title=_title;
		 }

	 @Nullable
	 @Override
	 public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,Bundle savedInstanceState)
		 {
		 getDialog().setTitle(title);
		 View mainView= inflater.inflate(R.layout.add_group_dialog_layout,container,false);

		 Button okBtn= (Button)mainView.findViewById(R.id.btnOk);
		 groupNameInput= (EditText)mainView.findViewById(R.id.titleInput);
		 groupPriorityInput= (EditText)mainView.findViewById(R.id.priorityInput);

		 if(groupId!=0)
			 {
			 Group group= App.session.getGroupDao().load(groupId);
			 groupNameInput.setText(group.getTitle() );
			 groupPriorityInput.setText(group.getPriority() );
			 }
		 okBtn.setOnClickListener(new OkListener() );
		 return mainView;
		 }
	 }
