package com.xtree.lottery.data.source.response;

import androidx.databinding.ObservableField;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by KAKA on 2024/5/20.
 * Describe:
 */
public class HandicapResponse {

    //{"status":10000,"message":"success","data":[{"category":"整合","groups":[{"name":"万位","codes":[{"menuid":"406701","methodid":"6367456","name":"大","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"},{"menuid":"406701","methodid":"6367456","name":"小","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"},{"menuid":"406701","methodid":"6367456","name":"单","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"},{"menuid":"406701","methodid":"6367456","name":"双","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"},{"menuid":"2376","methodid":"2394","name":"0","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"1","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"2","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"3","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"4","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"5","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"6","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"7","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"8","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"9","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"}]},{"name":"千位","codes":[{"menuid":"406702","methodid":"6367457","name":"大","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"},{"menuid":"406702","methodid":"6367457","name":"小","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"},{"menuid":"406702","methodid":"6367457","name":"单","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"},{"menuid":"406702","methodid":"6367457","name":"双","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"},{"menuid":"2376","methodid":"2394","name":"0","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"1","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"2","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"3","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"4","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"5","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"6","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"7","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"8","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"9","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"}]},{"name":"百位","codes":[{"menuid":"406703","methodid":"6367458","name":"大","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"},{"menuid":"406703","methodid":"6367458","name":"小","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"},{"menuid":"406703","methodid":"6367458","name":"单","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"},{"menuid":"406703","methodid":"6367458","name":"双","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"},{"menuid":"2376","methodid":"2394","name":"0","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"1","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"2","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"3","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"4","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"5","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"6","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"7","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"8","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"9","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"}]},{"name":"十位","codes":[{"menuid":"406704","methodid":"6367459","name":"大","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"},{"menuid":"406704","methodid":"6367459","name":"小","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"},{"menuid":"406704","methodid":"6367459","name":"单","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"},{"menuid":"406704","methodid":"6367459","name":"双","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"},{"menuid":"2376","methodid":"2394","name":"0","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"1","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"2","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"3","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"4","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"5","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"6","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"7","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"8","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"9","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"}]},{"name":"个位","codes":[{"menuid":"406705","methodid":"6367460","name":"大","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"},{"menuid":"406705","methodid":"6367460","name":"小","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"},{"menuid":"406705","methodid":"6367460","name":"单","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"},{"menuid":"406705","methodid":"6367460","name":"双","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"},{"menuid":"2376","methodid":"2394","name":"0","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"1","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"2","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"3","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"4","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"5","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"6","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"7","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"8","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"2376","methodid":"2394","name":"9","type":"digital","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"}]},{"name":"总和","codes":[{"menuid":"390708","methodid":"6363711","name":"大","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"},{"menuid":"390708","methodid":"6363711","name":"小","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"},{"menuid":"390708","methodid":"6363711","name":"单","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"},{"menuid":"390708","methodid":"6363711","name":"双","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"}]},{"name":"五星和值大小单双","codes":[{"menuid":"390708","methodid":"6363711","name":"大","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"},{"menuid":"390708","methodid":"6363711","name":"小","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"},{"menuid":"390708","methodid":"6363711","name":"单","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"},{"menuid":"390708","methodid":"6363711","name":"双","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"},{"menuid":"390708","methodid":"6363711","name":"大单","type":"dxds","show_str":"X","non_rebate_prize":"7.46","rebate_prize":"7.16"},{"menuid":"390708","methodid":"6363711","name":"大双","type":"dxds","show_str":"X","non_rebate_prize":"8.42","rebate_prize":"8.08"},{"menuid":"390708","methodid":"6363711","name":"小单","type":"dxds","show_str":"X","non_rebate_prize":"8.42","rebate_prize":"8.08"},{"menuid":"390708","methodid":"6363711","name":"小双","type":"dxds","show_str":"X","non_rebate_prize":"7.46","rebate_prize":"7.16"}]},{"name":"前三和值大小单双","codes":[{"menuid":"390722","methodid":"6363708","name":"大","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"},{"menuid":"390722","methodid":"6363708","name":"小","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"},{"menuid":"390722","methodid":"6363708","name":"单","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"},{"menuid":"390722","methodid":"6363708","name":"双","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"}]},{"name":"中三和值大小单双","codes":[{"menuid":"390723","methodid":"6363709","name":"大","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"},{"menuid":"390723","methodid":"6363709","name":"小","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"},{"menuid":"390723","methodid":"6363709","name":"单","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"},{"menuid":"390723","methodid":"6363709","name":"双","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"}]},{"name":"后三和值大小单双","codes":[{"menuid":"390724","methodid":"6363710","name":"大","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"},{"menuid":"390724","methodid":"6363710","name":"小","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"},{"menuid":"390724","methodid":"6363710","name":"单","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"},{"menuid":"390724","methodid":"6363710","name":"双","type":"dxds","show_str":"X","non_rebate_prize":"3.96","rebate_prize":"3.80"}]}]},{"category":"龙虎斗","groups":[{"name":"万个龙虎斗","codes":[{"menuid":"390804","methodid":"6363723","name":"龙","type":"dxds","show_str":"X","non_rebate_prize":"4.40","rebate_prize":"4.22"},{"menuid":"390804","methodid":"6363723","name":"虎","type":"dxds","show_str":"X","non_rebate_prize":"4.40","rebate_prize":"4.22"},{"menuid":"390804","methodid":"6363723","name":"和","type":"dxds","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"}]},{"name":"千十龙虎斗","codes":[{"menuid":"390806","methodid":"6363725","name":"龙","type":"dxds","show_str":"X","non_rebate_prize":"4.40","rebate_prize":"4.22"},{"menuid":"390806","methodid":"6363725","name":"虎","type":"dxds","show_str":"X","non_rebate_prize":"4.40","rebate_prize":"4.22"},{"menuid":"390806","methodid":"6363725","name":"和","type":"dxds","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"}]},{"name":"万千龙虎斗","codes":[{"menuid":"320396","methodid":"6130784","name":"龙","type":"dxds","show_str":"X","non_rebate_prize":"4.40","rebate_prize":"4.22"},{"menuid":"320396","methodid":"6130784","name":"虎","type":"dxds","show_str":"X","non_rebate_prize":"4.40","rebate_prize":"4.22"},{"menuid":"320396","methodid":"6130784","name":"和","type":"dxds","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"}]},{"name":"万百龙虎斗","codes":[{"menuid":"320397","methodid":"6130785","name":"龙","type":"dxds","show_str":"X","non_rebate_prize":"4.40","rebate_prize":"4.22"},{"menuid":"320397","methodid":"6130785","name":"虎","type":"dxds","show_str":"X","non_rebate_prize":"4.40","rebate_prize":"4.22"},{"menuid":"320397","methodid":"6130785","name":"和","type":"dxds","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"}]},{"name":"万十龙虎斗","codes":[{"menuid":"390803","methodid":"6363722","name":"龙","type":"dxds","show_str":"X","non_rebate_prize":"4.40","rebate_prize":"4.22"},{"menuid":"390803","methodid":"6363722","name":"虎","type":"dxds","show_str":"X","non_rebate_prize":"4.40","rebate_prize":"4.22"},{"menuid":"390803","methodid":"6363722","name":"和","type":"dxds","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"}]},{"name":"千百龙虎斗","codes":[{"menuid":"390805","methodid":"6363724","name":"龙","type":"dxds","show_str":"X","non_rebate_prize":"4.40","rebate_prize":"4.22"},{"menuid":"390805","methodid":"6363724","name":"虎","type":"dxds","show_str":"X","non_rebate_prize":"4.40","rebate_prize":"4.22"},{"menuid":"390805","methodid":"6363724","name":"和","type":"dxds","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"}]},{"name":"千个龙虎斗","codes":[{"menuid":"390807","methodid":"6363726","name":"龙","type":"dxds","show_str":"X","non_rebate_prize":"4.40","rebate_prize":"4.22"},{"menuid":"390807","methodid":"6363726","name":"虎","type":"dxds","show_str":"X","non_rebate_prize":"4.40","rebate_prize":"4.22"},{"menuid":"390807","methodid":"6363726","name":"和","type":"dxds","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"}]},{"name":"百十龙虎斗","codes":[{"menuid":"390808","methodid":"6363727","name":"龙","type":"dxds","show_str":"X","non_rebate_prize":"4.40","rebate_prize":"4.22"},{"menuid":"390808","methodid":"6363727","name":"虎","type":"dxds","show_str":"X","non_rebate_prize":"4.40","rebate_prize":"4.22"},{"menuid":"390808","methodid":"6363727","name":"和","type":"dxds","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"}]},{"name":"百个龙虎斗","codes":[{"menuid":"390809","methodid":"6363728","name":"龙","type":"dxds","show_str":"X","non_rebate_prize":"4.40","rebate_prize":"4.22"},{"menuid":"390809","methodid":"6363728","name":"虎","type":"dxds","show_str":"X","non_rebate_prize":"4.40","rebate_prize":"4.22"},{"menuid":"390809","methodid":"6363728","name":"和","type":"dxds","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"}]},{"name":"十个龙虎斗","codes":[{"menuid":"390810","methodid":"6363729","name":"龙","type":"dxds","show_str":"X","non_rebate_prize":"4.40","rebate_prize":"4.22"},{"menuid":"390810","methodid":"6363729","name":"虎","type":"dxds","show_str":"X","non_rebate_prize":"4.40","rebate_prize":"4.22"},{"menuid":"390810","methodid":"6363729","name":"和","type":"dxds","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"}]}]},{"category":"百家乐","groups":[{"name":"庄","codes":[{"menuid":"320399","methodid":"6130789","name":"庄","type":"dxds","show_str":"X","non_rebate_prize":"4.40","rebate_prize":"4.22"}]},{"name":"闲","codes":[{"menuid":"320399","methodid":"6130789","name":"闲","type":"dxds","show_str":"X","non_rebate_prize":"4.40","rebate_prize":"4.22"}]},{"name":"和","codes":[{"menuid":"320400","methodid":"6130790","name":"和","type":"dxds","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"}]},{"name":"对子","codes":[{"menuid":"320401","methodid":"6130791","name":"庄","type":"dxds","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"},{"menuid":"320401","methodid":"6130791","name":"闲","type":"dxds","show_str":"X","non_rebate_prize":"19.80","rebate_prize":"19.00"}]},{"name":"豹子","codes":[{"menuid":"320402","methodid":"6130792","name":"庄","type":"dxds","show_str":"X","non_rebate_prize":"198.00","rebate_prize":"190.00"},{"menuid":"320402","methodid":"6130792","name":"闲","type":"dxds","show_str":"X","non_rebate_prize":"198.00","rebate_prize":"190.00"}]},{"name":"天王","codes":[{"menuid":"320403","methodid":"6130793","name":"庄","type":"dxds","show_str":"X","non_rebate_prize":"10.42","rebate_prize":"10.00"},{"menuid":"320403","methodid":"6130793","name":"闲","type":"dxds","show_str":"X","non_rebate_prize":"10.42","rebate_prize":"10.00"}]}]},{"category":"牛牛","groups":[{"name":"无牛","codes":[{"menuid":"390738","methodid":"6363730","name":"无牛","type":"dxds","show_str":"X","non_rebate_prize":"5.55","rebate_prize":"5.32"}]},{"name":"牛1","codes":[{"menuid":"390738","methodid":"6363730","name":"牛1","type":"dxds","show_str":"X","non_rebate_prize":"31.05","rebate_prize":"29.80"}]},{"name":"牛2","codes":[{"menuid":"390738","methodid":"6363730","name":"牛2","type":"dxds","show_str":"X","non_rebate_prize":"30.43","rebate_prize":"29.20"}]},{"name":"牛3","codes":[{"menuid":"390738","methodid":"6363730","name":"牛3","type":"dxds","show_str":"X","non_rebate_prize":"31.05","rebate_prize":"29.80"}]},{"name":"牛4","codes":[{"menuid":"390738","methodid":"6363730","name":"牛4","type":"dxds","show_str":"X","non_rebate_prize":"30.43","rebate_prize":"29.20"}]},{"name":"牛5","codes":[{"menuid":"390738","methodid":"6363730","name":"牛5","type":"dxds","show_str":"X","non_rebate_prize":"31.05","rebate_prize":"29.80"}]},{"name":"牛6","codes":[{"menuid":"390738","methodid":"6363730","name":"牛6","type":"dxds","show_str":"X","non_rebate_prize":"30.43","rebate_prize":"29.20"}]},{"name":"牛7","codes":[{"menuid":"390738","methodid":"6363730","name":"牛7","type":"dxds","show_str":"X","non_rebate_prize":"31.05","rebate_prize":"29.80"}]},{"name":"牛8","codes":[{"menuid":"390738","methodid":"6363730","name":"牛8","type":"dxds","show_str":"X","non_rebate_prize":"30.43","rebate_prize":"29.20"}]},{"name":"牛9","codes":[{"menuid":"390738","methodid":"6363730","name":"牛9","type":"dxds","show_str":"X","non_rebate_prize":"31.05","rebate_prize":"29.80"}]},{"name":"牛牛","codes":[{"menuid":"390738","methodid":"6363730","name":"牛牛","type":"dxds","show_str":"X","non_rebate_prize":"30.66","rebate_prize":"29.42"}]},{"name":"牛大","codes":[{"menuid":"390738","methodid":"6363730","name":"牛大","type":"dxds","show_str":"X","non_rebate_prize":"6.14","rebate_prize":"5.89"}]},{"name":"牛小","codes":[{"menuid":"390738","methodid":"6363730","name":"牛小","type":"dxds","show_str":"X","non_rebate_prize":"6.16","rebate_prize":"5.91"}]},{"name":"牛单","codes":[{"menuid":"390738","methodid":"6363730","name":"牛单","type":"dxds","show_str":"X","non_rebate_prize":"6.21","rebate_prize":"5.96"}]},{"name":"牛双","codes":[{"menuid":"390738","methodid":"6363730","name":"牛双","type":"dxds","show_str":"X","non_rebate_prize":"6.09","rebate_prize":"5.85"}]}]},{"category":"炸金花","groups":[{"name":"前三","codes":[{"menuid":"402321","methodid":"6368206","name":"豹子","type":"dxds","show_str":"X","non_rebate_prize":"198","rebate_prize":"190.00"},{"menuid":"402321","methodid":"6368206","name":"顺子","type":"dxds","show_str":"X","non_rebate_prize":"33","rebate_prize":"31.67"},{"menuid":"402321","methodid":"6368206","name":"对子","type":"dxds","show_str":"X","non_rebate_prize":"7.33","rebate_prize":"7.04"},{"menuid":"402321","methodid":"6368206","name":"半顺","type":"dxds","show_str":"X","non_rebate_prize":"5.5","rebate_prize":"5.28"},{"menuid":"402321","methodid":"6368206","name":"杂六","type":"dxds","show_str":"X","non_rebate_prize":"6.6","rebate_prize":"6.33"}]},{"name":"中三","codes":[{"menuid":"402322","methodid":"6368207","name":"豹子","type":"dxds","show_str":"X","non_rebate_prize":"198","rebate_prize":"190.00"},{"menuid":"402322","methodid":"6368207","name":"顺子","type":"dxds","show_str":"X","non_rebate_prize":"33","rebate_prize":"31.67"},{"menuid":"402322","methodid":"6368207","name":"对子","type":"dxds","show_str":"X","non_rebate_prize":"7.33","rebate_prize":"7.04"},{"menuid":"402322","methodid":"6368207","name":"半顺","type":"dxds","show_str":"X","non_rebate_prize":"5.5","rebate_prize":"5.28"},{"menuid":"402322","methodid":"6368207","name":"杂六","type":"dxds","show_str":"X","non_rebate_prize":"6.6","rebate_prize":"6.33"}]},{"name":"后三","codes":[{"menuid":"402323","methodid":"6368208","name":"豹子","type":"dxds","show_str":"X","non_rebate_prize":"198","rebate_prize":"190.00"},{"menuid":"402323","methodid":"6368208","name":"顺子","type":"dxds","show_str":"X","non_rebate_prize":"33","rebate_prize":"31.67"},{"menuid":"402323","methodid":"6368208","name":"对子","type":"dxds","show_str":"X","non_rebate_prize":"7.33","rebate_prize":"7.04"},{"menuid":"402323","methodid":"6368208","name":"半顺","type":"dxds","show_str":"X","non_rebate_prize":"5.5","rebate_prize":"5.28"},{"menuid":"402323","methodid":"6368208","name":"杂六","type":"dxds","show_str":"X","non_rebate_prize":"6.6","rebate_prize":"6.33"}]}]},{"category":"梭哈","groups":[{"name":"梭哈","codes":[{"menuid":"402355","methodid":"6368240","name":"五条","type":"dxds","show_str":"X","non_rebate_prize":"19800","rebate_prize":"19000.00"},{"menuid":"402355","methodid":"6368240","name":"四条","type":"dxds","show_str":"X","non_rebate_prize":"440","rebate_prize":"422.22"},{"menuid":"402355","methodid":"6368240","name":"葫芦","type":"dxds","show_str":"X","non_rebate_prize":"220","rebate_prize":"211.11"},{"menuid":"402355","methodid":"6368240","name":"顺子","type":"dxds","show_str":"X","non_rebate_prize":"165","rebate_prize":"158.33"},{"menuid":"402355","methodid":"6368240","name":"三条","type":"dxds","show_str":"X","non_rebate_prize":"27.5","rebate_prize":"26.39"},{"menuid":"402355","methodid":"6368240","name":"两对","type":"dxds","show_str":"X","non_rebate_prize":"18.3333","rebate_prize":"17.59"},{"menuid":"402355","methodid":"6368240","name":"一对","type":"dxds","show_str":"X","non_rebate_prize":"3.9285","rebate_prize":"3.77"},{"menuid":"402355","methodid":"6368240","name":"单牌","type":"dxds","show_str":"X","non_rebate_prize":"6.8181","rebate_prize":"6.54"}]}]}],"timestamp":1716178052}

