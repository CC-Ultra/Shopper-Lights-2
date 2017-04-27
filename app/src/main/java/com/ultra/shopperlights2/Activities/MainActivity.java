package com.ultra.shopperlights2.Activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.ultra.shopperlights2.Fragments.Fragment_Green;
import com.ultra.shopperlights2.Fragments.Fragment_Red;
import com.ultra.shopperlights2.Fragments.Fragment_Yellow;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Utils.O;

public class MainActivity extends AppCompatActivity
	{
	public static int selectedScreen;
	//	 public static SharedPreferences prefs;
//	public static DbHelper dbHelper;
	private FragmentManager fragmentManager;
	private Drawer drawer;
	//	 private boolean drawerState;
	private Fragment fragments[]= new Fragment[3];
	private ImageButton buttons[]= new ImageButton[3];

	private class DrawerHeaderClickListener implements View.OnClickListener
		{
		@Override
		public void onClick(View v)
			{
			if(v.getId() == R.id.btn_close)
				{
				drawer.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
				drawer.closeDrawer();
				}
			else
				Toast.makeText(MainActivity.this,"Add",Toast.LENGTH_SHORT).show();
			}
		}
	private class LightsClickListener implements View.OnClickListener
		{
		int color;

		LightsClickListener(int _color)
			{
			color=_color;
			}
		@Override
		public void onClick(View v)
			{
			if(color == O.interaction.SCREEN_CODE_YELLOW)
				drawer.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
			else
				drawer.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
			buttons[selectedScreen].setImageResource(R.drawable.yellow_light);
			buttons[color].setImageResource(R.drawable.red_light);
			selectedScreen=color;
			FragmentTransaction transaction= fragmentManager.beginTransaction();
			transaction.replace(R.id.fragmentContainer,fragments[color] );
			transaction.commit();
			}
		}

	@Override
	protected void onCreate(Bundle savedInstanceState)
		{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);

		selectedScreen= getIntent().getIntExtra(O.mapKeys.extra.START_COLOR, O.interaction.SCREEN_CODE_GREEN);

		DrawerBuilder builder= new DrawerBuilder();
		builder.withActivity(this).withHeader(R.layout.drawer_header);
		drawer= builder.build();
		if(selectedScreen == O.interaction.SCREEN_CODE_YELLOW)
			drawer.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
		else
			drawer.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		View drawerHeader= drawer.getHeader();

		ImageButton drawerButton_close= (ImageButton)drawerHeader.findViewById(R.id.btn_close);
		Button drawerButton_add= (Button)drawerHeader.findViewById(R.id.btn_add);
		ImageButton button_Green= (ImageButton)findViewById(R.id.btnScreen_green);
		ImageButton button_Yellow= (ImageButton)findViewById(R.id.btnScreen_yellow);
		ImageButton button_Red= (ImageButton)findViewById(R.id.btnScreen_red);
		buttons[O.interaction.SCREEN_CODE_GREEN]= button_Green;
		buttons[O.interaction.SCREEN_CODE_YELLOW]= button_Yellow;
		buttons[O.interaction.SCREEN_CODE_RED]= button_Red;

		fragments[O.interaction.SCREEN_CODE_GREEN]= new Fragment_Green();
		fragments[O.interaction.SCREEN_CODE_YELLOW]= new Fragment_Yellow();
		fragments[O.interaction.SCREEN_CODE_RED]= new Fragment_Red();
		( (Fragment_Yellow)fragments[O.interaction.SCREEN_CODE_YELLOW] ).initFragment(drawer);
		fragmentManager= getSupportFragmentManager();
		FragmentTransaction transaction= fragmentManager.beginTransaction();
		transaction.add(R.id.fragmentContainer,fragments[selectedScreen]);
		transaction.commit();
		buttons[selectedScreen].setImageResource(R.drawable.red_light);

		drawerButton_add.setOnClickListener(new DrawerHeaderClickListener() );
		drawerButton_close.setOnClickListener(new DrawerHeaderClickListener() );
		button_Green.setOnClickListener(new LightsClickListener(O.interaction.SCREEN_CODE_GREEN) );
		button_Yellow.setOnClickListener(new LightsClickListener(O.interaction.SCREEN_CODE_YELLOW) );
		button_Red.setOnClickListener(new LightsClickListener(O.interaction.SCREEN_CODE_RED) );
		}
	}

