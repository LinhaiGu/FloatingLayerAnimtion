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
	 * ��ͼ��ʾ���͡�
	 */
	private int type=NONE;
	/**
	 * ����ĸ߶ȡ�
	 */
	private int floating_height;
	/**
	 * ����Ŀ��
	 */
	private int floating_width;
	
	/**
	 * �����߶�
	 */
	private float move_height;
	
	/**
	 * �Ƿ����»���������onTouch�¼�����
	 */
	private boolean isCanHide=false;
	/**
	 * �Ƿ���ж���
	 */
	private boolean isCanAnimation=false;
	
	/**
	 * �������ش����¼�ʱ������㡣 
	 * ���£�
	 *  interceptTouch_X:����ʱ��X����㡣
	 *  interceptTouch_Y:����ʱ��Y����㡣
	 * ������ 
	 * interceptMove_X:����ʱ��X����㡣 
	 * interceptMove_Y:����ʱ��Y����㡣 
	 * ���룺
	 * interceptTouch_Move_X:�Ӱ��µ�����֮��ľ��루���򻬶���
	 * interceptTouch_Move_Y:�Ӱ��µ�����֮��ľ��루���򻬶���
	 * �������룺
	 * moveLength:���ݴ�ֵ�ж��Ƿ�����˻�����
	 */
	private float interceptTouch_X;
	private float interceptTouch_Y;
	private float interceptMove_X;
	private float interceptMove_Y;
	private float interceptTouch_Move_X;
	private float interceptTouch_Move_Y;
	private int moveLength=10;
	
	/**
	 * ���������¼�ʱ�������
	 * down_X:����ʱ��X����㡣
	 * down_Y:����ʱ��Y����㡣
	 * move_X:�ƶ�ʱ��X����㡣
	 * move_Y:�ƶ�ʱ��Y����㡣
	 * down_move_X:���򻬶��ľ��롣
	 * down_move_Y:���򻬶��ľ��롣
	 */
	private float down_X;
	private float down_Y;
	private float move_X;
	private float move_Y;
	private float down_move_X;
	private float down_move_Y;

	/**
	 * �������ָ�����ʾ���� 
	 * 0������ʾ 1����ʾһ�� 2��ȫ����ʾ
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
			 * ÿ�λ����ľ����ǵ�ǰView��ȵ�����֮һ��
			 */
			move_height=floating_height/3;
		}
		super.onWindowFocusChanged(hasWindowFocus);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		/**
		 * ������ʱ��ȡx,y������㡣
		 */
		case MotionEvent.ACTION_DOWN:
			interceptTouch_X = ev.getX();
			interceptTouch_Y = ev.getY();
			isCanAnimation=true;
			break;
			/**
			 * ������ʱ�������£�
			 * 1����ȡ��������
			 * 2���ж����ϻ����������»���
			 * 3�����ϻ���ʱ
			 * 4�����»���ʱ���жϵ�ǰ��ʾ��ʽ��
			 * ��1������ʾһ��ʱ������onTouch�¼�����
			 * ��2����ȫ����ʾʱ���Ƿ����»������ɵ�ǰView����View����
			 * �Ƿ���onTouch�¼�����
			 */
		case MotionEvent.ACTION_MOVE:
			interceptMove_X = ev.getX();
			interceptMove_Y = ev.getY();
			interceptTouch_Move_X = Math
					.abs(interceptTouch_X - interceptMove_X);
			interceptTouch_Move_Y = Math
					.abs(interceptTouch_Y - interceptMove_Y);
			/**
			 * ���»���
			 */
			if(interceptMove_Y>interceptTouch_Y&&interceptTouch_Move_Y>moveLength&&interceptTouch_Move_Y>interceptTouch_Move_X){
				return isDounTransferOnTouch();
			}
			/**
			 * ���ϻ���
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
			 * ������ʱ��������
			 */
		case MotionEvent.ACTION_MOVE:
			down_X=interceptTouch_X;
			down_Y=interceptTouch_Y;
			move_X=event.getX();
			move_Y=event.getY();
			down_move_X=Math.abs(down_X-move_X);
			down_move_Y=Math.abs(down_Y-move_Y);
			/**
			 * ���»���
			 */
			if(move_Y>down_Y&&down_move_Y>moveLength&&getCanAnimation()){
				downAnimationConfig();
			}
			/**
			 * ���ϻ���
			 */
			if(down_Y>move_Y&&down_move_Y>moveLength&&getCanAnimation()){
				upAnimationConfig();
			}
			/**
			 * ִ�������涯�������ִֹͣ�ж���
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
	 * �Ƿ���ж�������
	 * @return true:����
	 */
	private boolean getCanAnimation(){
		return isCanAnimation;
	}
	
	/**
	 * ��ȡ��ǰ��ͼ��ʾ����
	 * @return
	 */
	private int getType(){
		return type;
	}
	
	private void setType(int type){
		this.type=type;
	}
	
	/**
	 * �����Ƿ���ж�������
	 * @param canAnimation
	 */
	private void setCanAnimation(boolean canAnimation){
		this.isCanAnimation=canAnimation;
	}

	/**
	 * ���»���ʱ�Ķ�������
	 */
	private void downAnimationConfig(){
		switch (getType()) {
		case HALF://����ͼ��ʾһ��ʱ
			half2None();
			break;
		case ALL://����ͼȫ����ʾʱ
			all2Half();
			break;

		default:
			break;
		}
	}
	
	/**
	 * ���ϻ���ʱ�Ķ�������
	 */
	private void upAnimationConfig(){
		switch (getType()) {
		case HALF://����ͼ��ʾһ��ʱ
			half2All();
			break;
		case ALL://����ͼȫ����ʾʱ
			/**
			 * ����ͼ�Ѿ�������ʾ������
			 * �ϻ���Ҳ��û�κ��������
			 * ��������
			 */
			break;
		default:
			break;
		}
	}
	
	/**
	 * ���»���ʱ�Ƿ���onTouch�¼�����
	 * @return true:��onTouch�¼����������ݸ���View
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
	 * ���ϻ���ʱ�Ƿ���onTouch�¼�����
	 * @return true:��onTouch�¼����������ݸ���View
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
	 * �����»���ʱ����ǰ��ͼ��ʾһ�룬�����»������ء�
	 * type����ΪNONE
	 */
	private void half2None(){
		float[] values=new float[]{move_height,getHeight()};
		startAnimation(values);
		setType(NONE);
	}
	
	/**
	 * �����»���ʱ����ǰ��ͼ��ʾ�����������»�����ͼ��ʾһ�롣
	 * type����ΪHALF
	 */
	private void all2Half(){
		float[] values=new float[]{0,move_height};
		startAnimation(values);
		setType(HALF);
	}
	
	/**
	 * �����ϻ���ʱ����ǰ��ͼ��ʾһ�룬�����ϻ�������ͼ��ʾ������
	 * type����ΪALL
	 */
	private void half2All(){
		float[] values=new float[]{move_height,0};
		startAnimation(values);
		setType(ALL);
	}
	
	/**
	 * ִ�ж���
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
	 * ���ظ���
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
	 * ��ʾ����һ��
	 */
	public void none2Half(){
		float[] values=new float[]{getHeight(),move_height};
		startAnimation(values);
		setType(HALF);
	}
	
	/**
	 * ��ʾȫ������
	 */
	public void none2All(){
		float[] values=new float[]{getHeight(),0};
		startAnimation(values);
		setType(HALF);
	}
	
	/**
	 * �Ƿ���ж�������
	 * @param canHide
	 */
	public void setCanHide(boolean canHide){
		this.isCanHide=canHide;
	}

}
