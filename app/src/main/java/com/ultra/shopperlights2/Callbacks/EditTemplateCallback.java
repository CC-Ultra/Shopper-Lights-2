package com.ultra.shopperlights2.Callbacks;

import android.view.ViewGroup;
import com.ultra.shopperlights2.Activities.AddTemplateActivity;
import com.ultra.shopperlights2.Adapters.TemplatesAdapter;

/**
 * <p>Интерфейс, запускающий диалог редактирования шаблона из адаптера в EditListener-е</p>
 * <p><sub>(05.06.2017)</sub></p>
 * @see AddTemplateActivity
 * @see TemplatesAdapter
 * @author CC-Ultra
 */

public interface EditTemplateCallback
	{
	void editTemplate(ViewGroup parent,long templateId);
	}
