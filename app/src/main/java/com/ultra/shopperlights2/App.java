package com.ultra.shopperlights2;

import android.app.Application;
import com.ultra.shopperlights2.Units.DaoMaster;
import com.ultra.shopperlights2.Units.DaoSession;
import org.greenrobot.greendao.database.Database;

/**
 * <p></p>
 * <p><sub>(28.04.2017)</sub></p>
 *
 * @author CC-Ultra
 */

public class App extends Application
	 {
	 public static DaoSession session;

	 @Override
	 public void onCreate()
		 {
		 super.onCreate();
		 DaoMaster.DevOpenHelper helper= new DaoMaster.DevOpenHelper(this,"ShopperLights2.db");
		 Database db= helper.getWritableDb();
		 session= new DaoMaster(db).newSession();
		 }
	 }
