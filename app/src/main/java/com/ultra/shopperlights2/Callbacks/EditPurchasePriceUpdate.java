package com.ultra.shopperlights2.Callbacks;

import com.ultra.shopperlights2.Activities.EditPurchaseActivity;
import com.ultra.shopperlights2.Adapters.EditPurchaseAdapter;

/**
 * <p>Интерфейс нужен для обновления цены после удаления продукта в {@link EditPurchaseActivity}
 * из адаптера</p>
 * <p><sub>(11.06.2017)</sub></p>
 * @see EditPurchaseActivity
 * @see EditPurchaseAdapter
 * @author CC-Ultra
 */

public interface EditPurchasePriceUpdate
	{
	void updatePrice();
	}
