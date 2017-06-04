package com.ultra.shopperlights2.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.*;
import com.ultra.shopperlights2.Utils.O;

import java.util.ArrayList;
import java.util.List;

/**
 * <p></p>
 * <p><sub>(29.04.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class AddNoteDialog extends DialogFragment
	 {
	 private static String lastTimeGroup=null;
	 private ListView listViewSelected,listViewRemaining;
	 private Spinner spinner_Group;
	 private EditText inputName, inputN;
	 private ArrayAdapter<String> adapterSelected,adapterRemaining;
	 private String title;
	 private ArrayList<String> listRemaining,listSelected;
	 private long noteId=0;
	 private String action;

	 private class OkListener implements View.OnClickListener
		 {
		 @Override
		 public void onClick(View v)
			 {
			 DaoSession session= App.session;
			 String noteName= inputName.getText().toString();
			 if(noteName.length() == 0)
				 inputName.setError("Введите название продукта");
			 else
				 {
				 NoteDao noteDao= session.getNoteDao();
				 Note note;
				 if(noteId==0)
					 note= new Note();
				 else
					 note= noteDao.load(noteId);
				 note.setTitle(noteName);
				 String noteNStr= inputN.getText().toString();
				 if(noteNStr.length()!=0)
					 note.setN(Integer.parseInt(noteNStr) );
				 lastTimeGroup= spinner_Group.getSelectedItem().toString();
				 GroupDao groupDao= session.getGroupDao();
				 for(Group lastGroup : groupDao.loadAll() )
					 {
					 if(lastGroup.getNotes().contains(note) )
						 {
						 lastGroup.getNotes().remove(note);
						 groupDao.update(lastGroup);
						 }
					 }
				 if(lastTimeGroup.length()==0)
					 {
					 note.setGroupId(0);
					 note.setTabbed(false);
					 }
				 else
					 {
					 Group group= session.getGroupDao().queryBuilder().where(GroupDao.Properties.Title.eq(lastTimeGroup) ).list().get(0);
					 note.setGroupId(group.getId() );
					 if(!group.getNotes().contains(note) )
						 group.getNotes().add(note);
					 groupDao.update(group);
					 }
				 if(noteId==0)
					 noteDao.insert(note);
				 else
					 noteDao.update(note);

				 TagToNoteDao tagToNoteDao= session.getTagToNoteDao();
				 List<TagToNote> lastTagToNotes= tagToNoteDao.queryBuilder().where(TagToNoteDao.Properties.NoteId.eq(note.getId())).list();
				 for(TagToNote lastTagToNote : lastTagToNotes)
					 tagToNoteDao.delete(lastTagToNote);
				 note.getTags().clear();

				 for(String selectedTag : listSelected)
					 {
					 Tag tag= session.getTagDao().queryBuilder().where(TagDao.Properties.Title.eq(selectedTag) ).list().get(0);
					 note.getTags().add(tag);
					 TagToNote tagToNote= new TagToNote();
					 tagToNote.setNoteId(note.getId() );
					 tagToNote.setTagId(tag.getId() );
					 tagToNoteDao.insert(tagToNote);
					 }
				 noteDao.update(note);
				 dismiss();
				 getContext().sendBroadcast(new Intent(action) );
				 }
			 }
		 }
	 private class OnItemClickListener implements AdapterView.OnItemClickListener
		 {
		 boolean selected;

		 OnItemClickListener(boolean _selected)
			 {
			 selected=_selected;
			 }

		 @Override
		 public void onItemClick(AdapterView<?> parent,View view,int position,long id)
			 {
			 String selectedItem;
			 adapterSelected.clear();
			 adapterRemaining.clear();
			 if(selected)
				 {
				 selectedItem= listSelected.get(position);
				 listSelected.remove(selectedItem);
				 listRemaining.add(selectedItem);
				 adapterRemaining.addAll(listRemaining);
				 }
			 else
				 {
				 selectedItem= listRemaining.get(position);
				 listRemaining.remove(selectedItem);
				 listSelected.add(selectedItem);
				 if(listSelected.size()!=3)
				 	adapterRemaining.addAll(listRemaining);
				 }
			 adapterSelected.addAll(listSelected);
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
		 noteId=_id;
		 title=_title;
		 }
	 private void initViews()
		 {
		 DaoSession session= App.session;
		 listRemaining= new ArrayList<>();
		 listSelected= new ArrayList<>();
		 for(Tag tag : session.getTagDao().queryBuilder().list() )
		 	listRemaining.add(tag.getTitle() );
		 ArrayList<Group> groupList= new ArrayList<>(session.getGroupDao().queryBuilder().list() );
		 ArrayList<String> groupListStr= new ArrayList<>();
		 groupListStr.add("");
		 for(Group group : groupList)
		 	groupListStr.add(group.getTitle() );
		 ArrayAdapter<String> groupSpinnerAdapter= new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,groupListStr);
		 groupSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 spinner_Group.setAdapter(groupSpinnerAdapter);
		 if(groupListStr.contains(lastTimeGroup) )
			 spinner_Group.setSelection(groupListStr.indexOf(lastTimeGroup) );
		 if(noteId!=0)
			 {
			 Note note= session.getNoteDao().load(noteId);
			 inputName.setText(note.getTitle() );
			 inputN.setText(note.getN() +"");
			 Group group= session.getGroupDao().load(note.getGroupId() );
			 if(group!=null)
				 spinner_Group.setSelection(groupListStr.indexOf(group.getTitle() ) );
			 for(Tag tag : note.getTags() )
				 {
				 listSelected.add(tag.getTitle() );
				 listRemaining.remove(tag.getTitle() );
				 }
			 }
		 adapterSelected= new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,new ArrayList(listSelected) );
		 adapterRemaining= new ArrayAdapter<>(getContext(),android.R.layout.simple_spinner_item,new ArrayList(listRemaining) );
		 listViewSelected.setAdapter(adapterSelected);
		 listViewRemaining.setAdapter(adapterRemaining);
		 }

	 @Nullable
	 @Override
	 public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,Bundle savedInstanceState)
		 {
		 getDialog().setTitle(title);
		 View mainView= inflater.inflate(R.layout.add_note_dialog_layout,container,false);
		 if(savedInstanceState!=null)
			 {
			 noteId= savedInstanceState.getLong(O.mapKeys.savedState.SAVED_STATE_NOTE_ID,0);
			 action= savedInstanceState.getString(O.mapKeys.savedState.SAVED_STATE_ACTION);
			 }

		 Button okBtn= (Button)mainView.findViewById(R.id.btnOk);
		 spinner_Group= (Spinner)mainView.findViewById(R.id.spinnerGroup);
		 inputName= (EditText)mainView.findViewById(R.id.titleInput);
		 inputN= (EditText)mainView.findViewById(R.id.nInput);
		 listViewRemaining= (ListView)mainView.findViewById(R.id.listRemaining);
		 listViewSelected= (ListView)mainView.findViewById(R.id.listSelected);

		 initViews();
		 okBtn.setOnClickListener(new OkListener() );
		 listViewSelected.setOnItemClickListener(new OnItemClickListener(true) );
		 listViewRemaining.setOnItemClickListener(new OnItemClickListener(false) );

		 return mainView;
		 }
	 @Override
	 public void onSaveInstanceState(Bundle outState)
		 {
		 if(noteId!=0)
			 outState.putLong(O.mapKeys.savedState.SAVED_STATE_NOTE_ID,noteId);
		 outState.putString(O.mapKeys.savedState.SAVED_STATE_ACTION,action);
		 super.onSaveInstanceState(outState);
		 }
	 }
