package com.gameplayer.applydemo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gameplayer.applydemo.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	private static final String TAG = "chen";
	private ProgressBar pgLoading;
	private RecyclerView rvDemos;
	private List<DemoResponse> responseList;
	private DemoAdapter demoAdapter;
	private int centerPosition = 2;
	private int height;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initView();
		initData();
	}

	private void initView() {
		rvDemos = (RecyclerView) findViewById(R.id.rv_demos);
		pgLoading = findViewById(R.id.pb_loading);
		final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		rvDemos.setLayoutManager(layoutManager);
		rvDemos.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				int firstItemPosition = layoutManager.findFirstVisibleItemPosition();
				if (centerPosition - 2 != firstItemPosition) {
					centerPosition = firstItemPosition + 2;
				}
				if (dy > 0) {
					View shrinkView = recyclerView.getChildAt(2);
					View expandView = recyclerView.getChildAt(3);
					shrinkAndExpand(shrinkView, expandView, dy);
				} else if (dy < 0) {
					View shrinkView = recyclerView.getChildAt(3);
					View expandView = recyclerView.getChildAt(2);
					shrinkAndExpand(shrinkView, expandView, Math.abs(dy));
				}

			}
		});
	}

	private void shrinkAndExpand(View shrinkView, View expandView, int dy) {
		ViewGroup.LayoutParams shrinkLayoutParams = shrinkView.getLayoutParams();
		shrinkLayoutParams.height = shrinkLayoutParams.height - dy;
		if (!valiableHeight(shrinkLayoutParams.height)) {
			shrinkLayoutParams.height = height;
		}
		shrinkView.setLayoutParams(shrinkLayoutParams);
		ViewGroup.LayoutParams expandLayoutParams = expandView.getLayoutParams();
		expandLayoutParams.height = expandLayoutParams.height + dy;
		if (!valiableHeight(expandLayoutParams.height)) {
			expandLayoutParams.height = height * 2;
		}
		expandView.setLayoutParams(expandLayoutParams);
	}

	private boolean valiableHeight(int mheight) {
		if (mheight >= height && mheight < height * 2) {
			return true;
		} else {
			return false;
		}
	}

	private void initData() {
		height = DensityUtils.getWindowHeight(MainActivity.this) / 6;
		responseList = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			DemoResponse demoResponse = new DemoResponse();
			demoResponse.setContent("这是内容这是内容这是内容" + i);
			responseList.add(demoResponse);
		}
		demoAdapter = new DemoAdapter(responseList, this);
		rvDemos.setAdapter(demoAdapter);
	}

	public class DemoAdapter extends RecyclerView.Adapter<DemoAdapter.ViewHolder> {

		public List<DemoResponse> responses;
		private Context context;


		public DemoAdapter(List<DemoResponse> responses, Context context) {
			if (responses == null) {
				this.responses = new ArrayList<DemoResponse>();
			} else {
				this.responses = responses;
			}
			this.context = context;
		}

		@Override
		public DemoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_demo, parent, false));
		}

		@Override
		public void onBindViewHolder(ViewHolder holder, int position) {
			ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
			if (holder.getAdapterPosition() == centerPosition) {
				Log.i(TAG, "onBindViewHolder: " + centerPosition);
				layoutParams.height = height * 2;
			} else {
				layoutParams.height = height;
			}
			holder.itemView.setLayoutParams(layoutParams);
			holder.ivPic.setImageResource(R.drawable.ad1);
			DemoResponse demoResponse = responses.get(position);
			holder.tvContent.setText(demoResponse.getContent());
		}

		@Override
		public int getItemCount() {
			return responses.size();
		}

		class ViewHolder extends RecyclerView.ViewHolder {
			public ImageView ivPic;
			public TextView tvContent;

			public ViewHolder(View itemView) {
				super(itemView);
				ivPic = itemView.findViewById(R.id.iv_pic);
				tvContent = itemView.findViewById(R.id.tv_content);
			}
		}
	}
}
