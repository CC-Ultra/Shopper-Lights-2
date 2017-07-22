package com.ultra.shopperlights2.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.ultra.shopperlights2.Activities.AddTemplateNoteActivity;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.Callbacks.DialogDecision;
import com.ultra.shopperlights2.Callbacks.EditTemplateCallback;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.DaoSession;
import com.ultra.shopperlights2.Units.Note;
import com.ultra.shopperlights2.Units.Template;
import com.ultra.shopperlights2.Utils.ConfirmDialog;
import com.ultra.shopperlights2.Utils.O;
import java.util.ArrayList;

/**
 * <p>Адаптер для списка шаблонов</p>
 * <p><sub>(05.06.2017)</sub></p>
 * @author CC-Ultra
 */

public class TemplatesAdapter extends RecyclerView.Adapter<TemplatesAdapter.Holder>
	{
	private Context context;
	private ArrayList<Template> elements;
	private EditTemplateCallback editCallback;

	class Holder extends RecyclerView.ViewHolder
		{
		View mainView;
		TextView title;
		ImageView btnDel;
		DelListener delListener;
		EditListener editListener;
		SelectListener selectListener;

		public Holder(View _mainView)
			{
			super(_mainView);
			mainView= _mainView;
			delListener= new DelListener();
			editListener= new EditListener();
			selectListener= new SelectListener();
			title= (TextView)mainView.findViewById(R.id.title);
			btnDel= (ImageView)mainView.findViewById(R.id.deleteBtn);
			mainView.setOnClickListener(selectListener);
			mainView.setOnLongClickListener(editListener);
			btnDel.setOnClickListener(delListener);
			}
		}
	private class ConfirmDialogDecison implements DialogDecision
		{
		Template element;

		private void setElement(Template _element)
			{
			element=_element;
			}

		@Override
		public void sayNo(int noId) {}
		@Override
		public void sayYes(int yesId)
			{
			delElement(elements.indexOf(element) );
			}
		}
	private class EditListener implements View.OnLongClickListener
		{
		Template element;

		void setElement(Template _element)
			{
			element=_element;
			}

		@Override
		public boolean onLongClick(View v)
			{
			editCallback.editTemplate( (ViewGroup)v.getParent(),element.getId() );
			return true;
			}
		}
	private class DelListener implements View.OnClickListener
		{
		ConfirmDialogDecison decison= new ConfirmDialogDecison();

		void setElement(Template element)
			{
			decison.setElement(element);
			}

		@Override
		public void onClick(View v)
			{
			ConfirmDialog.ask(context,decison,0,0);
			}
		}
	private class SelectListener implements View.OnClickListener
		{
		long templateId;

		void setId(long _templateId)
			{
			templateId=_templateId;
			}

		@Override
		public void onClick(View v)
			{
			Intent jumper= new Intent(context,AddTemplateNoteActivity.class);
			jumper.putExtra(O.mapKeys.extra.TEMPLATE_ID,templateId);
			context.startActivity(jumper);
			}
		}

	public TemplatesAdapter(Context _context,ArrayList<Template> _elements,EditTemplateCallback _editCallback)
		{
		context=_context;
		elements=_elements;
		editCallback=_editCallback;
		}
	private void delElement(int position)
		{
		DaoSession session= App.session;
		Template template= elements.get(position);
		for(Note note : template.getNotes() )
			session.getNoteDao().delete(note);
		session.getTemplateDao().delete(template);
		elements.remove(position);
		notifyItemRemoved(position);
		}

	@Override
	public Holder onCreateViewHolder(ViewGroup parent,int viewType)
		{
		View mainView= LayoutInflater.from(parent.getContext() ).inflate(R.layout.gts_list_element,parent,false);
		return new Holder(mainView);
		}
	@Override
	public void onBindViewHolder(Holder holder,int position)
		{
		Template template= elements.get(position);
		holder.title.setText(template.getTitle() );
		holder.delListener.setElement(template);
		holder.selectListener.setId(template.getId() );
		holder.editListener.setElement(elements.get(position) );
		}
	@Override
	public int getItemCount()
		{
		return elements.size();
		}
	}
