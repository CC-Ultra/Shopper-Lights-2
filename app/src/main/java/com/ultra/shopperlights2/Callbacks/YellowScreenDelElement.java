package com.ultra.shopperlights2.Callbacks;

import com.ultra.shopperlights2.Fragments.Fragment_Yellow_Purchase;
import com.ultra.shopperlights2.Adapters.YellowPurchaseListAdapter;

/**
 * <p>Интерфейс удаляет продукт из адаптера</p>
 * <p><sub>(10.05.2017)</sub></p>
 * @see YellowPurchaseListAdapter
 * @see Fragment_Yellow_Purchase
 * @author CC-Ultra
 */

public interface YellowScreenDelElement
	{
	void delElement(String title);
	}
