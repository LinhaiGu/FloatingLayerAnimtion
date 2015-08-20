package com.example.floatinglayeranimtion;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class FloatingLayerView extends LinearLayout implements OnTouchListener {

	/**
	 * 视图显示类型。
	 */
	private int type=NONE;
	/**
	 * 浮层的高度。
	 */
	private int floating_height;
	/**
	 * 浮层的宽度
	 */
	private int floating_width;
	
	/**
	 * 滑动高度
	 */
	private float move_height;
	
	/**
	 * 是否向下滑动，交由onTouch事件处理。
	 */
	private boolean isCanHide=false;
	/**
	 * 是否进行动画
	 */
	private boolean isCanAnimation=false;
	
	/**
	 * 触发拦截触摸事件时的坐标点。 
	 * 按下：
	 *  interceptTouch_X:按下时的X坐标点。
	 *  interceptTouch_Y:按下时的Y坐标点。
	 * 滑动： 
	 * interceptMove_X:滑动时的X坐标点。 
	 * interceptMove_Y:滑动时的Y坐标点。 
	 * 距离：
	 * interceptTouch_Move_X:从按下到滑动之间的距离（横向滑动）
	 * interceptTouch_Move_Y:从按下到滑动之间的距离（纵向滑动）
	 * 滑动距离：
	 * moveLength:根据此值判断是否进行了滑动。
	 */
	private float interceptTouch_X;
	private float interceptTouch_Y;
	private float interceptMove_X;
	private float interceptMove_Y;
	private float interceptTouch_Move_X;
	private float interceptTouch_Move_Y;
	private int moveLength=10;
	
	/**
	 * 触发触摸事件时的坐标点
	 * down_X:按下时的X坐标点。
	 * down_Y:按下时的Y坐标点。
	 * move_X:移动时的X坐标点。
	 * move_Y:移动时的Y坐标点。
	 * down_move_X:横向滑动的距离。
	 * down_move_Y:纵向滑动的距离。
	 */
	private float down_X;
	private float down_Y;
	private float move_X;
	private float move_Y;
	private float down_move_X;
	private float down_move_Y;

	/**
	 * 定义三种浮层显示类型 
	 * 0：不显示 1：显示一半 2：全部显示
	 */
	private static final int NONE=0;
	private static final int HALF=1;
	private static final int ALL=2;
	
	public FloatingLayerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnTouchListener(this);
	}

	public FloatingLayerView(Context context) {
		super(context);
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		if(hasWindowFocus){
			floating_width=getWidth();
			floating_height=getHeight();
			/**
			 * 每次滑动的距离是当前View宽度的三分之一。
			 */
			move_height=floating_height/3;
		}
		super.onWindowFocusChanged(hasWindowFocus);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		/**
		 * 当按下时获取x,y的坐标点。
		 */
		case MotionEvent.ACTION_DOWN:
			interceptTouch_X = ev.getX();
			interceptTouch_Y = ev.getY();
			isCanAnimation=true;
			break;
			/**
			 * 当滑动时操作如下：
			 * 1、获取滑动距离
			 * 2、判断向上滑动还是向下滑动
			 * 3、向上滑动时
			 * 4、向下滑动时，判断当前显示方式：
			 * （1）、显示一半时，交由onTouch事件处理。
			 * （2）、全部显示时，是否向下滑动交由当前View的子View处理，
			 * 是否交由onTouch事件处理。
			 */
		case MotionEvent.ACTION_MOVE:
			interceptMove_X = ev.getX();
			interceptMove_Y = ev.getY();
			interceptTouch_Move_X = Math
					.abs(interceptTouch_X - interceptMove_X);
			interceptTouch_Move_Y = Math
					.abs(interceptTouch_Y - interceptMove_Y);
			/**
			 * 向下滑动
			 */
			if(interceptMove_Y>interceptTouch_Y&&interceptTouch_Move_Y>moveLength&&interceptTouch_Move_Y>interceptTouch_Move_X){
				return isDounTransferOnTouch();
			}
			/**
			 * 向上滑动
			 */
			if(interceptTouch_Y>interceptMove_Y&&interceptTouch_Move_Y>moveLength&&interceptTouch_Move_Y>interceptTouch_Move_X){
				return isUpTransferOnTouch();
			}
			break;
		case MotionEvent.ACTION_UP:
			break;
		default:
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}
	
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			break;
			/**
			 * 当滑动时动画操作
			 */
		case MotionEvent.ACTION_MOVE:
			down_X=interceptTouch_X;
			down_Y=interceptTouch_Y;
			move_X=event.getX();
			move_Y=event.getY();
			down_move_X=Math.abs(down_X-move_X);
			down_move_Y=Math.abs(down_Y-move_Y);
			/**
			 * 向下滑动
			 */
			if(move_Y>down_Y&&down_move_Y>moveLength&&getCanAnimation()){
				downAnimationConfig();
			}
			/**
			 * 向上滑动
			 */
			if(down_Y>move_Y&&down_move_Y>moveLength&&getCanAnimation()){
				upAnimationConfig();
			}
			/**
			 * 执行完上面动画处理后，停止执行动画
			 */
			setCanAnimation(false);
			break;
		case MotionEvent.ACTION_UP:
			break;
		default:
			break;
		}
		return true;
	}
	
	/**
	 * 是否进行动画处理
	 * @return true:处理
	 */
	private boolean getCanAnimation(){
		return isCanAnimation;
	}
	
	/**
	 * 获取当前视图显示类型
	 * @return
	 */
	private int getType(){
		return type;
	}
	
	private void setType(int type){
		this.type=type;
	}
	
	/**
	 * 设置是否进行动画处理
	 * @param canAnimation
	 */
	private void setCanAnimation(boolean canAnimation){
		this.isCanAnimation=canAnimation;
	}

	/**
	 * 向下滑动时的动画处理
	 */
	private void downAnimationConfig(){
		switch (getType()) {
		case HALF://当视图显示一半时
			half2None();
			break;
		case ALL://当视图全部显示时
			all2Half();
			break;

		default:
			break;
		}
	}
	
	/**
	 * 向上滑动时的动画处理
	 */
	private void upAnimationConfig(){
		switch (getType()) {
		case HALF://当视图显示一半时
			half2All();
			break;
		case ALL://当视图全部显示时
			/**
			 * 当视图已经完整显示，再往
			 * 上滑动也就没任何意义进行
			 * 动画处理。
			 */
			break;
		default:
			break;
		}
	}
	
	/**
	 * 向下滑动时是否交由onTouch事件处理
	 * @return true:由onTouch事件处理，不传递给子View
	 */
	private boolean isDounTransferOnTouch(){
		switch (type) {
		case NONE:
			break;
		case HALF:
			return true;
		case ALL:
			if(isCanHide){
				return true;
			}
			break;
		default:
			break;
		}
		return false;
	}
	
	/**
	 * 向上滑动时是否交由onTouch事件处理
	 * @return true:由onTouch事件处理，不传递给子View
	 */
	private boolean isUpTransferOnTouch(){
		switch (type) {
		case NONE:
			break;
		case HALF:
			return true;
		case ALL:
			break;
		default:
			break;
		}
		return false;
	}
	
	
	/**
	 * 当向下滑动时，当前视图显示一半，再往下滑动隐藏。
	 * type设置为NONE
	 */
	private void half2None(){
		float[] values=new float[]{move_height,getHeight()};
		startAnimation(values);
		setType(NONE);
	}
	
	/**
	 * 当向下滑动时，当前视图显示完整，再往下滑动视图显示一半。
	 * type设置为HALF
	 */
	private void all2Half(){
		float[] values=new float[]{0,move_height};
		startAnimation(values);
		setType(HALF);
	}
	
	/**
	 * 当向上滑动时，当前视图显示一半，再往上滑动，视图显示完整。
	 * type设置为ALL
	 */
	private void half2All(){
		float[] values=new float[]{move_height,0};
		startAnimation(values);
		setType(ALL);
	}
	
	/**
	 * 执行动画
	 * @param values
	 */
	private void startAnimation(float[] values){
		AnimatorSet as=new AnimatorSet();
		ObjectAnimator anim=ObjectAnimator.ofFloat(this, "translationY", values);
		anim.setDuration(500);
		as.playTogether(anim);
		as.start();
	}
	
	/**
	 * 隐藏浮层
	 */
	public void beforeInput(){
		switch (getType()) {
		case NONE:	
			break;
		case HALF:
			half2None();
			break;
		case ALL:
			break;

		default:
			break;
		}
	}
	
	/**
	 * 显示浮层一半
	 */
	public void none2Half(){
		float[] values=new float[]{getHeight(),move_height};
		startAnimation(values);
		setType(HALF);
	}
	
	/**
	 * 显示全部浮层
	 */
	public void none2All(){
		float[] values=new float[]{getHeight(),0};
		startAnimation(values);
		setType(HALF);
	}
	
	/**
	 * 是否进行动画滚动
	 * @param canHide
	 */
	public void setCanHide(boolean canHide){
		this.isCanHide=canHide;
	}

}
