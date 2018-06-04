package smartrefreshlayout.sing.smartrefreshlayout;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sing.SmartRefreshLayout;
import sing.refreshlayout.api.RefreshLayout;
import sing.refreshlayout.listener.OnLoadMoreListener;
import sing.refreshlayout.listener.OnRefreshListener;

public class MainActivity extends AppCompatActivity {

    private SmartRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private List<String> list = new ArrayList<>();
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refreshLayout = findViewById(R.id.refreshLayout);
        recyclerView = findViewById(R.id.recycler_view);

        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        refreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能（非必须）
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                // 下拉刷新监听
                    list.clear();
                    getData();
                    adapter.notifyDataSetChanged();

                    refreshLayout.finishRefresh();
                    refreshLayout.setNoMoreData(false);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                // 上拉刷新监听
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (adapter.getItemCount() > 30) {
                            Toast.makeText(getApplication(), "数据全部加载完毕", Toast.LENGTH_SHORT).show();
                            refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                        } else {
                            getData();
                            adapter.notifyDataSetChanged();

                            refreshLayout.finishLoadMore();
                        }
                    }
                }, 500);
            }
        });

        getData();
        adapter.setData(list);
        //触发自动刷新
        refreshLayout.autoRefresh();
    }

    private void getData() {
        for (int i = 0; i < 10; i++) {
            list.add("123");
        }
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private List<String> list;

        public void setData(List<String> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.tv.setText(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list == null ? 0 : list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tv;

            public ViewHolder(View itemView) {
                super(itemView);
                tv = itemView.findViewById(R.id.tv);
            }
        }
    }
}
