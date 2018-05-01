package com.amos.ipcdemo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * author: amos
 * date: 18/5/1 17:58
 */

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    public interface IMainItemListener {
        public void onMainItemClick(int position);
    }
    private String[] mDatas;
    private IMainItemListener mListener;

    public MainAdapter(String[] pDatas, IMainItemListener pListener) {
        mDatas = pDatas;
        mListener = pListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvText.setText(mDatas[position]);
        holder.tvText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onMainItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (mDatas == null ? 0 : mDatas.length);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvText;
        public ViewHolder(View itemView) {
            super(itemView);
            tvText = (TextView) itemView.findViewById(R.id.tv_text);
        }
    }
}
