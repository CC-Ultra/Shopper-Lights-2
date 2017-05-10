package com.ultra.shopperlights2.Adapters;


import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.Callbacks.DialogDecision;
import com.ultra.shopperlights2.Callbacks.UpdateListCallback;
import com.ultra.shopperlights2.Fragments.AddGroupDialog;
import com.ultra.shopperlights2.Fragments.AddShopDialog;
import com.ultra.shopperlights2.Fragments.AddTagDialog;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.*;
import com.ultra.shopperlights2.Utils.ConfirmDialog;
import com.ultra.shopperlights2.Utils.O;
import java.util.ArrayList;
import java.util.List;

/**
 * <p></p>
 * <p><sub>(07.05.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class GTSAdapter extends RecyclerView.Adapter<GTSAdapter.Holder>
	{
	private FragmentManager fragmentManager;
	private ArrayList<RecyclerListElement> elements= new ArrayList<>();
	private UpdateListCallback callback;
	private Context context;

	class ConfirmDialogDecision implements DialogDecision
		{
		RecyclerListElement element;

		public ConfirmDialogDecision(RecyclerListElement _element)
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
		RecyclerListElement element;
		ConfirmDialogDecision dialogDecision;

		void setElement(RecyclerListElement _element)
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
		RecyclerListElement element;

		void setElement(RecyclerListElement _element)
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
					dialog.init(callback,"Изменить группу",group.getId() );
					dialog.show(transaction,"");
					break;
					}
				case O.interaction.ELEMENT_TYPE_TAG:
					{
					Tag tag=(Tag) element;
					AddTagDialog dialog=new AddTagDialog();
					dialog.init(callback,"Изменить группу",tag.getId());
					dialog.show(transaction,"");
					break;
					}
				case O.interaction.ELEMENT_TYPE_SHOP:
					{
					Shop shop= (Shop)element;
					AddShopDialog dialog=new AddShopDialog();
					dialog.init(callback,"Изменить магазин",shop.getId() );
					dialog.show(transaction,"");
					break;
					}
				}
			}
		}
	class Holder extends RecyclerView.ViewHolder
		{
		TextView title,city,adr;
		ImageButton btnEdit,btnDel;
		DelListener delListener;
		EditListener editListener;

		public Holder(View itemView)
			{
			super(itemView);
			delListener= new DelListener();
			editListener= new EditListener();
			title= (TextView)itemView.findViewById(R.id.title);
			city= (TextView)itemView.findViewById(R.id.city);
			adr= (TextView)itemView.findViewById(R.id.adr);
			btnDel= (ImageButton)itemView.findViewById(R.id.deleteBtn);
			btnEdit= (ImageButton)itemView.findViewById(R.id.editBtn);
			btnDel.setOnClickListener(delListener);
			btnEdit.setOnClickListener(editListener);
			}
		}

	public GTSAdapter(Context _context,ArrayList<RecyclerListElement> _elements,UpdateListCallback _callback,FragmentManager _fragmentManager)
		{
		fragmentManager=_fragmentManager;
		context=_context;
		callback=_callback;
		elements=_elements;
		}
	private void deleteShop(Shop shop)
		{
		ArrayList<Purchase> purchases= new ArrayList<>(shop.getPurchases() );
		for(Purchase purchase : purchases)
			purchase.setShopId(0);
		App.session.getShopDao().delete(shop);
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
	private void delElement(RecyclerListElement element,int position)
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
		RecyclerListElement element= elements.get(position);
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
