package com.xtree.live.ui.main.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.JsonObject;
import com.xtree.base.net.HttpCallBack;
import com.xtree.base.net.live.X9LiveInfo;
import com.xtree.base.utils.CfLog;
import com.xtree.live.LiveConfig;
import com.xtree.live.chat.RequestUtils;
import com.xtree.live.data.LiveRepository;
import com.xtree.live.data.source.httpnew.LiveRep;
import com.xtree.live.data.source.request.AnchorSortRequest;
import com.xtree.live.data.source.request.ChatRoomListRequest;
import com.xtree.live.data.source.request.LiveTokenRequest;
import com.xtree.live.data.source.request.SearchAssistantRequest;
import com.xtree.live.data.source.request.SendToAssistantRequest;
import com.xtree.live.data.source.response.AnchorSortResponse;
import com.xtree.live.data.source.response.ChatRoomResponse;
import com.xtree.live.data.source.response.LiveTokenResponse;
import com.xtree.live.data.source.response.SearchAssistantResponse;
import com.xtree.live.ui.main.model.chat.LiveThiredLoginRequest;

import java.lang.ref.WeakReference;

import me.xtree.mvvmhabit.base.BaseViewModel;
import me.xtree.mvvmhabit.utils.RxUtils;

public class AttentionListModel extends BaseViewModel<LiveRepository> {
    public  interface ICallBack{
        void  callback();
    }
    private WeakReference<FragmentActivity> mActivity = null;
    public AttentionListModel(@NonNull Application application) {
        super(application);
    }
    public AttentionListModel(@NonNull Application application, LiveRepository model) {
        super(application, model);
    }
    public ICallBack callBack ;

    public MutableLiveData<AnchorSortResponse> anchorSortResponseMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<ChatRoomResponse> chatRoomResponseMutableLiveData = new MutableLiveData<>();//聊天房列表
    public MutableLiveData<SearchAssistantResponse> searchAssistantResponseMutableLiveData = new MutableLiveData<>();//搜索主播助手


    public void setCallBack(ICallBack callBack) {
        CfLog.e("setCallBack --- >" +callBack.toString());
        this.callBack = callBack;
    }


    public void initData(FragmentActivity mActivity) {
        setActivity(mActivity);

        if (X9LiveInfo.INSTANCE.getToken().isEmpty()) {
            /*model.getLiveToken(new LiveTokenRequest())
                    .compose(RxUtils.schedulersTransformer())
                    .compose(RxUtils.exceptionTransformer())
                    .subscribe(new HttpCallBack<LiveTokenResponse>() {
                        @Override
                        public void onResult(LiveTokenResponse data) {
                            if (data.getAppApi() != null && !data.getAppApi().isEmpty()) {
                                model.setLive(data);
                                initData();
                                if (callBack != null){
                                    CfLog.e("initData ------------->allBack != null");
                                    callBack.callback();
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable t) {

                            super.onError(t);
                        }
                    });*/

//            JsonObject json = new JsonObject();
//            json.addProperty("fingerprint", X9LiveInfo.INSTANCE.getOaid());
//            json.addProperty("device_type", "android");
//            json.addProperty("channel_code", "xc");
//            json.addProperty("user_id", LiveConfig.getUserId());

            LiveThiredLoginRequest request = new LiveThiredLoginRequest(
                    X9LiveInfo.INSTANCE.getOaid(),"android",LiveConfig.getUserId()
            );
            LiveRepository.getInstance().getXLiveToken(request)
                    .compose(RxUtils.schedulersTransformer())
                    .compose(RxUtils.exceptionTransformer())
                    .subscribe(new HttpCallBack<LiveTokenResponse>() {
                        @Override
                        public void onResult(LiveTokenResponse data) {
                            if (data.getAppApi() != null && !data.getAppApi().isEmpty()) {
                                model.setLive(data);
                                initData();
                                if (callBack != null){
                                    CfLog.e("initData ------------->allBack != null");
                                    callBack.callback();
                                }
                            }
                        }

                        @Override
                        public void onError(Throwable t) {
                            super.onError(t);
                        }
                    });

        } else {
            initData();
        }
    }

    public void getAnchorSort(){
        LiveRepository.getInstance().getAnchorSort(new AnchorSortRequest())
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new HttpCallBack<AnchorSortResponse>() {
                    @Override
                    public void onResult(AnchorSortResponse data) {

                        if (data !=null){
                            CfLog.e("getAnchorSort ----------------> ");
                            anchorSortResponseMutableLiveData.setValue(data);
                        }
                    }

                });
    }
    public void  getChatRoomList(){
        LiveRepository.getInstance().getChatRoomList(new ChatRoomListRequest())
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new HttpCallBack<ChatRoomResponse>() {
                    @Override
                    public void onResult(ChatRoomResponse data) {

                        if (data !=null){
                            CfLog.e("getChatRoomList -------------chatRoomResponseMutableLiveData---> " + data.data.toString());
                            CfLog.e("getChatRoomList ------------- data.data.size()---> " + data.data.size());
                            chatRoomResponseMutableLiveData.setValue(data);
                        }
                    }

                });
    }

    /***
     * 搜索主播助手
     * @param response
     */
    public  void searchAssistant(final SearchAssistantRequest response){
        LiveRepository.getInstance().getSearchAssistant(response)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new HttpCallBack<SearchAssistantResponse>() {
                    @Override
                    public void onResult(SearchAssistantResponse data) {

                        if (data !=null){
                            searchAssistantResponseMutableLiveData.setValue(data);
                        }
                    }

                });
    }

    public void  sendAssistant(SendToAssistantRequest request){
        LiveRepository.getInstance().sendToAssistant(request)
                .compose(RxUtils.schedulersTransformer())
                .compose(RxUtils.exceptionTransformer())
                .subscribe(new HttpCallBack<SearchAssistantResponse>() {
                    @Override
                    public void onResult(SearchAssistantResponse data) {

                        if (data !=null){
                            searchAssistantResponseMutableLiveData.setValue(data);
                        }
                    }

                });
    }


    public void setActivity(FragmentActivity mActivity) {
        this.mActivity = new WeakReference<>(mActivity);
    }
    private void initData() {
        /*itemType.setValue(typeList);
        datas.setValue(bindModels);*/
       /* getAnchorSort();*/
    }
}