    /**
     * status
     */
    @SerializedName("status")
    private int status;
    /**
     * message
     */
    @SerializedName("message")
    private String message;
    /**
     * data
     */
    @SerializedName("data")
    private List<DataDTO> data;
    /**
     * timestamp
     */
    @SerializedName("timestamp")
    private int timestamp;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataDTO> getData() {
        return data;
    }

    public void setData(List<DataDTO> data) {
        this.data = data;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public static class DataDTO {
        /**
         * category
         */
        @SerializedName("category")
        private String category;
        /**
         * groups
         */
        @SerializedName("groups")
        private List<GroupsDTO> groups;

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public List<GroupsDTO> getGroups() {
            return groups;
        }

        public void setGroups(List<GroupsDTO> groups) {
            this.groups = groups;
        }

        public static class GroupsDTO {
            /**
             * name
             */
            @SerializedName("name")
            private String name;
            /**
             * codes
             */
            @SerializedName("codes")
            private List<CodesDTO> codes;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public List<CodesDTO> getCodes() {
                return codes;
            }

            public void setCodes(List<CodesDTO> codes) {
                this.codes = codes;
            }

            public static class CodesDTO {
                /**
                 * menuid
                 */
                @SerializedName("menuid")
                private String menuid;
                /**
                 * methodid
                 */
                @SerializedName("methodid")
                private String methodid;
                /**
                 * name
                 */
                @SerializedName("name")
                private String name;
                /**
                 * type
                 */
                @SerializedName("type")
                private String type;
                /**
                 * showStr
                 */
                @SerializedName("show_str")
                private String showStr;
                /**
                 * nonRebatePrize
                 */
                @SerializedName("non_rebate_prize")
                private String nonRebatePrize;
                /**
                 * rebatePrize
                 */
                @SerializedName("rebate_prize")
                private String rebatePrize;

                //是否选中
                public ObservableField<Boolean> isChecked = new ObservableField<>(false);

                public String getMenuid() {
                    return menuid;
                }

                public void setMenuid(String menuid) {
                    this.menuid = menuid;
                }

                public String getMethodid() {
                    return methodid;
                }

                public void setMethodid(String methodid) {
                    this.methodid = methodid;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public String getShowStr() {
                    return showStr;
                }

                public void setShowStr(String showStr) {
                    this.showStr = showStr;
                }

                public String getNonRebatePrize() {
                    return nonRebatePrize;
                }

                public void setNonRebatePrize(String nonRebatePrize) {
                    this.nonRebatePrize = nonRebatePrize;
                }

                public String getRebatePrize() {
                    return rebatePrize;
                }

                public void setRebatePrize(String rebatePrize) {
                    this.rebatePrize = rebatePrize;
                }
            }
        }
    }
}
