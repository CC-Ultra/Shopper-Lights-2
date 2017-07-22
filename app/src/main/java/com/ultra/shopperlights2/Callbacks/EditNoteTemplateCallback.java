package com.ultra.shopperlights2.Callbacks;

import android.view.ViewGroup;
import com.ultra.shopperlights2.Activities.AddTemplateNoteActivity;
import com.ultra.shopperlights2.Adapters.TemplatesNotesAdapter;

/**
 * <p>Интерфейс запускает диалог на редактирование записи шаблона. Вызывается метод из адаптера, из EditListener-а</p>
 * <p><sub>(06.06.2017)</sub></p>
 * @see AddTemplateNoteActivity
 * @see TemplatesNotesAdapter
 * @author CC-Ultra
 */

public interface EditNoteTemplateCallback
	{
	void initNoteFragment(ViewGroup parent,long noteId,long templateId);
	}
