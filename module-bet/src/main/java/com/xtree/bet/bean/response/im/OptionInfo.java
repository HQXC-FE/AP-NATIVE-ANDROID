package com.xtree.bet.bean.response.im;

import android.os.Parcel;
import android.os.Parcelable;

import com.xtree.base.vo.BaseBean;

/**
 * 玩法选项
 */
public class OptionInfo implements BaseBean {

    /**
     * 选项全称，投注框一般用全称展示
     */
    private String na;

    /**
     * 选项简称(全名or简名，订单相关为全名，否则为简名)， 赔率列表一般都用简称展示
     */
    private String nm;

    /**
     * 选项类型，主、客、大、小等，投注时需要提交该字段作为选中的选项参数
     */
    private int ty;

    /**
     * 欧盘赔率，目前我们只提供欧洲盘赔率，投注是请提交该字段赔率值作为选项赔率，赔率小于0代表锁盘
     */
    private double od;

    /**
     * 赔率
     */
    private double bod;

    /**
     * 赔率类型
     */
    private int odt;

    /**
     * 选项结算结果，仅虚拟体育展示
     */
    private int otcm;

    /**
     * 是否选中
     */
    private boolean isSelected;

    /**
     * line值，带线玩法的线，例如大小球2.5线，部分玩法展示可用该字段进行分组展示
     */
    private String li;

    private int change;

    public OptionInfo() {
    }

    protected OptionInfo(Parcel in) {
        this.na = in.readString();
        this.nm = in.readString();
        this.ty = in.readInt();
        this.od = in.readDouble();
        this.bod = in.readDouble();
        this.odt = in.readInt();
        this.otcm = in.readInt();
        this.isSelected = in.readByte() != 0;
        this.li = in.readString();
        this.change = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.na);
        dest.writeString(this.nm);
        dest.writeInt(this.ty);
        dest.writeDouble(this.od);
        dest.writeDouble(this.bod);
        dest.writeInt(this.odt);
        dest.writeInt(this.otcm);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
        dest.writeString(this.li);
        dest.writeInt(this.change);
    }

    public void readFromParcel(Parcel source) {
        this.na = source.readString();
        this.nm = source.readString();
        this.ty = source.readInt();
        this.od = source.readDouble();
        this.bod = source.readDouble();
        this.odt = source.readInt();
        this.otcm = source.readInt();
        this.isSelected = source.readByte() != 0;
        this.li = source.readString();
        this.change = source.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OptionInfo> CREATOR = new Creator<OptionInfo>() {
        @Override
        public OptionInfo createFromParcel(Parcel source) {
            return new OptionInfo(source);
        }

        @Override
        public OptionInfo[] newArray(int size) {
            return new OptionInfo[size];
        }
    };

    // Getter and Setter methods
    public String getNa() {
        return na;
    }

    public void setNa(String na) {
        this.na = na;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public int getTy() {
        return ty;
    }

    public void setTy(int ty) {
        this.ty = ty;
    }

    public double getOd() {
        return od;
    }

    public void setOd(double od) {
        this.od = od;
    }

    public double getBod() {
        return bod;
    }

    public void setBod(double bod) {
        this.bod = bod;
    }

    public int getOdt() {
        return odt;
    }

    public void setOdt(int odt) {
        this.odt = odt;
    }

    public int getOtcm() {
        return otcm;
    }

    public void setOtcm(int otcm) {
        this.otcm = otcm;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getLi() {
        return li;
    }

    public void setLi(String li) {
        this.li = li;
    }

    public int getChange() {
        return change;
    }

    public void setChange(int change) {
        this.change = change;
    }
}
