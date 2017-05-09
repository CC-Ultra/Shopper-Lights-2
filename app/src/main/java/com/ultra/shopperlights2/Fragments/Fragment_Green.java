package com.ultra.shopperlights2.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.ultra.shopperlights2.Adapters.GreenDropdownListAdapter;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.Callbacks.UpdateListCallback;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.*;

import java.util.ArrayList;

/**
 * <p></p>
 * <p><sub>(07.08.2016)</sub></p>
 *
 * @author CC-Ultra
 */
public class Fragment_Green extends Fragment implements UpdateListCallback
	 {
	 RecyclerView recyclerList;
	 GreenDropdownListAdapter adapter;

	 private class AddNoteListener implements View.OnClickListener
		 {
		 @Override
		 public void onClick(View v)
			 {
			 AddNoteDialog dialog= new AddNoteDialog();
			 dialog.init(Fragment_Green.this,"Добавить продукт");
			 FragmentTransaction transaction= getFragmentManager().beginTransaction();
			 dialog.show(transaction,"");
			 }
		 }

	 @Override
	 public void updateLists()
		 {
		 initAdapter();
		 }

	 private void initAdapter()
		 {
		 ArrayList<RecyclerListElement> elements= new ArrayList<>();
		 elements.addAll(App.session.getGroupDao().queryBuilder().orderAsc(GroupDao.Properties.Priority).list() );
		 for(int i=0; i<elements.size(); i++)
			 {
			 RecyclerListElement element= elements.get(i);
			 if(element.isGroup() )
				 {
				 Group group= (Group)element;
				 if(group.isOpen() )
					 {
					 ArrayList<Note> notes= new ArrayList<>(group.getNotes() );
					 for(Note note : notes)
						 {
						 note.setTabbed(true);
						 elements.add(i+1,note);
						 }
					 }
				 }
			 }
		 elements.addAll(App.session.getNoteDao().queryBuilder().orderAsc(NoteDao.Properties.Title).where(NoteDao.Properties.GroupId.eq(0) ).list() );

		 adapter= new GreenDropdownListAdapter(getContext(),elements,this,getFragmentManager() );
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
		 initAdapter();
		 return mainView;
		 }
	 }
