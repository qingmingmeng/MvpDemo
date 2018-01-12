package com.example.zj.mvpdemo.base;

import android.content.Context;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：带有header和Footer的RecyclerView的Adaptor
 * 项目作者：胡玉君
 * 创建日期：2016/10/19 14:07.
 * ----------------------------------------------------------------------------------------------------
 * 文件描述：
 *
 * 为RecyclerView添加header和footer时需注意：
 *
 * 1. 应使用以下方法填充布局，否则不能充满整个布局
 * View footView = LayoutInflater.from(activity).inflate(R.layout.add_bank_card, recyclerView, false);
 *
 *
 * 2. 必须在RecyclerView被初始化完毕后调用：
 * adaptor.addFootView(footView);
 * ----------------------------------------------------------------------------------------------------
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    protected List<T> datas;
    private int layoutId;
    protected WeakReference<Context> reference;
    /** 该方法即onCreateViewHolder，在实例化Adaptor时view已经被创建 */
    protected abstract RecyclerView.ViewHolder onCreateHolder(ViewGroup parent, View view, int viewType);

    /** 该方法即onBindViewHolder方法 */
    protected abstract void onBindHolder(RecyclerView.ViewHolder holder, int position);

    public BaseRecyclerAdapter(Context context, List<T> datas, int layoutId) {
        reference = new WeakReference<>(context);
        this.layoutId = layoutId;
        if (datas != null) {
            this.datas = datas;
        }else{
            this.datas = new ArrayList<>();
        }
    }

    @Override
    public int getItemCount() {
        return datas.size() + getHeadersCount() + getFootersCount();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    protected OnItemClickListener listener;
    protected OnLongItemClickListener longlistener;
    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }
    public void setOnLongItemClickListener(OnLongItemClickListener listener){
        this.longlistener = listener;
    }

    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onItemClick(v, (int)v.getTag());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if(longlistener != null){
            return longlistener.onLongItemClick(v, (int) v.getTag());
        }
        return false;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
    public interface OnLongItemClickListener{
        boolean onLongItemClick(View view, int position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(headerViews.get(viewType) != null){
            return new HeadAndFootHolder(headerViews.get(viewType));
        }else if (footViews.get(viewType) != null){
            return new HeadAndFootHolder(footViews.get(viewType));
        }else{
            Context context = reference.get();
            if(context == null) return null;
            View view = LayoutInflater.from(context).inflate(layoutId, parent, false);
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
            return onCreateHolder(parent, view, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setTag(position - getHeadersCount());
        if (isHeaderView(position)){
            return;
        }
        if (isFooterView(position)){
            return;
        }
        onBindHolder(holder, position - getHeadersCount());
    }

    @Override
    public int getItemViewType(int position){
        if (isHeaderView(position)){
            return headerViews.keyAt(position);
        } else if (isFooterView(position)) {
            return footViews.keyAt(position - getHeadersCount() - datas.size());
        }
        return super.getItemViewType(position - getHeadersCount());
    }

    /** 该方法只能在onDestroy()方法中调用，否则会出问题 */
    public void release(){
        if(reference != null){
            reference.clear();
        }
        reference = null;
        if(datas != null){
            datas.clear();
        }
        datas = null;
    }

    /**------------------------------------以下为head和foot--------------------------------------------*/

    private static final int RECYCLER_HEADER = 100000;
    private static final int RECYCLER_FOOTER = 200000;

    //HeaderView和footView的集合
    private SparseArrayCompat<View> headerViews = new SparseArrayCompat<>();
    private SparseArrayCompat<View> footViews = new SparseArrayCompat<>();

    /** 是否是HeaderView */
    private boolean isHeaderView(int position){
        return position < getHeadersCount();
    }
    /** Header的数量 */
    private int getHeadersCount() {
        return headerViews.size();
    }

    /** 是否是FootView */
    private boolean isFooterView(int position){
        return position >= getHeadersCount() + datas.size();
    }
    /** Foot的数量 */
    private int getFootersCount(){
        return footViews.size();
    }

    /** 添加header和footer */
    public void addHeaderView(View view){
        headerViews.put(headerViews.size() + RECYCLER_HEADER, view);
        notifyDataSetChanged();
    }
    public void addFootView(View view){
        footViews.put(footViews.size() + RECYCLER_FOOTER, view);
        notifyDataSetChanged();
    }

    /** 删除header和footer */
    public void remonveHeaderView(int position){
        if(isHeaderView(position)){
            headerViews.remove(position);
        }
    }
    public void removeFootView(int position){
        if(isFooterView(position)){
            footViews.remove(position);
        }
    }

    private class HeadAndFootHolder extends RecyclerView.ViewHolder{
        public HeadAndFootHolder(View itemView) {
            super(itemView);
        }
    }

    /** 重写该方法以使每个header和footer独立占据一行 */
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager){
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup(){
                @Override
                public int getSpanSize(int position){
                    int viewType = getItemViewType(position);
                    if (headerViews.get(viewType) != null)
                        return gridLayoutManager.getSpanCount();
                    else if (footViews.get(viewType) != null)
                        return gridLayoutManager.getSpanCount();
                    else if (spanSizeLookup != null)
                        return spanSizeLookup.getSpanSize(position);
                    return 1;
                }
            });
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        }
    }
}
