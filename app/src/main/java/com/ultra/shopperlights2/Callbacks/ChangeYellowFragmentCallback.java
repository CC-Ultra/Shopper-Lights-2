package com.ultra.shopperlights2.Callbacks;

import com.ultra.shopperlights2.Activities.MainActivity;

/**
 * <p>Интерфейс, переключающий фрагменты желтого экрана. Включает/выключает режим покупки</p>
 * <p><sub>(09.05.2017)</sub></p>
 * @see MainActivity
 * @author CC-Ultra
 */

public interface ChangeYellowFragmentCallback
	{
	void changeYellowFragment(boolean state);
	}
