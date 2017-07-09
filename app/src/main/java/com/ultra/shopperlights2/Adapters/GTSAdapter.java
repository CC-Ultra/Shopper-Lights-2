package com.ultra.shopperlights2.Adapters;


import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.Callbacks.DialogDecision;
import com.ultra.shopperlights2.Fragments.AddGroupDialog;
import com.ultra.shopperlights2.Fragments.AddShopDialog;
import com.ultra.shopperlights2.Fragments.AddTagDialog;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.*;
import com.ultra.shopperlights2.Utils.ConfirmDialog;
import com.ultra.shopperlights2.Utils.O;
import java.util.ArrayList;
import java.util.List;

import static com.ultra.shopperlights2.Utils.O.TAG;

/**
 * <p></p>
 * <p><sub>(07.05.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class GTSAdapter extends RecyclerView.Adapter<GTSAdapter.Holder>
	{
	private String action;
	private FragmentManager fragmentManager;
	private ArrayList<GreenRecyclerListElement> elements= new ArrayList<>();
	private Context context;

	class ConfirmDialogDecision implements DialogDecision
		{
		GreenRecyclerListElement element;

		public ConfirmDialogDecision(GreenRecyclerListElement _element)
			{
			element=_element;
			}

		@Override
		public void sayNo(int noId){}

		@Override
		public void sayYes(int yesId)
			{
			switch(element.getGTSType() )
				{
				case O.interaction.ELEMENT_TYPE_GROUP:
					deleteGroup( (Group)element);
					break;
				case O.interaction.ELEMENT_TYPE_TAG:
					deleteTag( (Tag)element);
					break;
				case O.interaction.ELEMENT_TYPE_SHOP:
					deleteShop( (Shop)element);
					break;
				}
			int position= elements.indexOf(element);
			delElement(element,position);
			}
		}
	class DelListener implements View.OnClickListener
		{
		GreenRecyclerListElement element;
		ConfirmDialogDecision dialogDecision;

		void setElement(GreenRecyclerListElement _element)
			{
			element=_element;
			dialogDecision= new ConfirmDialogDecision(element);
			}

		@Override
		public void onClick(View v)
			{
			ConfirmDialog.ask(context,dialogDecision,0,0);
			}
		}

	class EditListener implements View.OnClickListener
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
			switch(element.getGTSType() )
				{
				case O.interaction.ELEMENT_TYPE_GROUP:
					{
					Group group= (Group)element;
					AddGroupDialog dialog= new AddGroupDialog();
					dialog.init(action,"Изменить группу",group.getId() );
					dialog.show(transaction,"");
					break;
					}
				case O.interaction.ELEMENT_TYPE_TAG:
					{
					Tag tag=(Tag) element;
					AddTagDialog dialog=new AddTagDialog();
					dialog.init(action,"Изменить группу",tag.getId());
					dialog.show(transaction,"");
					break;
					}
				case O.interaction.ELEMENT_TYPE_SHOP:
					{
					Shop shop= (Shop)element;
					AddShopDialog dialog=new AddShopDialog();
					dialog.init(action,"Изменить магазин",shop.getId() );
					dialog.show(transaction,"");
					break;
					}
				}
			}
		}
	class Holder extends RecyclerView.ViewHolder
		{
		TextView title,city,adr;
		ImageView btnDel;
		DelListener delListener;
		EditListener editListener;

		public Holder(View mainView)
			{
			super(mainView);
			delListener= new DelListener();
			editListener= new EditListener();
			title= (TextView)mainView.findViewById(R.id.title);
			city= (TextView)mainView.findViewById(R.id.city);
			adr= (TextView)mainView.findViewById(R.id.adr);
			btnDel= (ImageView)mainView.findViewById(R.id.deleteBtn);
			btnDel.setOnClickListener(delListener);
			mainView.setOnClickListener(editListener);
			}
		}

	public GTSAdapter(Context _context,ArrayList<GreenRecyclerListElement> _elements,String _action,FragmentManager _fragmentManager)
		{
		action=_action;
		fragmentManager=_fragmentManager;
		context=_context;
		elements=_elements;
		}
	private void deleteShop(Shop shop)
		{
		shop.setAlive(false);
		App.session.getShopDao().update(shop);
		}
	private void deleteTag(Tag tag)
		{
		DaoSession session= App.session;
		List<TagToNote> tagToNotesSrc= session.getTagToNoteDao().queryBuilder().where(TagToNoteDao.Properties.TagId.eq(tag.getId() ) ).list();
		for(TagToNote tagToNote : tagToNotesSrc)
			session.getTagToNoteDao().delete(tagToNote);
		List<TagToProduct> tagToProductsSrc= session.getTagToProductDao().queryBuilder().where(TagToProductDao.Properties.TagId.eq(tag.getId() ) ).list();
		for(TagToProduct tagToProduct : tagToProductsSrc)
			session.getTagToProductDao().delete(tagToProduct);
		session.getTagDao().delete(tag);
		}
	private void deleteGroup(Group group)
		{
		ArrayList<Note> notes= new ArrayList<>(group.getNotes() );
		for(Note note : notes)
			note.setGroupId(0);
		App.session.getGroupDao().delete(group);
		}
	private void delElement(GreenRecyclerListElement element,int position)
		{
		elements.remove(element);
		notifyItemRemoved(position);
		}

	@Override
	public Holder onCreateViewHolder(ViewGroup parent,int viewType)
		{
		View view= LayoutInflater.from(parent.getContext() ).inflate(R.layout.gts_list_element,parent,false);
		return new Holder(view);
		}
	@Override
	public void onBindViewHolder(Holder holder,int position)
		{
		GreenRecyclerListElement element= elements.get(position);
		switch(element.getGTSType() )
			{
			case O.interaction.ELEMENT_TYPE_TAG:
				Tag tag= (Tag)element;
				holder.title.setTextColor(tag.getColor() );
				holder.title.setText(tag.getTitle() );
				break;
			case O.interaction.ELEMENT_TYPE_SHOP:
				Shop shop= (Shop)element;
				holder.city.setVisibility(View.VISIBLE);
				holder.adr.setVisibility(View.VISIBLE);
				holder.city.setText(shop.getCity() );
				holder.adr.setText(shop.getAdr() );
				holder.title.setText(shop.getTitle() );
				break;
			case O.interaction.ELEMENT_TYPE_GROUP:
				Group group= (Group)element;
				holder.title.setText(group.getTitle() );
				break;
			}
		holder.delListener.setElement(element);
		holder.editListener.setElement(element);
		}
	@Override
	public int getItemCount()
		{
		return elements.size();
		}
	}
