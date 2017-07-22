package com.ultra.shopperlights2.Callbacks;

import android.view.ViewGroup;
import com.ultra.shopperlights2.Activities.EditPurchaseActivity;
import com.ultra.shopperlights2.Fragments.Fragment_Yellow_Purchase;
import com.ultra.shopperlights2.Adapters.EditPurchaseAdapter;
import com.ultra.shopperlights2.Adapters.YellowPurchaseListAdapter;

/**
 * <p>Интерфейс вызывается из адаптеров и инициализирует диалог для изменения продукта</p>
 * <p><sub>(10.05.2017)</sub></p>
 * @see EditPurchaseActivity
 * @see Fragment_Yellow_Purchase
 * @see EditPurchaseAdapter
 * @see YellowPurchaseListAdapter
 * @author CC-Ultra
 */

public interface EditProductCallback
	{
	void initDialog(ViewGroup parent,long id);
	}
