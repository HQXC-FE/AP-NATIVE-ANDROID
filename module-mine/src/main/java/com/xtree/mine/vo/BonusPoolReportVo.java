package com.xtree.mine.vo;

import java.util.List;

public class BonusPoolReportVo {
	private int p;
	private String amount;
	private String total_num;
	private int total_page;
	private List<BonusPoolReportItemVo> list;
	private int pn;

	public int getP(){
		return p;
	}

	public String getAmount(){
		return amount;
	}

	public String getTotalNum(){
		return total_num;
	}

	public int getTotalPage(){
		return total_page;
	}

	public List<BonusPoolReportItemVo> getList(){
		return list;
	}

	public int getPn(){
		return pn;
	}
}