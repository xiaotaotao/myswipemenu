package com.zzt.swipemenu;

import android.content.Context;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.FrameLayout;

public class ZztSwipeLayout extends FrameLayout{
	
	private final int MINFILING = this.dp2px(500);
	private final int LEFT = 0;
	private final int TOP  = 1;
	private final int RIGHT= 2;
	private final int BOTTOM=3;
	private ViewDragHelper mDragHelper;
	private ViewGroup surfacechildview;
	private ViewGroup bottomchildview;
	private ZztSwipeStatusListener iStatusListener = null;
    
	public ZztSwipeLayout(Context context) {
		this(context, null);		
	}
	public ZztSwipeLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}	
	public ZztSwipeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mDragHelper = ViewDragHelper.create(this, 2.0f, this.mDragHelperCallback);	
		
	}       
    /*private ViewDragHelper.Callback mDragHelperCallback = new ViewDragHelper.Callback() {
		@Override
		public int clampViewPositionVertical(View child, int top, int dy) {
            final int topBound = getPaddingTop();
            final int bottomBound = getHeight() - ZztSwipeLayout.this.surfacechildview.getHeight();
            final int newTop = Math.min(Math.max(top, topBound), bottomBound);
            //return newTop;
		}		
		@Override
		public int clampViewPositionHorizontal(View child, int left, int dx) {
	            if (mDragHorizontal || mDragCapture) {
	                final int leftBound = getPaddingLeft();
	                final int rightBound = getWidth() - ZztSwipeLayout.this.surfacechildview.getWidth();
	                final int newLeft = Math.min(Math.max(left, leftBound), rightBound);	                
	                return left>0?0:left; //return newLeft;
	            }
	            return super.clampViewPositionHorizontal(child, left, dx);
		}			
	};*/

    private enum Status {
        Middle, Open, Close
    }      
    private Status ZztSwipeLayoutStatus = Status.Close;    
    private void setZztSwipeLayoutStatus(Status status){
    	this.ZztSwipeLayoutStatus = status;
    }
    private Status getZztSwipeLayoutStatus(){
    	return ZztSwipeLayoutStatus;
    }
    private int[] surfaceViewRect = new int[]{0, 0, 0, 0};   
    private void saveSurfaceViewRect(int left, int top, int right, int bottom){  
    	this.surfaceViewRect[LEFT]  = left;
    	this.surfaceViewRect[TOP]   = top;
    	this.surfaceViewRect[RIGHT] = right;
    	this.surfaceViewRect[BOTTOM]= bottom;
    }
    
    private int[] bottomViewRect  = new int[]{0, 0, 0, 0};
    private void saveBottomViewRect(int left, int top, int right, int bottom){
    	this.bottomViewRect[LEFT]   = left;
    	this.bottomViewRect[TOP]    = top;
    	this.bottomViewRect[RIGHT]  = right;
    	this.bottomViewRect[BOTTOM] = bottom;
    }    
    
    private ViewDragHelper.Callback mDragHelperCallback = new ViewDragHelper.Callback() {
        @Override//���Է���һ��view,������true,��child������
        public boolean tryCaptureView(View child, int pointerId) {
        	Log.i("mDragHelperCallback", "tryCaptureView");
        	if(child==ZztSwipeLayout.this.bottomchildview)
        		return true;
        	else if(child==ZztSwipeLayout.this.surfacechildview)
        		return true;
        	else
        		return false;
        }
        @Override
        public int getViewHorizontalDragRange(View child) {
        	//Log.i("mDragHelperCallback", "getViewHorizontalDragRange");
            return 400;
        }
        @Override
        public int getViewVerticalDragRange(View child) {
        	//Log.i("mDragHelperCallback", "getViewVerticalDragRange");
            return 0;
        }        
    	@Override//left��child������,dx������仯��,����ֵ��ʾchild����λ��
        public int clampViewPositionHorizontal(View child, int left, int dx) {
        	//Log.i("mDragHelperCallback", "clampViewPositionHorizontal");
        	if(child == ZztSwipeLayout.this.surfacechildview){       		
        		if(left>=0){
        			ZztSwipeLayout.this.setZztSwipeLayoutStatus(Status.Close);
        			if(ZztSwipeLayout.this.iStatusListener!=null)
        				ZztSwipeLayout.this.iStatusListener.onClose();        			
        			return 0;
        		}
        		else if(left>(-ZztSwipeLayout.this.bottomchildview.getWidth()))
        			return left;
        		else if(left<=(-ZztSwipeLayout.this.bottomchildview.getWidth())){
        			ZztSwipeLayout.this.setZztSwipeLayoutStatus(Status.Open);
        			if(ZztSwipeLayout.this.iStatusListener!=null)
        				ZztSwipeLayout.this.iStatusListener.onOpen(ZztSwipeLayout.this, ZztSwipeLayout.this.iStatusListener.getPosition());
        			return -ZztSwipeLayout.this.bottomchildview.getWidth();
        		}
        	}
    		else if(child == ZztSwipeLayout.this.bottomchildview){
    			if(left<=ZztSwipeLayout.this.getWidth()-child.getWidth())
    				return ZztSwipeLayout.this.getWidth()-child.getWidth();
    			else if(left>=ZztSwipeLayout.this.getWidth())
    				return ZztSwipeLayout.this.getWidth();
    			return left;
    		}
        	return 0;
        }
        @Override//����ȷ����drag��view�µ�topλ��
        public int clampViewPositionVertical(View child, int top, int dy) {
            if(child == ZztSwipeLayout.this.surfacechildview){
            	return getPaddingTop();               
            }
            else if(child == ZztSwipeLayout.this.bottomchildview){
            	return getPaddingTop();
            }
            return top;
        }
        @Override//���϶���viewλ�ñ仯ʱ�򱻵���
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
        	//Log.i("mDragHelperCallback", "onViewPositionChanged");
        	if(changedView == ZztSwipeLayout.this.surfacechildview){        		
        		if(left<=(-ZztSwipeLayout.this.bottomchildview.getWidth())){//ǰ�����if else if ��layout״̬open close�����仯ʱ���ִ��
        			//System.out.println("onViewPositionChanged open");
        			ZztSwipeLayout.this.setZztSwipeLayoutStatus(Status.Open);
        			if(ZztSwipeLayout.this.iStatusListener!=null)
        				ZztSwipeLayout.this.iStatusListener.onOpen(ZztSwipeLayout.this, ZztSwipeLayout.this.iStatusListener.getPosition());
        			ZztSwipeLayout.this.bottomchildview.layout(ZztSwipeLayout.this.getWidth()-ZztSwipeLayout.this.bottomchildview.getWidth(), 0, ZztSwipeLayout.this.getWidth(), ZztSwipeLayout.this.bottomchildview.getHeight()); 
	    			ZztSwipeLayout.this.saveBottomViewRect(getWidth()-ZztSwipeLayout.this.bottomchildview.getWidth(), 0, getWidth(), ZztSwipeLayout.this.bottomchildview.getHeight());
	    			ZztSwipeLayout.this.saveSurfaceViewRect(-ZztSwipeLayout.this.bottomchildview.getWidth(), 0, -ZztSwipeLayout.this.bottomchildview.getWidth()+ZztSwipeLayout.this.surfacechildview.getWidth(), ZztSwipeLayout.this.surfacechildview.getHeight());	        		
	    			invalidate();
	    			return ;
        		}
        		else if(left>=0){
        			//System.out.println("onViewPositionChanged close");
        			if(ZztSwipeLayout.this.iStatusListener!=null)
        				ZztSwipeLayout.this.iStatusListener.onClose();        			
        			ZztSwipeLayout.this.setZztSwipeLayoutStatus(Status.Close);
	    			ZztSwipeLayout.this.saveBottomViewRect(ZztSwipeLayout.this.bottomchildview.getLeft(), ZztSwipeLayout.this.bottomchildview.getTop(), ZztSwipeLayout.this.bottomchildview.getRight(), ZztSwipeLayout.this.bottomchildview.getBottom());
	    			ZztSwipeLayout.this.saveSurfaceViewRect(ZztSwipeLayout.this.surfacechildview.getLeft(), ZztSwipeLayout.this.surfacechildview.getTop(), ZztSwipeLayout.this.surfacechildview.getRight(),ZztSwipeLayout.this.surfacechildview.getBottom());    			        			
	    			invalidate();
	    			return ;
        		}  
        		
        		if(ZztSwipeLayout.this.getZztSwipeLayoutStatus() == Status.Close){
	        		int newleft = left/2+ZztSwipeLayout.this.getWidth()-ZztSwipeLayout.this.bottomchildview.getWidth()/2;
	        		ZztSwipeLayout.this.bottomchildview.layout(newleft, 0, newleft+ZztSwipeLayout.this.bottomchildview.getWidth(), ZztSwipeLayout.this.bottomchildview.getHeight());
        		}
        		else if(ZztSwipeLayout.this.getZztSwipeLayoutStatus() == Status.Open){
        			ZztSwipeLayout.this.bottomchildview.offsetLeftAndRight(dx);
        		}
        	}
        	else if(changedView == ZztSwipeLayout.this.bottomchildview)
        		ZztSwipeLayout.this.surfacechildview.offsetLeftAndRight(dx);
            invalidate();
        }        
        @Override//view���ٱ�dragʱ�򱻵���,(��ָ�ɿ�)
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
        	Log.i("mDragHelperCallback", "onViewReleased");
            super.onViewReleased(releasedChild, xvel, yvel);
            if(releasedChild == ZztSwipeLayout.this.surfacechildview){
				int left = ZztSwipeLayout.this.surfacechildview.getLeft();			
				if(left<-ZztSwipeLayout.this.bottomchildview.getWidth()/2){
	    			ZztSwipeLayout.this.mDragHelper.smoothSlideViewTo(ZztSwipeLayout.this.surfacechildview, -ZztSwipeLayout.this.bottomchildview.getWidth(), 0);
				}
	    		else{	
	    			ZztSwipeLayout.this.mDragHelper.smoothSlideViewTo(ZztSwipeLayout.this.surfacechildview, 0, 0);			
	    		}
			}
            else if(releasedChild == ZztSwipeLayout.this.bottomchildview)
            {
            	int left = ZztSwipeLayout.this.bottomchildview.getLeft();
            	if(left>ZztSwipeLayout.this.getWidth()-ZztSwipeLayout.this.bottomchildview.getWidth()/2){
	    			System.out.println("close");
	    			ZztSwipeLayout.this.mDragHelper.smoothSlideViewTo(ZztSwipeLayout.this.bottomchildview, ZztSwipeLayout.this.getWidth(), 0);			
	    			ZztSwipeLayout.this.saveBottomViewRect(ZztSwipeLayout.this.bottomchildview.getLeft(), ZztSwipeLayout.this.bottomchildview.getTop(), ZztSwipeLayout.this.bottomchildview.getRight(), ZztSwipeLayout.this.bottomchildview.getBottom());
	    			ZztSwipeLayout.this.saveSurfaceViewRect(ZztSwipeLayout.this.surfacechildview.getLeft(), ZztSwipeLayout.this.surfacechildview.getTop(), ZztSwipeLayout.this.surfacechildview.getRight(), ZztSwipeLayout.this.surfacechildview.getBottom());    			            		
            	}
	    		else{	
					System.out.println("open");
	    			ZztSwipeLayout.this.mDragHelper.smoothSlideViewTo(ZztSwipeLayout.this.bottomchildview, ZztSwipeLayout.this.getWidth()-ZztSwipeLayout.this.bottomchildview.getWidth(), 0);
	    			ZztSwipeLayout.this.saveBottomViewRect(getWidth()-ZztSwipeLayout.this.bottomchildview.getWidth(), 0, getWidth(), ZztSwipeLayout.this.bottomchildview.getHeight());
	    			ZztSwipeLayout.this.saveSurfaceViewRect(-ZztSwipeLayout.this.bottomchildview.getWidth(), 0, -ZztSwipeLayout.this.bottomchildview.getWidth()+ZztSwipeLayout.this.surfacechildview.getWidth(), ZztSwipeLayout.this.surfacechildview.getHeight());            			    			
	    		}            	
            }
			ZztSwipeLayout.this.invalidate();   			
        }
    };        
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
    	//Log.i("dispatchTouchEvent", "dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }	
	//����onTouchEvent������,Intercept���ص���˼
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		/*final int action = MotionEventCompat.getActionMasked(ev);
		if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
			this.mDragHelper.cancel();
			return false;
		}*/
		return mDragHelper.shouldInterceptTouchEvent(ev);//����true��Ϣ����ǰview����,false��������view����
	}	
	private float sX = -1 , sY = -1;
	public boolean onTouchEvent(MotionEvent ev){	       
		int action = ev.getActionMasked();
        ViewParent parent = getParent();//LinearLayout
        gestureDetector.onTouchEvent(ev);
        switch (action){
            case MotionEvent.ACTION_DOWN:
                this.mDragHelper.processTouchEvent(ev);
                parent.requestDisallowInterceptTouchEvent(true);
                sX = ev.getRawX();
                sY = ev.getRawY();
                return true;
            case MotionEvent.ACTION_MOVE:	
                float distanceX = ev.getRawX() - sX;
                float distanceY = ev.getRawY() - sY;
                sX = ev.getRawX();
                sY = ev.getRawY();
                float angle = Math.abs(distanceY / distanceX);
                angle = (float)Math.toDegrees(Math.atan(angle));
            	if(angle>30){
                  parent.requestDisallowInterceptTouchEvent(false);
                    return false;            		
            	}            		
            	parent.requestDisallowInterceptTouchEvent(true);
            	mDragHelper.processTouchEvent(ev);            	
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:         	
            default:
                parent.requestDisallowInterceptTouchEvent(true);
                mDragHelper.processTouchEvent(ev);
        }
        return true;	//���뷵��true,��ʾ�Ѿ���ȷ����touch�¼�,���ܼ����������϶�*/	
	}	
    public ViewGroup getSurfaceView(){
        return this.surfacechildview;
    } 
    public ViewGroup getBottomView(){
    	return this.bottomchildview;
    }
	@Override//���������smoothSlideViewTo�й�,�鿴SDK��֪,��smoothSlideViewTo����trueʱ,Ҫ����continueSettling,�������ƽ�����ƶ�
	public void computeScroll() {		
		//super.computeScroll();
        if(mDragHelper.continueSettling(true)) {
        	this.postInvalidate(); //ViewCompat.postInvalidateOnAnimation(this);       	
        }        
	}	
    private GestureDetector gestureDetector = new GestureDetector(getContext(), new SwipeDetector());
    class SwipeDetector extends GestureDetector.SimpleOnGestureListener{
    	@Override
    	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
    		if(velocityX<-MINFILING){ 			
    			ZztSwipeLayout.this.mDragHelper.smoothSlideViewTo(ZztSwipeLayout.this.surfacechildview, -ZztSwipeLayout.this.bottomchildview.getWidth(), 0);
    			ZztSwipeLayout.this.invalidate();
    			return true;
    		}
    		else if(velocityX>MINFILING){
    			ZztSwipeLayout.this.mDragHelper.smoothSlideViewTo(ZztSwipeLayout.this.surfacechildview, 0, 0);
    			ZztSwipeLayout.this.invalidate();
    			return true;
    		} 
    		return false;
    	}    	
        @Override
        public boolean onDown(MotionEvent e) {       	
            return true;
        }
        @Override
        public boolean onSingleTapUp(MotionEvent e) {         	
            ViewParent t = getParent();//LinearLayout->listview          
            while(t != null) {
                if(t instanceof AdapterView){
                	System.out.println("AdapterView");
                    int p = ((AdapterView) t).getPositionForView(ZztSwipeLayout.this);
                    if( p != AdapterView.INVALID_POSITION &&
                            ((AdapterView) t).performItemClick(((AdapterView) t).getChildAt(p),p , ((AdapterView) t).getAdapter().getItemId(p)))
                        return true;
                }else{
                    if(t instanceof View && ((View) t).performClick())
                        return true;
                }
                t = t.getParent();
            }
            return false;
        }
        @Override
        public void onLongPress(MotionEvent e) {
        	performLongClick();
        }
	}	
    @Override
    protected void onFinishInflate() {
        this.surfacechildview = (ViewGroup)this.getChildAt(1);
        this.bottomchildview  = (ViewGroup)this.getChildAt(0);     
    }	
	@Override	//�����ܹ������view����,δ�ܻ��frame�Ŀ���
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		System.out.println("on attach window");
	}	
	@Override//�����ܹ������view����,�ڶ��α����õ�ʱ����ܻ��width
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		System.out.println("on measure");
	}	
	@Override//����onMeasure������,���������������view��λ��
	protected void onLayout(boolean changed, int left, int top, int right, int bottom){
		System.out.println("onlayout");
		super.onLayout(changed, left, top, right, bottom);
		if(this.ZztSwipeLayoutStatus == Status.Open){
			//this.surfacechildview.layout(this.surfaceViewRect[LEFT], this.surfaceViewRect[TOP], this.surfaceViewRect[RIGHT], this.surfaceViewRect[BOTTOM]);
			//this.bottomchildview.layout(this.bottomViewRect[LEFT], this.bottomViewRect[TOP], this.bottomViewRect[RIGHT], this.bottomViewRect[BOTTOM]);
			this.open();
			return ;
		}	
		this.close(false);
	}
    private int dp2px(float dp){
        return (int) (dp * getContext().getResources().getDisplayMetrics().density + 0.5f);
    }
       
    public void addZztSwipeStatusInterface(ZztSwipeStatusListener i){
    	this.iStatusListener = i;
    }
    public interface ZztSwipeStatusListener{
    	void onOpen(ZztSwipeLayout layout, int position);
    	void onClose();
    	int  getPosition();
    }
    public void close(boolean withAnmitation){
    	ZztSwipeLayout.this.setZztSwipeLayoutStatus(Status.Close);    	
    	if(withAnmitation==false){
			this.surfacechildview.layout(0, 0, this.getWidth(), this.getHeight());
			this.bottomchildview.layout(this.getWidth()-this.bottomchildview.getWidth()/2, 0, this.bottomchildview.getWidth()+this.getWidth()-this.bottomchildview.getWidth()/2, this.bottomchildview.getHeight());   	
    	}
    	else{
    		System.out.println("close with anmitation "+this.iStatusListener.getPosition());
			ZztSwipeLayout.this.mDragHelper.smoothSlideViewTo(ZztSwipeLayout.this.surfacechildview, 0, 0);	
			ZztSwipeLayout.this.invalidate();
    	}
    }
    public void open(){
    	ZztSwipeLayout.this.setZztSwipeLayoutStatus(Status.Open);
		this.surfacechildview.layout(-this.bottomchildview.getWidth(), 0, this.surfacechildview.getWidth()-this.bottomchildview.getWidth(), this.surfacechildview.getHeight());
		this.bottomchildview.layout(this.getWidth()-this.bottomchildview.getWidth(), 0, this.getWidth(), this.bottomchildview.getHeight());    	
    }
}