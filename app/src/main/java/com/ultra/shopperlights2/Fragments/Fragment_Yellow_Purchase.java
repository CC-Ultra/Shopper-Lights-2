package com.ultra.shopperlights2.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.ultra.shopperlights2.Callbacks.ChangeYellowFragmentCallback;
import com.ultra.shopperlights2.R;

import java.util.ArrayList;

/**
 * <p></p>
 * <p><sub>(07.08.2016)</sub></p>
 *
 * @author CC-Ultra
 */
public class Fragment_Yellow_Purchase extends Fragment
	{
	 private ChangeYellowFragmentCallback callback;
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
			 long drawerItemId= drawerItem.getIdentifier();
			 drawer.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
			 String toastTxt= drawerItem.getTag().toString();
			 Toast.makeText(getActivity(),toastTxt,Toast.LENGTH_SHORT).show();
			 strs.remove(position-1);
			 drawer.removeItem(drawerItemId);
//			 initDrawerList();
			 drawer.getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
			 return true;
			 }
		 }

	 public void initFragment(Drawer _drawer,ChangeYellowFragmentCallback _callback)
		{
		 callback=_callback;
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
			 {
			 PrimaryDrawerItem drawerItem= new PrimaryDrawerItem().withName(str);
			 drawerItem.withTag(2);
			 drawer.addItem(drawerItem);
			 }
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
