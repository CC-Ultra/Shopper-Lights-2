package com.ultra.shopperlights2.Adapters;


import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.Callbacks.DialogDecision;
import com.ultra.shopperlights2.Dialogs.AddGroupDialog;
import com.ultra.shopperlights2.Dialogs.AddShopDialog;
import com.ultra.shopperlights2.Dialogs.AddTagDialog;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.*;
import com.ultra.shopperlights2.Utils.ConfirmDialog;
import com.ultra.shopperlights2.Utils.O;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Адаптер для списка GTS-активности. Работает со списком тегов, групп, записей и магазинов</p>
 * <p><sub>(07.05.2017)</sub></p>
 * @author CC-Ultra
 */

public class GTSAdapter extends RecyclerView.Adapter<GTSAdapter.Holder>
	{
	private String action;
	private ArrayList<GreenRecyclerListElement> elements= new ArrayList<>();
	private Context context;

	/**
	 * Реализация интерфейса, удаляющая gts-элемент, соответствующим его типу образом
	 */
	private class ConfirmDialogDecision implements DialogDecision
		{
		GreenRecyclerListElement element;

		ConfirmDialogDecision(GreenRecyclerListElement _element)
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

	/**
	 * удаление элемента идет с подтверждением, так что его описал в {@link ConfirmDialogDecision}
	 */
	private class DelListener implements View.OnClickListener
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

	/**
	 * запуск диалога в зависимости от типа. context и action нужны для этого
	 */
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
			switch(element.getGTSType() )
				{
				case O.interaction.ELEMENT_TYPE_GROUP:
					{
					Group group= (Group)element;
					AddGroupDialog dialog= new AddGroupDialog();
					dialog.init(context,(ViewGroup)v.getParent(),action,"Изменить группу",group.getId() );
					dialog.createAndShow();
					break;
					}
				case O.interaction.ELEMENT_TYPE_TAG:
					{
					Tag tag= (Tag)element;
					AddTagDialog dialog=new AddTagDialog();
					dialog.init(context,(ViewGroup)v.getParent(),action,"Изменить тег",tag.getId());
					dialog.createAndShow();
					break;
					}
				case O.interaction.ELEMENT_TYPE_SHOP:
					{
					Shop shop= (Shop)element;
					AddShopDialog dialog=new AddShopDialog();
					dialog.init(context,(ViewGroup)v.getParent(),action,"Изменить магазин",shop.getId() );
					dialog.createAndShow();
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

	public GTSAdapter(Context _context,ArrayList<GreenRecyclerListElement> _elements,String _action)
		{
		action=_action;
		context=_context;
		elements=_elements;
		}

	/**
	 * магазины не удаляются, а просто "деактивируются"
	 */
	private void deleteShop(Shop shop)
		{
		shop.setAlive(false);
		App.session.getShopDao().update(shop);
		}

	/**
	 * Кроме удаления тега надо удалить упоминания о нем в {@code TagToNote} и {@code TagToProduct}
	 */
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

	/**
	 * все записи, которые были в группе, становятся самостоятельными, без группы
	 */
	private void deleteGroup(Group group)
		{
		ArrayList<Note> notes= new ArrayList<>(group.getNotes() );
		for(Note note : notes)
			note.setGroupId(0);
		App.session.getGroupDao().delete(group);
		}

	/**
	 * удалить из gts-списка
	 */
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

	/**
	 * В зависимости от типа заполняются разные поля holder-а
	 */
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
