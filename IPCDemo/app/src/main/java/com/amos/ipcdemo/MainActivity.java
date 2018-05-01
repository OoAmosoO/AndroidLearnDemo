package com.amos.ipcdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.amos.ipcdemo.bymessenger.MessengerClientActivity;

public class MainActivity extends AppCompatActivity implements MainAdapter.IMainItemListener {
    private static final String[] mDatas = {
            "IPC By Messenger"
    };
    private static final Class[] mActivities = {
            MessengerClientActivity.class
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rvList = (RecyclerView) findViewById(R.id.rv_list);
        rvList.setAdapter(new MainAdapter(mDatas, this));
        rvList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onMainItemClick(int position) {
        toActivity(position);
    }

    private void toActivity(int position) {
        Intent intent = new Intent(this, mActivities[position]);
        startActivity(intent);
    }
}
