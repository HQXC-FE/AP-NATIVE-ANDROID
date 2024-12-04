package com.xtree.lottery.data.source.vo

data class MethodMenus(
    val labels: List<Label>,
    val lotteryid: Int,
    val menuid: Int
)

data class Label(
    val isdefault: Boolean,
    val isnew: Boolean,
    val labels: List<LabelX>,
    val title: String
)

data class LabelX(
    val labels: List<LabelXX>,
    val title: String
)

data class LabelXX(
    val code_sp: String,
    val defaultposition: String,
    val description: String,
    val is_multiple: Int,
    val maxcodecount: Int,
    val menuid: Int,
    val methoddesc: String,
    val methodexample: String,
    val methodhelp: String,
    val methodid: Int,
    val money_modes: List<MoneyMode>,
    val name: String,
    val relationMethods: List<Int>,
    val selectarea: Selectarea,
    val show_str: String
)

data class MoneyMode(
    val modeid: Int,
    val name: String,
    val rate: Double
)

data class Selectarea(
    val isButton: Boolean,
    val layout: List<Layout>,
    val noBigIndex: Int,
    val selPosition: String,
    val singletypetips: String,
    val type: String
)

data class Layout(
    val cols: Int,
    val minchosen: Int,
    val no: String,
    val place: Int,
    val title: String
)