package com.ultra.shopperlights2.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.ultra.shopperlights2.R;

/**
 * <p></p>
 * <p><sub>(07.08.2016)</sub></p>
 *
 * @author CC-Ultra
 */
public class Fragment_Green extends Fragment
	{
	 private class AddTagListener implements View.OnClickListener
		 {
		 @Override
		 public void onClick(View v)
			 {
			 AddTagDialog dialog= new AddTagDialog();
			 dialog.init("Создать тег");
			 FragmentTransaction transaction= getFragmentManager().beginTransaction();
			 dialog.show(transaction,"");
			 }
		 }
	 private class AddGroupListener implements View.OnClickListener
		 {
		 @Override
		 public void onClick(View v)
			 {
			 AddGroupDialog dialog= new AddGroupDialog();
			 dialog.init("Создать группу");
			 FragmentTransaction transaction= getFragmentManager().beginTransaction();
			 dialog.show(transaction,"");
			 }
		 }
	 private class AddNoteListener implements View.OnClickListener
		 {
		 @Override
		 public void onClick(View v)
			 {
			 AddNoteDialog dialog= new AddNoteDialog();
			 dialog.init("Добавить продукт");
			 FragmentTransaction transaction= getFragmentManager().beginTransaction();
			 dialog.show(transaction,"");
			 }
		 }
	 @Nullable
	 @Override
	 public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
		{
		 View mainView= inflater.inflate(R.layout.green_screen,container,false);
		 Button btnAddGroup= (Button)mainView.findViewById(R.id.btn_addGroup);
		 Button btnAddTag= (Button)mainView.findViewById(R.id.btn_addTag);
		 Button btnAddNote= (Button)mainView.findViewById(R.id.btn_addNote);
		 RecyclerView recyclerList= (RecyclerView)mainView.findViewById(R.id.list);

		 btnAddTag.setOnClickListener(new AddTagListener() );
		 btnAddGroup.setOnClickListener(new AddGroupListener() );
		 btnAddNote.setOnClickListener(new AddNoteListener() );
		 return mainView;
		 }
	 }
