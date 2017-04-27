package com.ultra.shopperlights2.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.ultra.shopperlights2.Units.DropdownListView;

import java.util.ArrayList;
import java.util.List;

import static com.ultra.shopperlights2.Utils.O.TAG;

/**
 * <p></p>
 * <p><sub>(27.12.2016)</sub></p>
 *
 * @author CC-Ultra
 */

public class GreenDropdownAdapter extends ArrayAdapter<String>
	 {
	 ArrayList<String> data;
	 Context context;
	 public GreenDropdownAdapter(Context _context,int resource,List<String> objects)
		 {
		 super(_context,resource,objects);
		 data= new ArrayList<>(objects);
		 context=_context;
		 }

	 @NonNull
	 @Override
	 public View getView(int position,View convertView,ViewGroup parent)
		 {
		 Log.d(TAG,"getView: "+ getCount() );
//		 LayoutInflater inflater;
//		 inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 DropdownListView item= new DropdownListView(context);
//		 convertView= inflater.inflate(R.layout.dropdown_listview, parent, false);
		 item.setTitle(data.get(position) );
		 Log.d(TAG,"getView: "+ position);
		 return item;
		 }
	 }
