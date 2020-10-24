package com.example.association

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup

/**
 * @Description
 * 代码高手
 1.确定了父容器，子控件不确定
 2.确定了子控件，不确定父容器
 3.子控件不确定，父容器也不确定
 */
class MyViewGroup:ViewGroup {
    private val space=30 //间距
    constructor(context:Context,attrs:AttributeSet?):super(context, attrs){

    }

    //测量子View 确定自己的最终尺寸
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //先预测量一下自己的限制尺寸  size mode
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        //获取预测量之后自己（父容器）的宽和高
        val parentWidth = MeasureSpec.getSize(widthMeasureSpec)
        val parentHeight = MeasureSpec.getSize(heightMeasureSpec)

        var childWidth =0
        var childHeight = 0
        //计算子控件的宽和高
        if (childCount==0){
            childWidth = parentWidth- 2*space
            childHeight = parentHeight -2*space
        }else{
            childWidth = (parentWidth-3*space)/2
            //计算有多少行
            val row = (childCount+1)/2
            childHeight = (parentHeight-(row+1)*space)/row
        }

        //将尺寸设置给子控件
        //获取子控件测量时需要的spec,确定一个限制条件
        val widthSpec = MeasureSpec.makeMeasureSpec(childWidth,MeasureSpec.EXACTLY)
        val heightSpec = MeasureSpec.makeMeasureSpec(childHeight,MeasureSpec.EXACTLY)
        for (i in 0 until childCount){
            val child=getChildAt(i)
            child.measure(widthSpec,heightSpec)
        }
    }
    //按照规则来对自己的子控件进行布局
    /**
     val child= getChildAt(0)
     getChildCount
     测量完之后再进行布局  这个就是先测量再布局
     在xml中引用过后还需要在onLayout方法里面布局过之后才会显示出来
     */
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
          //获取某一个子控件 因为目前只加了一个控件，所以0就是代表我们的控件
        var left = 0
        var top=0
        var right=0
        var bottom=0
        for (i in 0 until childCount){
            //确定i具体的位置
            val row = i/2
            val column = i%2
            val child = getChildAt(i)

            left = space+ column*(child.measuredWidth+space)
            top = space+ row*(child.measuredHeight+space)
            right = left+child.measuredWidth
            bottom = top+child.measuredHeight
            //告诉子容器放在哪个位置,对子控件进行布局
            child.layout(left,top, right, bottom)
        }

    }

    private fun dp2px(dp:Int):Int = (dp*resources.displayMetrics.density).toInt()
}