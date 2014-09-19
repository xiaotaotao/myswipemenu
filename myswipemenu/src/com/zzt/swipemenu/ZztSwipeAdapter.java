package com.zzt.swipemenu;

import java.util.List;

import myswipemenu.mainactivity;

import com.zzt.myswipemenu.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class ZztSwipeAdapter extends BaseAdapter {
	
	private int mOpenPosition = -1;
	private ZztSwipeLayout prevOpenLayout;
	private LayoutInflater inflater;
	
	public abstract int getZztSwipeLayoutId(int position);
	public abstract int getItemLayoutId(int position);
	public abstract List<View> getViewHolder(View convertView, int position);
	public abstract void fillValues(ViewHolder viewHolder, int position);
	
	@Override
	public Object getItem(int position) {
		return null;
	}
	
	public ZztSwipeAdapter(Context context){
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		ZztSwipeLayout swipe;
		if(convertView==null||position==this.mOpenPosition){
			convertView= inflater.inflate(this.getItemLayoutId(position), null);
			swipe = (ZztSwipeLayout)convertView.findViewById(this.getZztSwipeLayoutId(position));
			viewHolder = new ViewHolder();
			viewHolder.viewHolder = this.getViewHolder(convertView, position);			
			if(swipe!=null){
				viewHolder.listener = new ZztSwipeAdapterStatusListener(position, swipe);
				swipe.addZztSwipeStatusInterface(viewHolder.listener);	
			}			
			convertView.setTag(viewHolder);			
		}
		else{
			viewHolder = (ViewHolder)convertView.getTag();	
			swipe = (ZztSwipeLayout)convertView.findViewById(this.getZztSwipeLayoutId(position));	
			if(swipe!=null){
				viewHolder.listener.mposition = position;
			}
		}			
		this.fillValues(viewHolder, position);		
		if(mOpenPosition!=position&&swipe!=null){
			swipe.close(false);
		}
		else if(mOpenPosition==position&&swipe!=null){			
			swipe.open();			
		}
		return convertView;
	}
	
	class ZztSwipeAdapterStatusListener implements ZztSwipeLayout.ZztSwipeStatusListener{		
		private int mposition;
		public ZztSwipeAdapterStatusListener(int position, ZztSwipeLayout swipe){
			this.mposition = position;
			if(position==ZztSwipeAdapter.this.mOpenPosition){
				ZztSwipeAdapter.this.prevOpenLayout = swipe;
			}
		}
		@Override
		public void onOpen(ZztSwipeLayout layout, int position) {	
			if(ZztSwipeAdapter.this.prevOpenLayout!=layout&&ZztSwipeAdapter.this.prevOpenLayout!=null){
				ZztSwipeAdapter.this.prevOpenLayout.close(true);
			}
			ZztSwipeAdapter.this.prevOpenLayout = layout;
			ZztSwipeAdapter.this.mOpenPosition = position;
		}		
		public void onClose(){
			if(this.mposition == ZztSwipeAdapter.this.mOpenPosition){
				ZztSwipeAdapter.this.mOpenPosition = -1;
				ZztSwipeAdapter.this.prevOpenLayout = null;
			}
		}
		@Override
		public int getPosition() {
			return this.mposition;
		}
	}
	public class ViewHolder{
		private ZztSwipeAdapterStatusListener listener;
		public List<View> viewHolder = null;
	}
}
