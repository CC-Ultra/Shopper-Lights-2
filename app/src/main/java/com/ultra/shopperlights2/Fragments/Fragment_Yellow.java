package com.ultra.shopperlights2.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.ultra.shopperlights2.R;

import java.util.ArrayList;

/**
 * <p></p>
 * <p><sub>(07.08.2016)</sub></p>
 *
 * @author CC-Ultra
 */
public class Fragment_Yellow extends Fragment
	{
	 private Drawer drawer;
	 private ArrayList<String> strs= new ArrayList<>();

	 class DrawerButtonClickListener implements View.OnClickListener
		{
		 @Override
		 public void onClick(View v)
			{
			 drawer.openDrawer();
			 }
		 }
	 class DrawerItemClickListener implements Drawer.OnDrawerItemClickListener
		{
		 @Override
		 public boolean onItemClick(View view,int position,IDrawerItem drawerItem)
			{
			 drawer.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
			 String toastTxt= strs.get(position-1);
			 Toast.makeText(getActivity(),toastTxt,Toast.LENGTH_SHORT).show();
			 strs.remove(position-1);
			 initDrawerList();
			 drawer.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
			 return true;
			 }
		 }

	 public void initFragment(Drawer _drawer)
		{
		 drawer=_drawer;
		 strs.add("Молоко");
		 strs.add("Сметана");
		 strs.add("Хлеб");
		 strs.add("Пылесос");
		 initDrawerList();
		 drawer.setOnDrawerItemClickListener(new DrawerItemClickListener() );
		 }
	 private void initDrawerList()
		{
		 drawer.removeAllItems();
		 for(String str : strs)
			 drawer.addItem(new PrimaryDrawerItem().withName(str) );
		 drawer.deselect();
		 }

	 @Nullable
	 @Override
	 public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState)
		{
		 View view= inflater.inflate(R.layout.yellow_screen,container,false);

		 ImageButton button_close= (ImageButton)view.findViewById(R.id.btn_openDrawer);

		 button_close.setOnClickListener(new DrawerButtonClickListener() );
		 return view;
		 }
	 }
