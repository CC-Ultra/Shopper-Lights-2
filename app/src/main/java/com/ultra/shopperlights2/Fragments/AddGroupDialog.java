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
import android.widget.Toast;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.Group;
import com.ultra.shopperlights2.Units.GroupDao;
import com.ultra.shopperlights2.Utils.O;

/**
 * <p></p>
 * <p><sub>(28.04.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class AddGroupDialog extends DialogFragment
	 {
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
				 Toast.makeText(getContext(),"Не все поля заполнены",Toast.LENGTH_SHORT).show();
			 else
				 {
				 Group group;
				 GroupDao groupDao= App.session.getGroupDao();
				 if(groupId ==0)
					 group= new Group();
				 else
					 group= groupDao.load(groupId);
				 int size= groupDao.queryBuilder().where(GroupDao.Properties.Title.eq(groupName) ).list().size();
				 if( (groupId==0 && size>0) || (groupId!=0 && size>0 && !group.getTitle().equals(groupName) ) )
					 {
					 inputGroupName.setError("Это имя уже занято");
					 return;
					 }
				 group.setTitle(groupName);
				 group.setPriority(Integer.parseInt(groupPriority) );
				 if(groupId ==0)
					 groupDao.insert(group);
				 else
					 groupDao.update(group);
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
		 groupId=_id;
		 title=_title;
		 }

	 @Nullable
	 @Override
	 public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,Bundle savedInstanceState)
		 {
		 View mainView= inflater.inflate(R.layout.add_group_dialog_layout,container,false);
		 if(savedInstanceState!=null)
			 {
			 groupId= savedInstanceState.getLong(O.mapKeys.savedState.SAVED_STATE_GROUP_ID,0);
			 action= savedInstanceState.getString(O.mapKeys.savedState.SAVED_STATE_ACTION);
			 title= savedInstanceState.getString(O.mapKeys.savedState.SAVED_STATE_TITLE);
			 }
		 getDialog().setTitle(title);

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
		 return mainView;
		 }
	 @Override
	 public void onSaveInstanceState(Bundle outState)
		 {
		 if(groupId!=0)
			 outState.putLong(O.mapKeys.savedState.SAVED_STATE_GROUP_ID,groupId);
		 outState.putString(O.mapKeys.savedState.SAVED_STATE_ACTION,action);
		 outState.putString(O.mapKeys.savedState.SAVED_STATE_TITLE,title);
		 super.onSaveInstanceState(outState);
		 }
	 }
