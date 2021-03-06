package com.ultra.shopperlights2.Fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.ultra.shopperlights2.Adapters.GreenDropdownListAdapter;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.Dialogs.AddNoteDialog;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.*;
import com.ultra.shopperlights2.Utils.O;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Фрагмент зеленого экрана</p>
 * <p><sub>(07.08.2016)</sub></p>
 * @author CC-Ultra
 */
public class Fragment_Green extends Fragment
	 {
	 private RecyclerView recyclerList;
	 private GreenDropdownListAdapter adapter;
	 private BroadcastReceiver receiver;

	 private class Receiver extends BroadcastReceiver
		 {
		 @Override
		 public void onReceive(Context context,Intent intent)
			 {
			 initAdapter();
			 }
		 }
	 private class AddNoteListener implements View.OnClickListener
		 {
		 @Override
		 public void onClick(View v)
			 {
			 AddNoteDialog dialog= new AddNoteDialog();
			 dialog.init(getContext(),(ViewGroup) v.getParent(),O.actions.ACTION_FRAGMENT_GREEN,"Добавить продукт");
			 dialog.createAndShow();
			 }
		 }

	 private void initAdapter()
		 {
		 ArrayList<GreenRecyclerListElement> elements= new ArrayList<>();
		 elements.addAll(App.session.getGroupDao().queryBuilder().orderAsc(GroupDao.Properties.Priority).list() );
		 for(int i=0; i<elements.size(); i++)
			 {
			 GreenRecyclerListElement element= elements.get(i);
			 //сначала кажется, что эта проверка не нужна, т.к. в списке только группы, но потом добавляются записи и она
			 //становится актуальной
			 if(element.isGroup() )
				 {
				 Group group= (Group)element;
				 if(group.isOpen() )
					 {
					 for(Note note : group.getNotes() )
						 {
						 note.setTabbed(true);
						 elements.add(i+1,note);
						 }
					 }
				 }
			 }
		 List<Note> freeNoteList= App.session.getNoteDao().queryBuilder().orderAsc(NoteDao.Properties.Title).
				 													where(NoteDao.Properties.GroupId.eq(0)).list();
		 for(Note note : freeNoteList)
			 if(!note.isEthereal() )
				 elements.add(note);

		 adapter= new GreenDropdownListAdapter(getContext(),elements,O.actions.ACTION_FRAGMENT_GREEN);
		 recyclerList.setAdapter(adapter);
		 recyclerList.setLayoutManager(new LinearLayoutManager(getContext() ) );
		 }

	 @Nullable
	 @Override
	 public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
		{
		 View mainView= inflater.inflate(R.layout.green_screen,container,false);
		 Button btnAddNote= (Button)mainView.findViewById(R.id.btn_addNote);
		 recyclerList= (RecyclerView)mainView.findViewById(R.id.list);

		 btnAddNote.setOnClickListener(new AddNoteListener() );

		 receiver= new Receiver();
		 IntentFilter filter= new IntentFilter(O.actions.ACTION_FRAGMENT_GREEN);
		 getContext().registerReceiver(receiver,filter);
		 return mainView;
		 }
	 @Override
	 public void onResume()
		 {
		 super.onResume();
		 initAdapter();
		 }
	 @Override
	 public void onDestroy()
		 {
		 super.onDestroy();
		 getContext().unregisterReceiver(receiver);
		 }
	 }
