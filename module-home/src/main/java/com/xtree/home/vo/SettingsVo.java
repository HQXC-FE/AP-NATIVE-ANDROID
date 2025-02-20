package com.xtree.home.vo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Arrays;

public class SettingsVo implements Parcelable {

    public String public_key; // "-----BEGIN PUBLIC KEY-----MIGf***AQAB-----END PUBLIC KEY-----"
    public String barrage_api_url; // "wss://www.shenbofilm.com/wss/"
    public String promption_code; // "kygprka" 回传注册接口的code
    public String default_promption_code; // "kygprb"
    public String customer_service_url; // "https://www.mjooh9i.com/service/chatlink.html"
    public String[] customer_service_urls; // ["https://www.mjooh9i.com/service/chatlink.html", "https://www.5trfg4g.com/service/chatlink.html"]
    public String[] x9_customer_service_url; // ["https://www.hikefu01.net/im/index?appid=0NYEsh"]
    public long ws_check_interval; // "30"若为30, 则代表需在收到后端上一次type为open讯息30秒后再发一次确认讯息给后端
    public long ws_retry_number; // "3"//每次最大重连尝试次数
    public long ws_retry_waiting_time; // "300"超过最大尝试次数后的等待时间(秒), 需度过等待时间才能在发起连线尝试
    public long ws_expire_time; // "90"若检测到距离上次收到后端确认成功讯息(type为open)超过90秒, 這中間都沒有訊息傳入或是成功訊息, 则需主动断开连线然后重新连接。
    // 测速扣除百分比
    public long app_response_speed_calculation;
    //推荐测速上限设置
    public int app_response_speed_max = -1;
    //新客服列表
    public String []hichat_url_suffix ;//// ["\/im\/chat?platformCode=THRB&channelLink=OKGV5vPNGc", "\/im\/chat?platformCode=THRB&channelLink=OKGV5vPNGc"]
    public String[] op_hichat_url_suffix; // ["\/im\/chat?platformCode=THRB&channelLink=OKGV5vPNGc", "\/im\/chat?platformCode=THRB&channelLink=OKGV5vPNGc"]
    // Constructor for Parcelable
    protected SettingsVo(Parcel in) {
        public_key = in.readString();
        barrage_api_url = in.readString();
        promption_code = in.readString();
        default_promption_code = in.readString();
        customer_service_url = in.readString();
        customer_service_urls = in.createStringArray();
        x9_customer_service_url = in.createStringArray();

        ws_check_interval = in.readLong();
        ws_retry_number = in.readLong();
        ws_retry_waiting_time = in.readLong();
        ws_expire_time = in.readLong();
        app_response_speed_calculation = in.readLong();
        app_response_speed_max = in.readInt();
        hichat_url_suffix = in.createStringArray();
        op_hichat_url_suffix = in.createStringArray();
    }

    public static final Creator<SettingsVo> CREATOR = new Creator<SettingsVo>() {
        @Override
        public SettingsVo createFromParcel(Parcel in) {
            return new SettingsVo(in);
        }

        @Override
        public SettingsVo[] newArray(int size) {
            return new SettingsVo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {
        parcel.writeString(public_key);
        parcel.writeString(barrage_api_url);
        parcel.writeString(promption_code);
        parcel.writeString(default_promption_code);
        parcel.writeString(customer_service_url);
        parcel.writeStringArray(customer_service_urls);
        parcel.writeStringArray(x9_customer_service_url);
        parcel.writeStringArray(hichat_url_suffix);
        parcel.writeLong(ws_check_interval);
        parcel.writeLong(ws_retry_number);
        parcel.writeLong(ws_retry_waiting_time);
        parcel.writeLong(ws_expire_time);
        parcel.writeLong(app_response_speed_calculation);
        parcel.writeInt(app_response_speed_max);
        parcel.writeStringArray(hichat_url_suffix);
        parcel.writeStringArray(op_hichat_url_suffix);
    }

    @Override
    public String toString() {
        return "SettingsVo{" +
                "public_key='" + public_key + '\'' +
                ", barrage_api_url='" + barrage_api_url + '\'' +
                ", promption_code='" + promption_code + '\'' +
                ", default_promption_code='" + default_promption_code + '\'' +
                ", customer_service_url='" + customer_service_url + '\'' +
                ", customer_service_urls=" + Arrays.toString(customer_service_urls) +
                ", x9_customer_service_url=" + Arrays.toString(x9_customer_service_url) +
                ", hichat_url_suffix=" + Arrays.toString(hichat_url_suffix) +
                ", ws_check_interval=" + ws_check_interval +
                ", ws_retry_number=" + ws_retry_number +
                ", ws_retry_waiting_time=" + ws_retry_waiting_time +
                ", ws_expire_time=" + ws_expire_time +
                ", app_response_speed_calculation=" + app_response_speed_calculation +
                ", app_response_speed_max=" + app_response_speed_max +
                ", hichat_url_suffix=" + Arrays.toString(hichat_url_suffix) +
                '}';
    }
}
