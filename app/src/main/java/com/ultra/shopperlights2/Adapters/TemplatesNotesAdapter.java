package com.ultra.shopperlights2.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.Callbacks.*;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.*;
import com.ultra.shopperlights2.Utils.ConfirmDialog;

import java.util.ArrayList;

/**
 * <p></p>
 * <p><sub>(05.06.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class TemplatesNotesAdapter extends RecyclerView.Adapter<TemplatesNotesAdapter.Holder>
	{
	private Context context;
	private ArrayList<Note> elements;
	private EditNoteTemplateCallback editCallback;

	class Holder extends RecyclerView.ViewHolder
		{
		View mainView;
		TextView tag[]= new TextView[3];
		TextView title,nTxt;
		ImageButton btnDel;
		DelListener delListener;
		EditListener editListener;

		public Holder(View _mainView)
			{
			super(_mainView);
			mainView= _mainView;
			delListener= new DelListener();
			editListener= new EditListener();
			title= (TextView)mainView.findViewById(R.id.title);
			btnDel= (ImageButton)mainView.findViewById(R.id.deleteBtn);
			nTxt= (TextView)mainView.findViewById(R.id.n);
			tag[0]= (TextView)mainView.findViewById(R.id.tag1);
			tag[1]= (TextView)mainView.findViewById(R.id.tag2);
			tag[2]= (TextView)mainView.findViewById(R.id.tag3);
			mainView.setOnClickListener(editListener);
			btnDel.setOnClickListener(delListener);
			}
		}
	private class EditListener implements View.OnClickListener
		{
		Note element;

		void setElement(Note _element)
			{
			element=_element;
			}

		@Override
		public void onClick(View v)
			{
			editCallback.initNoteFragment(element.getId(),element.getTemplateId() );
			}
		}
	private class DelListener implements View.OnClickListener
		{
		Note element;

		void setPosition(Note _element)
			{
			element=_element;
			}

		@Override
		public void onClick(View v)
			{
			DaoSession session= App.session;
			int position= elements.indexOf(element);
			for(Tag tag : element.getTags())
				{
				TagToNote tagToNote= session.getTagToNoteDao().queryBuilder().where(TagToNoteDao.Properties.NoteId.eq(element.getId() ),
						TagToNoteDao.Properties.TagId.eq(tag.getId() ) ).list().get(0);
				session.getTagToNoteDao().delete(tagToNote);
				}
			Template template= session.getTemplateDao().load(element.getTemplateId() );
			template.getNotes().remove(element);
			session.getTemplateDao().update(template);
			session.getNoteDao().delete(element);
			elements.remove(position);
			notifyItemRemoved(position);
			}
		}

	public TemplatesNotesAdapter(Context _context,long templateId,EditNoteTemplateCallback _editCallback)
		{
		context=_context;
		editCallback=_editCallback;
		elements= new ArrayList<>(App.session.getTemplateDao().load(templateId).getNotes() );
		}

	@Override
	public Holder onCreateViewHolder(ViewGroup parent,int viewType)
		{
		View mainView= LayoutInflater.from(parent.getContext() ).inflate(R.layout.list_element_green_screen,parent,false);
		return new Holder(mainView);
		}
	@SuppressWarnings("deprecation")
	@Override
	public void onBindViewHolder(Holder holder,int position)
		{
		Note note= elements.get(position);
		holder.title.setText(note.getTitle() );
		if(note.getN()!=0)
			{
			holder.nTxt.setText(""+ note.getN() );
			holder.nTxt.setVisibility(View.VISIBLE);
			}
		int i=0;
		for(Tag tag : note.getTags())
			{
			holder.tag[i].setVisibility(View.VISIBLE);
			holder.tag[i].setText(tag.getTitle() );
			holder.tag[i].setTextColor(tag.getColor() );
			i= (i+1)%3;
			}
		holder.delListener.setPosition(note);
		holder.editListener.setElement(note);
		}
	@Override
	public int getItemCount()
		{
		return elements.size();
		}
	}
