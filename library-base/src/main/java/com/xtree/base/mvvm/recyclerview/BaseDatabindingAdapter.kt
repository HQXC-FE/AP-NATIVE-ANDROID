package com.xtree.base.mvvm.recyclerview

import android.util.NoSuchPropertyException
import android.view.View
import com.drake.brv.BindingAdapter
import kotlin.reflect.full.createType

/**
 *Created by KAKA on 2024/3/8.
 *Describe: RecyclerView适配器的封装层，这里会提供BRV的功能转化和封装操作
 */
class BaseDatabindingAdapter : BindingAdapter() {

    /**
     * 数据回调
     */
    interface onBindListener {
        /**
         * 可操作视图和adapter,最好不用
         */
        fun onBind(bindingViewHolder: BindingViewHolder, view: View, itemViewType: Int)

        /**
         * item点击回调
         */
        fun onItemClick(modelPosition: Int, layoutPosition: Int, itemViewType: Int)
    }

    /**
     * 数据和视图绑定
     */
    fun initData(
        datas: List<BindModel>,
        layouts: List<Int>
    ) {

        datas
            .distinctBy { it::class } //强制规定数据类型
            .forEach { model ->
                val kType = if (model.javaClass.kotlin.toString().contains("\$")) {//匿名内部类
                    (model.javaClass.superclass as Class).kotlin.createType()
                } else {
                    model.javaClass.kotlin.createType()
                }

                typePool[kType] = { layouts[model.itemType] }
            }

        datas.filterNot {

            if (it is BindHead || it is BindFooter) {
                if (it is BindHead) addHeader(it)
                if (it is BindFooter) addFooter(it)
                true
            } else {
                false
            }
        }.let { models = it }

    }

    override fun getItemViewType(position: Int): Int {
        val model = getModel<Any>(position)
        // 获取模型的 Java 类
        val modelClass = model::class.java

        // 检查是否为匿名内部类
        if (modelClass.name.contains("\$")) {
            // 处理匿名内部类的逻辑
            return typePool.firstNotNullOfOrNull {
                it.value
            }?.invoke(model, position)
                ?: interfacePool.firstNotNullOfOrNull {
                    it.value
                }?.invoke(model, position)
                ?: throw NoSuchPropertyException("Please add item model type : addType<${model.javaClass.name}>(R.layout.item)")
        }
        return super.getItemViewType(position)
    }

}