# FloatingLayerAnimtion
滑动的浮层，支持添加各种View


Skip to content
This repository  
Search
Pull requests
Issues
Gist
 @LinhaiGu
 Unwatch 1
  Unstar 2
  Fork 0 LinhaiGu/FloatingLayerAnimtion
 Code  Issues 0  Pull requests 0  Wiki  Pulse  Graphs  Settings
Branch: master Find file Copy pathFloatingLayerAnimtion/FloatingLayerAnimtion/src/com/example/floatinglayeranimtion/MainActivity.java
d726be2  on 20 Aug
 chen long new project
0 contributors
RawBlameHistory     141 lines (105 sloc)  3.72 KB
package com.example.floatinglayeranimtion;


public class MainActivity extends Activity implements OnClickListener {

	private Button btn_show;
	private Button btn_hide;
	private GridView gv_all;

	private TestAdapter testAdapter = new TestAdapter();


	// 覆盖层
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
		// 覆盖层
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

		// 显示浮层
		case R.id.btn_show:
			mFloatingLayerView.none2Half();
			break;

		// 隐藏浮层
		case R.id.btn_hide:
			mFloatingLayerView.beforeInput();
			break;

		}
	}

	

	/** 覆盖层中GridView滑动监听 */
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


	// =============测试======================
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
Status API Training Shop Blog About Pricing
© 2015 GitHub, Inc. Terms Privacy Security Contact Help
