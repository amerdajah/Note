package com.amerdev.note

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText

class LinedEditText (context: Context, attrs: AttributeSet) : TextInputEditText(context, attrs) {

    private val rect: Rect = Rect()
    private val mPaint: Paint = Paint()
    private var mBaseline = 0

    init{
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 2.toFloat()
        mPaint.color = 0xFFFFD966.toInt()
    }
    override fun onDraw(canvas: Canvas) {
        mBaseline = getLineBounds(0, rect)
        for (i in 0..(height/lineHeight)){
            canvas.drawLine(rect.left.toFloat(), mBaseline+1.toFloat(), rect.right.toFloat(),
                mBaseline+1.toFloat(), mPaint)
            mBaseline += lineHeight
        }
        super.onDraw(canvas)
    }

}