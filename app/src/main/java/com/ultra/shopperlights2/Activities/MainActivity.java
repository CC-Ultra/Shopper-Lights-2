package com.ultra.shopperlights2.Activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.ultra.shopperlights2.App;
import com.ultra.shopperlights2.Callbacks.ChangeYellowFragmentCallback;
import com.ultra.shopperlights2.Fragments.*;
import com.ultra.shopperlights2.R;
import com.ultra.shopperlights2.Units.PurchaseDao;
import com.ultra.shopperlights2.Utils.Calc;
import com.ultra.shopperlights2.Utils.O;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ChangeYellowFragmentCallback
	 {
	 public static boolean purchaseState;
	 private Drawer drawer;
	 private Fragment fragments[]= new Fragment[3];
	 private ViewPager pager;
	 private ViewPagerFragment_Lights lightsFragment;
	 private int selectedScreen;
	 private XPagerAdapter pagerAdapter;

	 private class PagerListener implements ViewPager.OnPageChangeListener
		 {
		 ViewGroup.LayoutParams layoutParams= pager.getLayoutParams();
		 int minWidth= Calc.dpToPx(MainActivity.this,O.dimens.ARROW_PAGER_WIDTH);
		 int maxWidth;

		 public PagerListener()
			 {
			 int orientation= getResources().getConfiguration().orientation;
			 if(orientation == Configuration.ORIENTATION_LANDSCAPE)
				 maxWidth= Calc.dpToPx(MainActivity.this,O.dimens.LIGHTS_PAGER_WIDTH_LAND);
			 else
				 maxWidth= Calc.dpToPx(MainActivity.this,O.dimens.LIGHTS_PAGER_WIDTH_PORT);
			 }

		 @Override
		 public void onPageSelected(int position)
			 {
			 if(position==0)
				 {
				 layoutParams.width=minWidth;
				 pager.setLayoutParams(layoutParams);
				 }
			 else
				 {
				 layoutParams.width=maxWidth;
				 pager.setLayoutParams(layoutParams);
				 }
			 }

		 @Override
		 public void onPageScrolled(int position,float positionOffset,int positionOffsetPixels) {}
		 @Override
		 public void onPageScrollStateChanged(int state) {}
		 }
	 private class PagerSizeListener implements View.OnLayoutChangeListener
		 {
		 @Override
		 public void onLayoutChange(View v,int left,int top,int right,int bottom,int oldLeft,int oldTop,int oldRight,int oldBottom)
			 {
			 int delta=right-left;
			 Log.d(O.TAG,"onLayoutChange: delta="+ delta +"\tMinSize="+ Calc.dpToPx(MainActivity.this,O.dimens.ARROW_PAGER_WIDTH) );
			 if(delta == Calc.dpToPx(MainActivity.this,O.dimens.ARROW_PAGER_WIDTH) )
				 {
				 pager.setCurrentItem(1);
				 pager.setCurrentItem(0);
				 }
			 }
		 }
	 private class XPagerAdapter extends FragmentPagerAdapter
		 {
		 ArrayList<ViewPagerFragment_Basic> fragments= new ArrayList<>();

		 public XPagerAdapter(FragmentManager fm)
			 {
			 super(fm);
			 ViewPagerFragment_Arrow fragment_arrow=new ViewPagerFragment_Arrow();
			 fragment_arrow.init(pager);
			 fragments.add(fragment_arrow);
			 lightsFragment= new ViewPagerFragment_Lights();
			 PendingIntent pendingIntent= createPendingResult(0,new Intent(),0);
			 lightsFragment.init(pendingIntent,pager);
			 fragments.add(lightsFragment);
			 notifyDataSetChanged();
			 }

		 @Override
		 public Fragment getItem(int position)
			 {
			 return fragments.get(position);
			 }
		 @Override
		 public int getCount()
			 {
			 return fragments.size();
			 }
		 }
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

	 private void checkPurchaseState()
		 {
		 int size= App.session.getPurchaseDao().queryBuilder().where(PurchaseDao.Properties.Completed.eq(false) ).list().size();
		 purchaseState= size!=0;
		 }
	 @Override
	 public void changeYellowFragment(boolean state)
		 {
		 purchaseState=state;
		 refreshFragments();
		 }
	 private void refreshFragments()
		 {
		 fragments[O.interaction.SCREEN_CODE_GREEN]= new Fragment_Green();
		 if(purchaseState)
			 {
			 fragments[O.interaction.SCREEN_CODE_YELLOW]= new Fragment_Yellow_Purchase();
			 ( (Fragment_Yellow_Purchase)fragments[O.interaction.SCREEN_CODE_YELLOW] ).initFragment(drawer,this);
			 }
		 else
			 {
			 fragments[O.interaction.SCREEN_CODE_YELLOW]= new Fragment_Yellow_Shop();
			 ( (Fragment_Yellow_Shop)fragments[O.interaction.SCREEN_CODE_YELLOW] ).initFragment(this,getSupportFragmentManager() );
			 }
		 fragments[O.interaction.SCREEN_CODE_RED]= new Fragment_Red();
		 selectScreen(selectedScreen);
		 }
	 private void selectScreen(int code)
		 {
		 selectedScreen=code;
		 FragmentManager fragmentManager= getSupportFragmentManager();
		 FragmentTransaction transaction= fragmentManager.beginTransaction();
		 transaction.replace(R.id.fragmentContainer,fragments[code] );
		 transaction.commit();
		 }

	 @Override
	 protected void onCreate(Bundle savedInstanceState)
		 {
		 super.onCreate(savedInstanceState);
		 setContentView(R.layout.main_layout);

		 selectedScreen= getIntent().getIntExtra(O.mapKeys.extra.START_COLOR, O.interaction.SCREEN_CODE_GREEN);
		 if(savedInstanceState!=null)
			 selectedScreen= savedInstanceState.getInt(O.mapKeys.extra.START_COLOR);

		 DrawerBuilder builder= new DrawerBuilder();
		 builder.withActivity(this).withHeader(R.layout.drawer_header);
		 drawer= builder.build();
		 if(selectedScreen == O.interaction.SCREEN_CODE_YELLOW)
			 drawer.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
		 else
			 drawer.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		 View drawerHeader= drawer.getHeader();

		 Toolbar toolbar= (Toolbar)findViewById(R.id.toolbar);
		 ImageButton drawerButton_close= (ImageButton)drawerHeader.findViewById(R.id.btn_close);
		 Button drawerButton_add= (Button)drawerHeader.findViewById(R.id.btn_add);
		 pager= (ViewPager)findViewById(R.id.pager);

		 pagerAdapter= new XPagerAdapter(getSupportFragmentManager() );
		 pager.setAdapter(pagerAdapter);
		 pager.addOnPageChangeListener(new PagerListener() );
		 pager.addOnLayoutChangeListener(new PagerSizeListener() );
		 setSupportActionBar(toolbar);
		 lightsFragment.lightMemory= selectedScreen;

		 ViewGroup.LayoutParams layoutParams= pager.getLayoutParams();
		 layoutParams.width= Calc.dpToPx(MainActivity.this,O.dimens.ARROW_PAGER_WIDTH);
		 pager.setLayoutParams(layoutParams);

		 drawerButton_add.setOnClickListener(new DrawerHeaderClickListener() );
		 drawerButton_close.setOnClickListener(new DrawerHeaderClickListener() );
		 }
	 @Override
	 protected void onResume()
		 {
		 super.onResume();
		 checkPurchaseState();
		 refreshFragments();
		 }
	 @Override
	 protected void onActivityResult(int requestCode,int resultCode,Intent data)
		 {
		 selectScreen(resultCode);
		 }
	 @Override
	 protected void onSaveInstanceState(Bundle outState)
		 {
		 super.onSaveInstanceState(outState);
		 outState.putInt(O.mapKeys.extra.START_COLOR,selectedScreen);
		 }
	 @Override
	 public boolean onCreateOptionsMenu(Menu menu)
		 {
		 getMenuInflater().inflate(R.menu.main_nenu, menu);
		 return true;
		 }
	 @Override
	 public boolean onOptionsItemSelected(MenuItem item)
		 {
		 Intent jumper;
		 switch(item.getItemId() )
			 {
			 case R.id.theme:
				 Toast.makeText(this,"Theme",Toast.LENGTH_SHORT).show();
				 break;
			 case R.id.groups:
				 jumper= new Intent(this,EditListActivity.class);
				 jumper.putExtra(O.mapKeys.extra.LIST_ELEMENT_TYPE, O.interaction.ELEMENT_TYPE_GROUP);
				 startActivity(jumper);
				 break;
			 case R.id.tags:
				 jumper= new Intent(this,EditListActivity.class);
				 jumper.putExtra(O.mapKeys.extra.LIST_ELEMENT_TYPE, O.interaction.ELEMENT_TYPE_TAG);
				 startActivity(jumper);
				 break;
			 case R.id.shops:
				 jumper= new Intent(this,EditListActivity.class);
				 jumper.putExtra(O.mapKeys.extra.LIST_ELEMENT_TYPE, O.interaction.ELEMENT_TYPE_SHOP);
				 startActivity(jumper);
				 break;
			 case R.id.help:
				 Toast.makeText(this,"help",Toast.LENGTH_SHORT).show();
				 break;
			 case R.id.about:
				 Toast.makeText(this,"about",Toast.LENGTH_SHORT).show();
				 break;
			 }
		 return true;
		 }
	 }

