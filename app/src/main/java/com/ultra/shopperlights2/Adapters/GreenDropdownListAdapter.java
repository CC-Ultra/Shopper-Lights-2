package com.ultra.shopperlights2.Adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.Callbacks.DialogDecision;
import com.ultra.shopperlights2.Fragments.AddGroupDialog;
import com.ultra.shopperlights2.Fragments.AddNoteDialog;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.*;
import com.ultra.shopperlights2.Utils.Calc;
import com.ultra.shopperlights2.Utils.ConfirmDialog;
import com.ultra.shopperlights2.Utils.O;

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
	 private ArrayList<GreenRecyclerListElement> elements;
	 private Context context;
	 private FragmentManager fragmentManager;
	 private String action;

	 class ConfirmDialogDecision implements DialogDecision
		 {
		 GreenRecyclerListElement element;
		 int position;

		 void init(GreenRecyclerListElement _element,int _position)
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
		 GreenRecyclerListElement element;

		 void setElement(GreenRecyclerListElement _element)
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
				 dialog.init(action,"Изменить группу",( (Group)element).getId() );
				 dialog.show(transaction,"");
				 }
			 else
				 {
				 AddNoteDialog dialog= new AddNoteDialog();
				 dialog.init(action,"Изменить продукт",( (Note)element).getId() );
				 dialog.show(transaction,"");
				 }
			 }
		 }
	 private class DelListener implements View.OnClickListener
		 {
		 GreenRecyclerListElement element;
		 ConfirmDialogDecision decision= new ConfirmDialogDecision();

		 void setElement(GreenRecyclerListElement _element)
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
					 notifyItemChanged(position-1);
					 }
				 session.getNoteDao().delete(note);
				 delElement(element,position);
				 }
			 }
		 }
	 private class DropdownListener implements View.OnClickListener
		 {
		 GreenRecyclerListElement element;
		 ImageView img;

		 void init(GreenRecyclerListElement _element,ImageView _img)
			 {
			 element=_element;
			 img=_img;
			 }

		 @Override
		 public void onClick(View v)
			 {
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
				 img.setImageResource(R.drawable.arrow_dropdown);
			 else
				 img.setImageResource(R.drawable.arrow_dropup);
			 App.session.getGroupDao().update(group);
			 }
		 }
	 class Holder extends RecyclerView.ViewHolder
		 {
//		 ImageButton btnDel;
		 ImageView img,btnDel;
		 TextView title,n;
		 TextView tag[]= new TextView[3];
		 DelListener delListener;
		 DropdownListener dropdownListener;
		 EditListener editListener;
		 View mainView;

		 public Holder(View _mainView)
			 {
			 super(_mainView);
			 mainView=_mainView;
			 img= (ImageView)mainView.findViewById(R.id.dropdownImg);
			 btnDel= (ImageView) mainView.findViewById(R.id.deleteBtn);
			 title= (TextView)mainView.findViewById(R.id.title);
			 n= (TextView)mainView.findViewById(R.id.n);
			 tag[0]= (TextView)mainView.findViewById(R.id.tag1);
			 tag[1]= (TextView)mainView.findViewById(R.id.tag2);
			 tag[2]= (TextView)mainView.findViewById(R.id.tag3);
			 delListener= new DelListener();
			 dropdownListener= new DropdownListener();
			 editListener= new EditListener();
			 btnDel.setOnClickListener(delListener);
			 }
		 }

	 public GreenDropdownListAdapter(Context _context,ArrayList<GreenRecyclerListElement> _elements,String _action,FragmentManager _fragmentManager)
		 {
		 fragmentManager=_fragmentManager;
		 action=_action;
		 context=_context;
		 elements=_elements;
		 }
	 private void delGroup(GreenRecyclerListElement element,int position)
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
	 private void addElement(GreenRecyclerListElement element,int position)
		 {
		 elements.add(position,element);
		 notifyItemInserted(position);
		 }
	 private void delElement(GreenRecyclerListElement element,int position)
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
	 @SuppressWarnings("deprecation")
	 @Override
	 public void onBindViewHolder(Holder holder,int position)
		 {
		 GreenRecyclerListElement recyclerListElement= elements.get(position);
		 if(recyclerListElement.isGroup() )
			 {
			 Group group= (Group) recyclerListElement;
			 holder.img.setVisibility(View.VISIBLE);
			 holder.dropdownListener.init(recyclerListElement,holder.img);
			 group.setHolderTitle(group.getTitle() +" ("+ group.getNotes().size() +")");
			 holder.title.setText(group.getHolderTitle() );
			 holder.title.setTextColor(context.getResources().getColor(R.color.bright_green) );
			 for(int i=0; i<3; i++)
				 holder.tag[i].setVisibility(View.GONE);
			 holder.n.setVisibility(View.GONE);
			 holder.mainView.setOnClickListener(holder.dropdownListener);
			 if(group.isOpen() )
				 holder.img.setImageResource(R.drawable.arrow_dropup);
			 }
		 else
			 {
			 Note note= (Note) recyclerListElement;
			 if(note.isLocked() )
				 {
				 holder.btnDel.setImageResource(R.drawable.red_close_button_disabled);
				 holder.btnDel.setEnabled(false);
				 }
			 else
				 {
				 holder.btnDel.setImageResource(R.drawable.red_close_button);
				 holder.btnDel.setEnabled(true);
				 }
			 holder.title.setText(note.getTitle() );
			 holder.title.setTextColor(context.getResources().getColor(R.color.bright_yellow) );
			 if(note.isTabbed() )
				 {
				 float k=1;
				 int orientation= context.getResources().getConfiguration().orientation;
				 if(orientation==Configuration.ORIENTATION_LANDSCAPE)
				 	k=1.9F;
				 Log.d(TAG,"onBindViewHolder: "+ note.getTitle() +" tabbed");
				 RelativeLayout.LayoutParams layoutParams= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
				 layoutParams.setMargins(Calc.dpToPx(context,O.dimens.GREEN_DROPDOWN_TAB),Calc.dpToPx(context,2),Calc.dpToPx(context,2),Calc.dpToPx(context,2) );
				 layoutParams.height= Calc.dpToPx(context,O.dimens.GREEN_DROPDOWN_ELEMENT_HEIGHT);
				 holder.mainView.setLayoutParams(layoutParams);
				 ViewGroup.LayoutParams titleParams= holder.title.getLayoutParams();
				 titleParams.width= Calc.dpToPx(context,(int)(O.dimens.GREEN_DROPDOWN_TABBED_TITLE_WIDTH * k) );
				 holder.title.setLayoutParams(titleParams);
				 RelativeLayout.LayoutParams nParams= new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
				 nParams.setMargins(Calc.dpToPx(context,(int)(O.dimens.GREEN_DROPDOWN_TABBED_N_MARGIN * k) ),Calc.dpToPx(context,5),0,0);
				 holder.n.setLayoutParams(nParams);
				 }
			 holder.n.setVisibility(View.VISIBLE);
			 holder.n.setText(note.getN()==0 ? "" : ""+note.getN() );
			 holder.n.setTextColor(context.getResources().getColor(R.color.bright_yellow) );
			 ArrayList<Tag> tags= new ArrayList<>(note.getTags() );
			 int i=0;
			 for(Tag tag : tags)
				 {
				 holder.tag[i].setVisibility(View.VISIBLE);
				 holder.tag[i].setText(tag.getTitle() );
				 holder.tag[i].setTextColor(tag.getColor() );
				 i++;
				 }
			 holder.mainView.setOnClickListener(holder.editListener);
			 holder.img.setVisibility(View.GONE);
			 }
		 holder.editListener.setElement(recyclerListElement);
		 holder.delListener.setElement(recyclerListElement);
		 }

	 @Override
	 public int getItemCount()
		 {
		 return elements.size();
		 }
	 }
