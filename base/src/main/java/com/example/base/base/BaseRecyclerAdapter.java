package com.example.base.base;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author : sklyand
 * @email : zhengdengyao@51yryc.com
 * @time : 2019/7/18 15:47
 * @describe ：
 */

public abstract class BaseRecyclerAdapter<Data>
        extends RecyclerView.Adapter<BaseRecyclerAdapter.ViewHolder<Data>> {
    private final List<Data> mDataList;
    public static final int TYPE_HEADER = 0;  //说明是带有Header的
    public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
    public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的
    private int headCount = 0;
    private int footCount = 0;
    private List<LayoutContainer> mFooterViews;
    private List<LayoutContainer> mHeaderViews;
    private int position;

    /**
     * @param headerViewLayout 添加头部
     */
    public <T> void addHeaderView(@LayoutRes int headerViewLayout, final T data, final DataInjector<T> dataInjector) {
        if (mHeaderViews == null) {
            mHeaderViews = new ArrayList<>();
        }
        LayoutContainer<T> layoutContainer = new LayoutContainer<>();
        layoutContainer.setLayout(headerViewLayout);
        layoutContainer.setData(data);
        layoutContainer.setDataInjector(dataInjector);
        IViewHolderCreator iViewHolderCreator = new IViewHolderCreator<T>() {
            @Override
            public ViewHolder<T> create(View root, T data) {
                return new ViewHolder<T>(root) {
                    @Override
                    protected void onBind(T t) {
                        dataInjector.onInject(data, root);
                    }
                };
            }
        };
        layoutContainer.setIViewHolderCreator(iViewHolderCreator);
        mHeaderViews.add(0, layoutContainer);
        headCount = mHeaderViews.size();
        notifyItemInserted(0);
    }

    /**
     * @param headerViewLayout 添加头部
     */
    public void addHeaderView(@LayoutRes int headerViewLayout) {
        if (mHeaderViews == null) {
            mHeaderViews = new ArrayList<>();
        }
        LayoutContainer layoutContainer = new LayoutContainer();
        layoutContainer.setLayout(headerViewLayout);
        mHeaderViews.add(layoutContainer);
        headCount = mHeaderViews.size();
        notifyItemInserted(0);
    }

    /**
     * @param index headerview 在mHeaderViews位置
     */
    public void removeHeaderViewByIndex(int index) {
        if (mHeaderViews != null && index >= 0 && index < mHeaderViews.size()) {
            mHeaderViews.remove(index);
            headCount = mHeaderViews.size();
            notifyItemRemoved(index);
        }
    }

    /**
     * 移除第一个headerView
     */
    public void removeHeaderView() {
        if (mHeaderViews != null && mHeaderViews.size() > 0) {
            mHeaderViews.remove(0);
            headCount = mHeaderViews.size();
            notifyItemRemoved(0);
        }
    }

    /**
     * @param footerViewLayout 增加footview
     */
    public <T> void addFooterView(@LayoutRes int footerViewLayout, final T data, final DataInjector<T> dataInjector) {


        if (mFooterViews == null) {
            mFooterViews = new ArrayList<>();
        }
//        if (mFooterViews.contains(footerViewLayout)) {
//            //同一个view只能添加一次
//            return;
//        }
        LayoutContainer<T> layoutContainer = new LayoutContainer<>();
        layoutContainer.setLayout(footerViewLayout);
        layoutContainer.setData(data);
        layoutContainer.setDataInjector(dataInjector);
        IViewHolderCreator iViewHolderCreator = new IViewHolderCreator<T>() {
            @Override
            public ViewHolder<T> create(View root, T data) {
                return new ViewHolder<T>(root) {
                    @Override
                    protected void onBind(T t) {
                        dataInjector.onInject(data, root);
                    }
                };
            }
        };
        layoutContainer.setIViewHolderCreator(iViewHolderCreator);
        mFooterViews.add(layoutContainer);
        footCount = mFooterViews.size();
        notifyItemInserted(getItemCount() - 1);
    }

    /**
     * @param footerViewLayout 增加footview
     */
    public void addFooterView(@LayoutRes int footerViewLayout) {
        if (mFooterViews == null) {
            mFooterViews = new ArrayList<>();
        }
        LayoutContainer layoutContainer = new LayoutContainer();
        layoutContainer.setLayout(footerViewLayout);
//        if (mFooterViews.contains(footerViewLayout)) {
//            //同一个view只能添加一次
//            return;
//        }
        mFooterViews.add(layoutContainer);
        footCount = mFooterViews.size();
        notifyItemInserted(getItemCount() - 1);
    }

    /**
     * 移除最后一个footerView
     */
    public void removeFooterView() {
        if (mFooterViews != null && mFooterViews.size() > 0) {
            int index = mFooterViews.size() - 1;
            int oldSize = mFooterViews.size();
            mFooterViews.remove(index);
            footCount = mFooterViews.size();
            int notify = getItemCount() - oldSize + index + 1;//加1 是因为getItemCount() 这时已经减了1
            notifyItemRemoved(notify);
        }

    }

    /**
     * @param index FooterView 在mFooterViews位置
     */
    public void removeFooterViewByIndex(int index) {
        if (mFooterViews != null && index >= 0 && index < mFooterViews.size()) {
            int oldSize = mFooterViews.size();
            mFooterViews.remove(index);
            footCount = mFooterViews.size();
            int notify = getItemCount() - oldSize + index + 1;//加1 是因为getItemCount() 这时已经减了1
            notifyItemRemoved(notify);
        }
    }

    /**
     * @param data 数据
     * @param index headerview 在mHeaderViews位置
     * @param <T>
     *     刷新头部数据
     */
    public <T> void refreshHeaderView(final T data,int index){
        if (mHeaderViews!=null&&mHeaderViews.size()>index&&index>=0){
            mHeaderViews.get(index).setData(data);
            notifyItemChanged(index);
        }
    }

    /**
     * @param data 数据
     * @param index FooterView 在mFooterViews位置
     * @param <T>
     *     刷新尾部数据
     */
    public <T> void refreshFooterView(final T data,int index){
        if (mFooterViews!=null&&mFooterViews.size()>index&&index>=0){
            mFooterViews.get(index).setData(data);
            notifyItemChanged(index);
        }
    }
    /**
     * 构造函数模块
     */
    public BaseRecyclerAdapter() {
        this(new ArrayList<Data>());
    }

    public BaseRecyclerAdapter(List<Data> dataList) {
        this.mDataList = dataList;
    }


    /**
     * 复写默认的布局类型返回
     *
     * @param position 坐标
     * @return 类型，其实复写后返回的都是XML文件的ID
     */
    @Override
    public int getItemViewType(int position) {
        this.position = position;
        if (mHeaderViews != null && mHeaderViews.size() > 0 && position >= 0 && position < headCount) {
            return TYPE_HEADER;
        } else if (mFooterViews != null && mFooterViews.size() > 0 && position > getItemCount() - footCount - 1 && position < getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return getItemViewType(position - headCount, mDataList.get(position - headCount));
        }
    }


    /**
     * 得到布局的类型
     *
     * @param position 坐标
     * @param data     当前的数据
     * @return XML文件的ID，用于创建ViewHolder
     */
    @LayoutRes
    protected abstract int getItemViewType(int position, Data data);

    /**
     * 创建一个ViewHolder
     *
     * @param parent   RecyclerView
     * @param viewType 界面的类型,约定为XML布局的Id
     * @return ImageViewHolder
     */
    @Override
    public ViewHolder<Data> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // 得到LayoutInflater用于把XML初始化为View
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View root = null;
        if (viewType == TYPE_HEADER) {
            LayoutContainer layoutContainer =  mHeaderViews.get(position);
            int layout =layoutContainer.getLayout();
            IViewHolderCreator iViewHolderCreator = mHeaderViews.get(position).getIViewHolderCreator();
            root = inflater.inflate(layout, parent, false);
            if (iViewHolderCreator == null) {
                return new BaseHolder(root);
            } else {
                return iViewHolderCreator.create(root,layoutContainer.getData());
            }
        } else if (viewType == TYPE_FOOTER) {
            LayoutContainer layoutContainer = mFooterViews.get(mFooterViews.size() + position - getItemCount());
            int layout = layoutContainer.getLayout();
            IViewHolderCreator viewHolderCreator = layoutContainer.getIViewHolderCreator();
            root = inflater.inflate(layout, parent, false);
            if (viewHolderCreator == null) {
                return new BaseHolder(root);
            } else {
                return viewHolderCreator.create(root,layoutContainer.getData());
            }
        } else {
            // 把XML id为viewType的文件初始化为一个root View
            root = inflater.inflate(viewType, parent, false);
            // 通过子类必须实现的方法，得到一个ViewHolder
            ViewHolder<Data> holder = onCreateViewHolder(root, parent, viewType);
            // 设置View的Tag为ViewHolder，进行双向绑定
            // 设置事件点击
            // 进行界面注解绑定
            holder.unbinder = ButterKnife.bind(holder, root);
            // 绑定callback
            return holder;
        }
    }

    /**
     * 得到一个新的ViewHolder
     *
     * @param root     根布局
     * @param viewType 布局类型，其实就是XML的ID
     * @return ImageViewHolder
     */
    protected abstract ViewHolder<Data> onCreateViewHolder(View root, ViewGroup parent, int viewType);

    /**
     * 绑定数据到一个Holder上
     *
     * @param holder   ImageViewHolder
     * @param position 坐标
     */
    @Override
    public void onBindViewHolder(ViewHolder<Data> holder, int position) {
        // 得到需要绑定的数据
        if (mHeaderViews != null && mHeaderViews.size() > 0 && position >= 0 && position < headCount) {
            LayoutContainer layoutContainer =  mHeaderViews.get(position);
            //holder.bind(null);
            if (layoutContainer.getDataInjector()!=null){
                layoutContainer.getDataInjector().onInject(layoutContainer.getData(),holder.itemView);
            }

        } else if (mFooterViews != null && mFooterViews.size() > 0 && position >= getItemCount() - footCount && position < getItemCount()) {
            //holder.bind(null);
            LayoutContainer layoutContainer = mFooterViews.get(mFooterViews.size() + position - getItemCount());
            if (layoutContainer.getDataInjector()!=null){
                layoutContainer.getDataInjector().onInject(layoutContainer.getData(),holder.itemView);
            }
        } else {
            Data data = mDataList.get(position - headCount);
            holder.bind(data);
            // 触发Holder的绑定方法
        }
    }

    /**
     * 得到当前集合的数据量
     */
    @Override
    public int getItemCount() {
        return mDataList.size() + headCount + footCount;
    }

    /**
     * 返回整个集合
     *
     * @return List<Data>
     */
    public List<Data> getItems() {
        return mDataList;
    }

    /**
     * 插入一条数据并通知插入
     *
     * @param data Data
     */
    public void add(Data data) {
        mDataList.add(data);
        notifyItemInserted(getItemCount());
    }


    /**
     * 删除一条数据并通知刷新
     *
     * @param data Data
     */
    public void delete(Data data, int position) {

        mDataList.remove(data);
        notifyItemRemoved(position);
    }


    /**
     * 刷新一条数据并通知刷新
     *
     * @param data Data
     */
    public void update(Data data, int position) {
        mDataList.set(position - headCount, data);
        notifyItemChanged(position);
    }

    /**
     * 刷新一条数据并从最新插入
     *
     * @param data Data
     */
    public void updateFromEnd(Data data, int position) {
        mDataList.remove(position - headCount);
        mDataList.add(data);
        notifyItemChanged(mDataList.size() - 1);
    }

    /**
     * 从开始插入一堆数据，并通知这段集合更新
     *
     * @param dataList Data
     */
    public void addFromStar(List<Data> dataList) {

        if (dataList == null) {
            return;
        }

        if (dataList.size() > 0) {
            //int startPos = mDataList.size();
            mDataList.addAll(0, dataList);
            //notifyDataSetChanged();
            notifyItemRangeInserted(headCount, dataList.size());

        }
    }


    /**
     * 插入一堆数据，并通知这段集合更新
     *
     * @param dataList Data
     */
    public void add(List<Data> dataList) {
        if (dataList == null) {
            return;
        }
        if (dataList.size() > 0) {
            int startPos = getItemCount() + 1;
            mDataList.addAll(dataList);
            notifyItemRangeInserted(startPos, dataList.size());
        }
    }

    private static final String TAG = "BaseRecyclerAdapter";

    /**
     * 删除操作
     */
    public void clear() {

        mDataList.clear();
        notifyDataSetChanged();
    }

    /**
     * 替换为一个新的集合，其中包括了清空
     *
     * @param dataList 一个新的集合
     */
    public void replace(Collection<Data> dataList) {
        mDataList.clear();
        if (dataList == null || dataList.size() == 0)
            return;
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    /**
     * 自定义的ViewHolder
     *
     * @param <Data> 范型类型
     */
    public static abstract class ViewHolder<Data> extends RecyclerView.ViewHolder {
        private Unbinder unbinder;
        protected Data mData;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        /**
         * 用于绑定数据的触发
         *
         * @param data 绑定的数据
         */
        void bind(Data data) {
            this.mData = data;
            onBind(data);
        }

        /**
         * 当触发绑定数据的时候，的回掉；必须复写
         *
         * @param data 绑定的数据
         */
        protected abstract void onBind(Data data);

    }

    public static class RecyclerItem {
        public RecyclerItem() {
        }

        public RecyclerItem(int type, Object data) {
            this.type = type;
            this.data = data;
        }

        public int type;

        public Object data;
    }

    class BaseHolder extends ViewHolder {

        BaseHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Object o) {

        }
    }

    public interface DataInjector<T> {
        void onInject(T data, View root);
    }

    private interface IViewHolderCreator<T> {
        ViewHolder<T> create(View root,T data);
    }

    public static class BaseData {

    }

    private class LayoutContainer<T> {
        private int layout;
        private IViewHolderCreator iViewHolderCreator;
        private T data;
        private DataInjector dataInjector;

        public DataInjector getDataInjector() {
            return dataInjector;
        }

        public void setDataInjector(DataInjector dataInjector) {
            this.dataInjector = dataInjector;
        }

        public int getLayout() {
            return layout;
        }

        public void setLayout(int layout) {
            this.layout = layout;
        }


        public IViewHolderCreator getIViewHolderCreator() {
            return iViewHolderCreator;
        }

        public void setIViewHolderCreator(IViewHolderCreator iViewHolderCreator) {
            this.iViewHolderCreator = iViewHolderCreator;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }
    }
}
