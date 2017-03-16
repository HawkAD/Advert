package com.hawk.android.adsdk.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hawk.android.adsdk.ads.HKNativeAd;
import com.hawk.android.adsdk.ads.mediator.HawkAdRequest;
import com.hawk.android.adsdk.ads.mediator.HkNativeAdListener;
import com.hawk.android.adsdk.demo.view.NativeViewBuild;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangpeng on 2017/2/28.
 * <p/>
 * 这是一个例子:原生广告应用于滑动列表的例子
 */
public class NativeListActiviety extends Activity {
    private RecyclerView recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recyclerview = new RecyclerView(this);
        recyclerview.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        setContentView(recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        ListAdapter listAdapter = new ListAdapter();
        listAdapter.setList(getDates());

        recyclerview.setAdapter(listAdapter);
    }


    private List<ListDate> getDates(){

        List<ListDate> lists = new ArrayList<>();

        for(int i =0;i<10;i++){
            if(i==3||i==8){
                lists.add(new ListDate(ListDateType.ADVERTISEMENT,null));

            }else {

                lists.add(new ListDate(ListDateType.CONTENT,null));
            }
        }

        return lists;
    }


    public class ListAdapter extends RecyclerView.Adapter {
        private List<ListDate> list = new ArrayList();

        public List getList() {
            return list;
        }

        public void setList(List list) {
            this.list = list;
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            RecyclerView.ViewHolder viewHolder = null;

            switch (viewType) {
                case ListDateType.CONTENT:
                    TextView textView= new TextView(parent.getContext());
                    parent.addView(textView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,100));
                    viewHolder =  new ContentViewHold(textView);
                    break;
                case ListDateType.ADVERTISEMENT:
                    FrameLayout nativeAdContainer= new FrameLayout(parent.getContext());
                    parent.addView(nativeAdContainer,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                    viewHolder = new AdvertiseHolder(nativeAdContainer);

                    break;
            }


            return viewHolder;


        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(getItemViewType(position) == ListDateType.CONTENT){
                ((ContentViewHold)holder).textView.setText(position+"");

            }else if(getItemViewType(position) == ListDateType.ADVERTISEMENT){

                AdvertiseHolder advertiseHolder = (AdvertiseHolder)holder;
                advertiseHolder.loadAd();
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }


        @Override
        public int getItemViewType(int position) {

            Log.e("dsag","list ="+list);
            return list.get(position).type;
        }
    }

    public static class ContentViewHold extends RecyclerView.ViewHolder {
        public   TextView textView;
        public ContentViewHold(View itemView) {
            super(itemView);
            textView = (TextView) itemView;
        }

    }

    public static class AdvertiseHolder extends RecyclerView.ViewHolder{

        private HKNativeAd mHKNativeAd;
        private FrameLayout nativeAdContainer;//View Container
        private Activity activity;
        private View mAdView;
        public AdvertiseHolder(View itemView) {
            super(itemView);
            nativeAdContainer = (FrameLayout) itemView;
            activity = (Activity) itemView.getContext();
            initNativeAd();
        }

        private void initNativeAd() {
            //setp1 : create mHKNativeAd
            //The first parameter：Context
            //The second parameter: posid
            String testUnitId=activity.getString(R.string.native_ad_unitid);
            mHKNativeAd = new HKNativeAd(activity,testUnitId);
            //setp2 : set callback listener(INativeAdLoaderListener)
            mHKNativeAd.setNativeAdListener(new HkNativeAdListener() {
                @Override
                public void onNativeAdLoaded() {
                    //ad load  success ,you can do other something here;
                    showAd();
                    Log.e("监听测试","原生加载成功");
                    Toast.makeText(activity, "原生加载成功", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNativeAdFailed(int var1) {
                    Log.e("监听测试","原生加载失败");
                    Toast.makeText(activity, "原生加载失败,error code is:"+var1, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onAdClick() {
                    Log.e("监听测试","原生被点击");
                    Toast.makeText(activity, "原生被点击", Toast.LENGTH_SHORT).show();
                }

            });
        }

        public void loadAd(){
            mHKNativeAd.loadAd(new HawkAdRequest().addTestDevice("441eef257ec3736e03c516a86844e1c4"));
        }

        /**
         * if load nativeAd success,you can get and show nativeAd;
         */
        private void showAd(){
            if(mHKNativeAd != null){

                if(mHKNativeAd != null&&!mHKNativeAd.isLoaded()){
                    Toast.makeText(activity,
                            "no native ad loaded!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (mAdView != null) {
                    // remove old ad view
                    nativeAdContainer.removeAllViews();
                }
                //the mAdView is custom by publisher
                mAdView = NativeViewBuild.createAdView(activity,mHKNativeAd.getAd(),mHKNativeAd);
                if (mHKNativeAd != null) {
                    mHKNativeAd.unregisterView();
                    mHKNativeAd.registerViewForInteraction(mAdView);
                }
                //add the mAdView into the layout of view container.(the container should be prepared by youself)
                nativeAdContainer.addView(mAdView);
            }
        }


    }




    public static class ListDate<T> {
        private int type;

        private T content;

        public ListDate(int type, T content) {
            this.type = type;
            this.content = content;
        }
    }

    public @interface ListDateType {
        int CONTENT = 0;
        int ADVERTISEMENT = 1;
    }


}
