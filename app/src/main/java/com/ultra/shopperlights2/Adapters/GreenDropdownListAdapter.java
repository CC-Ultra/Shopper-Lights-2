package com.ultra.shopperlights2.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.Callbacks.DialogDecision;
import com.ultra.shopperlights2.Callbacks.UpdateNoteListCallback;
import com.ultra.shopperlights2.Fragments.AddGroupDialog;
import com.ultra.shopperlights2.Fragments.AddNoteDialog;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.*;
import com.ultra.shopperlights2.Utils.ConfirmDialog;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * <p></p>
 * <p><sub>(03.05.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class GreenDropdownListAdapter extends RecyclerView.Adapter<GreenDropdownListAdapter.Holder>
	 {
	 private ArrayList<NoteListElement> elements;
	 private Context context;
	 private UpdateNoteListCallback callback;
	 FragmentManager fragmentManager;

	 class ConfirmDialogDecision implements DialogDecision
		 {
		 NoteListElement element;
		 int position;

		 void init(NoteListElement _element,int _position)
			 {
			 position=_position;
			 element=_element;
			 }
		 @Override
		 public void sayYes(int yesId)
			 {
			 delGroup(element,position);
			 }
		 @Override
		 public void sayNo(int noId) {}
		 }
	 private class EditListener implements View.OnClickListener
		 {
		 NoteListElement element;

		 void setElement(NoteListElement _element)
			 {
			 element=_element;
			 }

		 @Override
		 public void onClick(View v)
			 {
			 FragmentTransaction transaction= fragmentManager.beginTransaction();
			 if(element.isGroup() )
				 {
				 AddGroupDialog dialog= new AddGroupDialog();
				 dialog.init(callback,"Изменить группу",( (Group)element).getId() );
				 dialog.show(transaction,"");
				 }
			 else
				 {
				 AddNoteDialog dialog= new AddNoteDialog();
				 dialog.init(callback,"Изменить продукт",( (Note)element).getId() );
				 dialog.show(transaction,"");
				 }
			 }
		 }
	 private class DelListener implements View.OnClickListener
		 {
		 NoteListElement element;
		 ConfirmDialogDecision decision= new ConfirmDialogDecision();

		 void setElement(NoteListElement _element)
			 {
			 element=_element;
			 }

		 @Override
		 public void onClick(View v)
			 {
			 int position= elements.indexOf(element);
			 DaoSession session=App.session;
			 Group group;
			 if(element.isGroup() )
				 {
				 decision.init(element,position);
				 ConfirmDialog.ask(context,decision,0,0);
				 }
			 else
				 {
				 Note note= (Note)element;
				 ArrayList<TagToNote> tagToNotes= new ArrayList<>();
				 TagToNoteDao tagToNoteDao= session.getTagToNoteDao();
				 tagToNotes.addAll(tagToNoteDao.queryBuilder().where(TagToNoteDao.Properties.NoteId.eq(note.getId() ) ).list() );
				 for(TagToNote tagToNote : tagToNotes)
					 tagToNoteDao.delete(tagToNote);
				 if(note.getGroupId()!=0)
					 {
					 group= session.getGroupDao().load(note.getGroupId() );
					 group.getNotes().remove(note);
					 group.setHolderTitle(group.getTitle() +" ("+ group.getNotes().size() +")");
					 session.getGroupDao().update(group);
					 }
				 session.getNoteDao().delete(note);
				 delElement(element,position);
				 }
			 }
		 }
	 private class DropdownListener implements View.OnClickListener
		 {
		 NoteListElement element;

		 void setElement(NoteListElement _element)
			 {
			 element=_element;
			 }

		 @Override
		 public void onClick(View v)
			 {
			 ImageButton btn= (ImageButton)v;
			 int position= elements.indexOf(element);
			 Group group= (Group)element;
			 boolean isOpen= group.isOpen();
			 ArrayList<Note> notes= new ArrayList<>(group.getNotes() );
			 if(!isOpen)
				 for(Note note : notes)
					 {
					 note.setTabbed(true);
					 addElement(note,position+1);
					 }
			 else
				 for(Note note : notes)
					 delElement(note,position+1);
			 group.setIsOpen(!isOpen);
			 if(isOpen)
				 btn.setImageResource(R.drawable.arrow_dropdown);
			 else
				 btn.setImageResource(R.drawable.arrow_dropup);
			 App.session.getGroupDao().update(group);
			 }
		 }
	 class Holder extends RecyclerView.ViewHolder
		 {
		 ImageButton btnDel,btnEdit,btnDropdown;
		 TextView title,n;
		 TextView tag[]= new TextView[3];
		 DelListener delListener;
		 DropdownListener dropdownListener;
		 EditListener editListener;

		 public Holder(View itemView)
			 {
			 super(itemView);
			 btnDel= (ImageButton)itemView.findViewById(R.id.deleteBtn);
			 btnEdit= (ImageButton)itemView.findViewById(R.id.editBtn);
			 btnDropdown= (ImageButton)itemView.findViewById(R.id.dropdownBtn);
			 title= (TextView)itemView.findViewById(R.id.title);
			 n= (TextView)itemView.findViewById(R.id.n);
			 tag[0]= (TextView)itemView.findViewById(R.id.tag1);
			 tag[1]= (TextView)itemView.findViewById(R.id.tag2);
			 tag[2]= (TextView)itemView.findViewById(R.id.tag3);
			 delListener= new DelListener();
			 dropdownListener= new DropdownListener();
			 editListener= new EditListener();
			 btnDel.setOnClickListener(delListener);
			 btnEdit.setOnClickListener(editListener);
			 btnDropdown.setOnClickListener(dropdownListener);
			 }
		 }

	 public GreenDropdownListAdapter(Context _context,ArrayList<NoteListElement> _elements,UpdateNoteListCallback _callback,FragmentManager _fragmentManager)
		 {
		 fragmentManager=_fragmentManager;
		 callback=_callback;
		 context=_context;
		 elements=_elements;
		 }
	 private int dpInPX(int dp)
		 {
		 Resources r = context.getResources();
		 int px= (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics() );
		 return px;
		 }
	 private void delGroup(NoteListElement element,int position)
		 {
		 DaoSession session= App.session;
		 Group group= (Group)element;
		 ArrayList<Note> notes= new ArrayList<>(group.getNotes() );
		 for(Note note : notes)
			 {
			 note.setGroupId(0);
			 note.setTabbed(false);
			 if(group.isOpen() )
				 delElement(note,position+1);
			 session.getNoteDao().update(note);
			 addElement(note,elements.size() );
			 }
		 session.getGroupDao().delete(group);
		 delElement(element,position);
		 }
	 private void addElement(NoteListElement element,int position)
		 {
		 elements.add(position,element);
		 notifyItemInserted(position);
		 }
	 private void delElement(NoteListElement element,int position)
		 {
		 elements.remove(element);
		 notifyItemRemoved(position);
		 }

	 @Override
	 public Holder onCreateViewHolder(ViewGroup parent,int viewType)
		 {
		 View view= LayoutInflater.from(parent.getContext() ).inflate(R.layout.list_element_green_screen,parent,false);
		 return new Holder(view);
		 }
	 @Override
	 public void onBindViewHolder(Holder holder,int position)
		 {
		 NoteListElement noteListElement= elements.get(position);
		 if(noteListElement.isGroup() )
			 {
			 Group group= (Group)noteListElement;
			 holder.btnDropdown.setVisibility(View.VISIBLE);
			 holder.dropdownListener.setElement(noteListElement);
			 group.setHolderTitle(group.getTitle() +" ("+ group.getNotes().size() +")");
			 holder.title.setText(group.getHolderTitle() );
			 holder.title.setTextColor(Color.GREEN);
			 for(int i=0; i<3; i++)
				 holder.tag[i].setVisibility(View.GONE);
			 holder.n.setVisibility(View.GONE);
			 if(group.isOpen() )
				 holder.btnDropdown.setImageResource(R.drawable.arrow_dropup);
			 }
		 else
			 {
			 Note note= (Note)noteListElement;
			 holder.title.setText(note.getTitle() );
			 holder.title.setTextColor(Color.YELLOW);
			 if(note.isTabbed() )
				 {
				 Log.d(TAG,"onBindViewHolder: "+ note.getTitle() +" tabbed");
				 RelativeLayout.LayoutParams layoutParams= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
				 layoutParams.setMargins(dpInPX(50),dpInPX(5),0,0);
				 holder.title.setLayoutParams(layoutParams);
				 }
			 holder.n.setVisibility(View.VISIBLE);
			 holder.n.setText(note.getN() +"");
			 holder.n.setTextColor(Color.YELLOW);
			 ArrayList<Tag> tags= new ArrayList<>(note.getTags() );
			 int i=0;
			 for(Tag tag : tags)
				 {
				 holder.tag[i].setVisibility(View.VISIBLE);
				 holder.tag[i].setText(tag.getTitle() );
				 holder.tag[i].setTextColor(tag.getColor() );
				 i++;
				 }
			 holder.btnDropdown.setVisibility(View.GONE);
			 }
		 holder.editListener.setElement(noteListElement);
		 holder.delListener.setElement(noteListElement);
		 }

	 @Override
	 public int getItemCount()
		 {
		 return elements.size();
		 }
	 }
