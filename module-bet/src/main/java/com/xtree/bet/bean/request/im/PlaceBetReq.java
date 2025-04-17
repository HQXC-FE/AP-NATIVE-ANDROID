package com.xtree.bet.bean.request.im;

import com.xtree.bet.bean.response.im.WagerSelectionInfo;

import java.util.List;

public class PlaceBetReq {
    public int WagerType; // 1=Single, 2=Combo
    public String CustomerIP;
    public String ServerIP;
    public String CustomerMACAddress;
    public String UserAgent;
    public boolean IsComboAcceptAnyOdds;
    public List<WagerSelectionInfo> WagerSelectionInfos;
    public List<ComboSelection> ComboSelections;
    public String Token;
    public String MemberCode;
    public int SourceWallet; // 1=主账户, 2=免费投注
    public String TimeStamp;
    public String LanguageCode;
}
