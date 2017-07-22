package com.ultra.shopperlights2.Dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
 * <p>Класс-обертка над диалогом добавления/редактирования записи</p>
 * <p><sub>(29.04.2017)</sub></p>
 * @author CC-Ultra
 */

public class AddNoteDialog
	{
	private Context context;
	private AlertDialog dialog;
	private static String lastTimeGroup=null;
	private ListView listViewSelected,listViewRemaining;
	private Spinner spinnerGroup;
	private EditText inputName, inputN;
	private ArrayAdapter<String> adapterSelected,adapterRemaining;
	private String title;
	private ArrayList<String> listRemaining,listSelected;
	private long noteId=0;
	private String action;
	private long templateId;
	private ViewGroup parent;

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
				lastTimeGroup= spinnerGroup.getSelectedItem().toString();
				//удаляю запись из группы, в которой она может состоять
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
				else //и вношу заново
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

				//удаляю связи с тегами
				TagToNoteDao tagToNoteDao= session.getTagToNoteDao();
				List<TagToNote> lastTagToNotes= tagToNoteDao.queryBuilder().where(TagToNoteDao.Properties.NoteId.eq(note.getId() ) ).list();
				for(TagToNote lastTagToNote : lastTagToNotes)
					tagToNoteDao.delete(lastTagToNote);
				note.getTags().clear();

				//добавляю заново
				for(String selectedTag : listSelected)
					{
					Tag tag= session.getTagDao().queryBuilder().where(TagDao.Properties.Title.eq(selectedTag) ).list().get(0);
					note.getTags().add(tag);
					TagToNote tagToNote= new TagToNote();
					tagToNote.setNoteId(note.getId() );
					tagToNote.setTagId(tag.getId() );
					tagToNoteDao.insert(tagToNote);
					}
				if(templateId!=0) //если запись была для шаблона
					{
					note.setEthereal(true); //устанавливается эфирность
					note.setTemplateId(templateId);
					Template template= session.getTemplateDao().load(templateId);
					if(!template.getNotes().contains(note) )
						template.getNotes().add(note);
					session.getTemplateDao().update(template);
					}
				noteDao.update(note);
				dialog.dismiss();
				context.sendBroadcast(new Intent(action) ); //обновление списка в вызывающей активности/фрагменте
				}
			}
		}
	private class OnItemClickListener implements AdapterView.OnItemClickListener
		{
		boolean selected; //selected или remaining

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
				{ //был нажат selected список
				selectedItem= listSelected.get(position);
				listSelected.remove(selectedItem);
				listRemaining.add(selectedItem);
				adapterRemaining.addAll(listRemaining);
				}
			else
				{ //был нажат remaining список
				selectedItem= listRemaining.get(position);
				listRemaining.remove(selectedItem);
				listSelected.add(selectedItem);
				if(listSelected.size()!=3) //а если ==3, то не добавлять, оставив пустым
					adapterRemaining.addAll(listRemaining);
				}
			adapterSelected.addAll(listSelected);
			}
		}

	/**
	 * создать запись
	 */
	public void init(Context _context,ViewGroup _parent,String _action,String _title)
		{
		context=_context;
		parent=_parent;
		action=_action;
		title=_title;
		}

	/**
	 * редактировать запись
	 */
	public void init(Context _context,ViewGroup _parent,String _action,String _title,long _id)
		{
		context=_context;
		parent=_parent;
		action=_action;
		noteId=_id;
		title=_title;
		}

	/**
	 * создать запись для шаблона
	 */
	public void init(Context _context,ViewGroup _parent,String _action,long _templateId,String _title)
		{
		context=_context;
		parent=_parent;
		action=_action;
		title=_title;
		templateId=_templateId;
		}

	/**
	 * редактировать запись для шаблона
	 */
	public void init(Context _context,ViewGroup _parent,String _action,String _title,long _id,long _templateId)
		{
		context=_context;
		parent=_parent;
		action=_action;
		noteId=_id;
		title=_title;
		templateId=_templateId;
		}
	private void initViews()
		{
		DaoSession session= App.session;
		listRemaining= new ArrayList<>();
		listSelected= new ArrayList<>();
		//в listRemaining переписываю все теги кроме транспортного
		for(Tag tag : session.getTagDao().loadAll() )
			if(!tag.getTitle().equals(O.TRANSPORT_TAG_NAME) )
				listRemaining.add(tag.getTitle() );
		//составляю список групп и передаю его адаптеру спиннера
		ArrayList<Group> groupList= new ArrayList<>(session.getGroupDao().queryBuilder().orderAsc(GroupDao.Properties.Priority).list() );
		ArrayList<String> groupListStr= new ArrayList<>();
		groupListStr.add("");
		for(Group group : groupList)
			groupListStr.add(group.getTitle() );
		ArrayAdapter<String> groupSpinnerAdapter= new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,groupListStr);
		groupSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinnerGroup.setAdapter(groupSpinnerAdapter);
		if(groupListStr.contains(lastTimeGroup) )
			spinnerGroup.setSelection(groupListStr.indexOf(lastTimeGroup) );
		//заполнение полей
		if(noteId!=0)
			{
			Note note= session.getNoteDao().load(noteId);
			inputName.setText(note.getTitle() );
			if(note.getN()!=0)
				inputN.setText(note.getN() +"");
			Group group= session.getGroupDao().load(note.getGroupId() );
			if(group!=null)
				spinnerGroup.setSelection(groupListStr.indexOf(group.getTitle() ) );
			for(Tag tag : note.getTags() )
				{
				listSelected.add(tag.getTitle() );
				listRemaining.remove(tag.getTitle() );
				}
			}
		if(listSelected.size()!=3)
			{
			adapterRemaining= new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,new ArrayList<>(listRemaining) );
			listViewRemaining.setAdapter(adapterRemaining);
			}
		else //если 3, то то же самое, только с пустым списком
			{
			adapterRemaining= new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,new ArrayList<String>() );
			listViewRemaining.setAdapter(adapterRemaining);
			}
		adapterSelected= new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,new ArrayList<>(listSelected) );
		listViewSelected.setAdapter(adapterSelected);
		}

	/**
	 * инициализация mainView и передача ее диалогу
	 */
	public void createAndShow()
		{
		View mainView= LayoutInflater.from(context).inflate(R.layout.add_note_dialog_layout,parent,false);
		AlertDialog.Builder builder= new AlertDialog.Builder(context);
		builder.setTitle(title);

		Button okBtn= (Button)mainView.findViewById(R.id.btnOk);
		TextView groupTxt= (TextView)mainView.findViewById(R.id.groupTxt);
		spinnerGroup= (Spinner)mainView.findViewById(R.id.spinnerGroup);
		//при шаблоне убираю все упоминания о группах
		if(templateId!=0)
			{
			spinnerGroup.setVisibility(View.GONE);
			groupTxt.setVisibility(View.GONE);
			}
		inputName= (EditText)mainView.findViewById(R.id.titleInput);
		inputN= (EditText)mainView.findViewById(R.id.nInput);
		listViewRemaining= (ListView)mainView.findViewById(R.id.listRemaining);
		listViewSelected= (ListView)mainView.findViewById(R.id.listSelected);

		initViews();
		okBtn.setOnClickListener(new OkListener() );
		listViewSelected.setOnItemClickListener(new OnItemClickListener(true) );
		listViewRemaining.setOnItemClickListener(new OnItemClickListener(false) );

		builder.setView(mainView);
		dialog= builder.create();
		dialog.show();
		}
	 }
