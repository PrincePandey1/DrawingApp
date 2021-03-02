package com.example.android.drawingapp

import android.content.Context
import android.graphics.*
import android.graphics.Color.BLACK
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View

class DrawingView(context:Context ,attrs: AttributeSet): View(context , attrs) {

    private var mDrawPath: CustomPath? = null
    private var mCanvasBitmap: Bitmap? = null
    private var mDrawPaint: Paint? = null
    private var mCanvasPaint: Paint? = null
    private var mBrushSize: Float = 0.toFloat()
    private var color = Color.BLACK
    private var canvas: Canvas? = null
    private val mPaths = ArrayList<CustomPath>()
    init {
        setupDrawing()
    }

    override fun onSizeChanged(w:Int ,h: Int , oldw: Int ,oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mCanvasBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888)
        canvas = Canvas(mCanvasBitmap!!)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(mCanvasBitmap!!,0f,0f,mCanvasPaint)

        for(paths in mPaths){
            mDrawPaint!!.color= paths.color
            mDrawPaint!!.strokeWidth = paths.brushThickness
            canvas.drawPath(paths, mDrawPaint!!)
        }

        if(!mDrawPath!!.isEmpty) {
            canvas.drawPath(mDrawPath!!, mDrawPaint!!)
            mDrawPaint!!.color= mDrawPath!!.color
            mDrawPaint!!.strokeWidth = mDrawPath!!.brushThickness
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {

        val touchX = event?.x
        val touchY = event?.y

        when(event?.action){
            MotionEvent.ACTION_DOWN->{

                mDrawPath!!.color = color
                if (touchX != null) {
                    if (touchY != null) {
                        mDrawPath!!.moveTo(touchX,touchY)
                    }
                }
            }
            MotionEvent.ACTION_MOVE->{
                if (touchX != null) {
                    if (touchY != null) {
                        mDrawPath!!.lineTo(touchX,touchY)
                    }
                }
            }
            MotionEvent.ACTION_UP->{

                mPaths.add(mDrawPath!!)
                mDrawPath = CustomPath(color,mBrushSize)
            }
            else-> return false



        }

        invalidate()

        return true
    }

    private fun setupDrawing() {
        mDrawPath = CustomPath(color , mBrushSize)
        mDrawPaint = Paint()
        mDrawPaint!!.color = color
        mDrawPaint!!.style = Paint.Style.STROKE
        mDrawPaint!!.strokeJoin = Paint.Join.ROUND
        mDrawPaint!!.strokeCap = Paint.Cap.ROUND
        mCanvasPaint = Paint(Paint.DITHER_FLAG)
        mBrushSize = 20.toFloat()
    }

    fun setSizeForBrush(newSize: Float){
        mBrushSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,newSize,resources.displayMetrics)
        mDrawPaint!!.strokeWidth = mBrushSize

    }

    fun setColor(newColor: String){
        color = Color.parseColor(newColor)
        mDrawPaint!!.color = color
    }

    internal inner class CustomPath(var color: Int , var brushThickness: Float) : Path() {
    }

}