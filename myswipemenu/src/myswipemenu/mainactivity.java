package myswipemenu;

import java.util.ArrayList;
import java.util.List;

import com.zzt.myswipemenu.R;
import com.zzt.swipemenu.ZztSwipeAdapter;
import com.zzt.swipemenu.ZztSwipeLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.ViewDragHelper;
import android.support.v4.widget.ViewDragHelper.Callback;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi") 
public class mainactivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.list);
		ListView list = (ListView)findViewById(R.id.list);
		/*final TextView tv = (TextView)findViewById(R.id.tv);	
		tv.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				tv.setText("hello  world~~");
			}
		});
		final TextView tv3 = (TextView)findViewById(R.id.tv3);	
		tv3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				tv3.setText("tv3 click~~~~~~");
			}
		});*/
		ListAdapter adapter = new ZztSwipeAdapter(mainactivity.this) {					
			@Override
			public long getItemId(int position) {
				return position;
			}			
			@Override
			public Object getItem(int position) {
				return null;
			}			
			@Override
			public int getCount() {
				return 20;
			}
			@Override
			public int getZztSwipeLayoutId(int position) {
				return R.id.frame;
			}
			@Override
			public int getItemLayoutId(int position) {
				return R.layout.item;
			}
			@Override
			public List<View> getViewHolder(View convertView, int position) {
				List<View> viewlist = new ArrayList<View>();//new List<View>();
				viewlist.add(convertView.findViewById(R.id.tv));
				viewlist.add(convertView.findViewById(R.id.tv2));
				viewlist.add(convertView.findViewById(R.id.tv3));
				return viewlist;
			}
			@Override
			public void fillValues(ViewHolder viewHolder, final int position) {
				TextView tv = (TextView)viewHolder.viewHolder.get(0);
				TextView tv2 = (TextView)viewHolder.viewHolder.get(1);
				TextView tv3 = (TextView)viewHolder.viewHolder.get(2);
				tv.setText("hello world "+position);
				tv2.setOnClickListener(new OnClickListener() {				
					@Override
					public void onClick(View v) {
						System.out.println("tv2 click " + position);
					}
				});
			}			
		};
		
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				System.out.println("item click "+position);
				Intent intent = new Intent(mainactivity.this, null);
			}
		});
	}
}
