package com.example.floatinglayeranimtion;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

public class MainActivity extends Activity implements OnClickListener {

	private Button btn_show;
	private Button btn_hide;
	private GridView gv_all;

	private TestAdapter testAdapter = new TestAdapter();


	// ∏≤∏«≤„
	private FloatingLayerView mFloatingLayerView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		addListener();
	}

	private void initView() {
		btn_show = (Button) findViewById(R.id.btn_show);
		btn_hide = (Button) findViewById(R.id.btn_hide);
		// ∏≤∏«≤„
		mFloatingLayerView = (FloatingLayerView) findViewById(R.id.activity_shine_ll_cover);
		gv_all = (GridView) findViewById(R.id.activity_shine_gv_all);

		gv_all.setAdapter(testAdapter);

	}

	private void addListener() {
		btn_show.setOnClickListener(this);
		btn_hide.setOnClickListener(this);
		gv_all.setOnScrollListener(scrollListener);

	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		// œ‘ æ∏°≤„
		case R.id.btn_show:
			mFloatingLayerView.none2Half();
			break;

		// “˛≤ÿ∏°≤„
		case R.id.btn_hide:
			mFloatingLayerView.beforeInput();
			break;

		}
	}

	

	/** ∏≤∏«≤„÷–GridViewª¨∂Øº‡Ã˝ */
	private OnScrollListener scrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			if (firstVisibleItem == 0) {
				mFloatingLayerView.setCanHide(true);
			} else {
				mFloatingLayerView.setCanHide(false);
			}

		}

	};


	// =============≤‚ ‘======================
	private int[] images = new int[] { R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher, R.drawable.ic_launcher, R.drawable.ic_launcher,
			R.drawable.ic_launcher };

	class TestAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return images.length;
		}

		@Override
		public Object getItem(int position) {
			return images[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View view = LayoutInflater.from(MainActivity.this).inflate(
					R.layout.image, null);
			
			ImageView imagView = (ImageView) view.findViewById(R.id.iv_show);
			imagView.setBackgroundResource(images[position]);
			return view;
		}

	}

}
