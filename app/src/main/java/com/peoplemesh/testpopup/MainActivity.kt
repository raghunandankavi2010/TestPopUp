package com.peoplemesh.testpopup

import android.annotation.SuppressLint
import android.graphics.Point
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val editText = findViewById<EditText>(R.id.editText)
        editText.setOnClickListener {
            showPopupWindow(editText)
        }
    }

    @SuppressLint("InflateParams")
    private fun showPopupWindow(anchor: View) {
        var popUpHeight: Int
        PopupWindow(anchor.context).apply {
            isOutsideTouchable = true
            val inflater = from(anchor.context)
            contentView = inflater.inflate(R.layout.popup_layout, null).apply {
                measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                )
                measuredWidth
                measuredHeight
                popUpHeight = measuredHeight
            }
            height = ViewGroup.LayoutParams.WRAP_CONTENT
            width = ViewGroup.LayoutParams.MATCH_PARENT
        }.also { popupWindow ->
            val location = IntArray(2).apply {
                anchor.getLocationInWindow(this)
            }
//            val size = Size(
//                popupWindow.contentView.measuredWidth,
//                popupWindow.contentView.measuredHeight
//            )
            val editText = anchor as EditText
            val pos = editText.selectionStart
            val layout: Layout = editText.layout
            val line: Int = layout.getLineForOffset(pos)
            //val baseline: Int = layout.getLineBaseline(line)
            val ascent: Int = layout.getLineAscent(line)
            editText.getLocationOnScreen(location)
            val point = Point()
            point.x = layout.getPrimaryHorizontal(pos).toInt()
            point.y = layout.getLineBottom(line)// location[1] //-  (baseline + ascent )
            val y = point.y - editText.scrollY
            //val top = layout.getLineTop(line) + editText.scrollY
            val editTextheight = editText.measuredHeight
            val yOffset = if (y >= (editTextheight / 2)) {
                y - (popUpHeight + 50 - ascent)
            } else {
                y + 50
            }
            popupWindow.showAsDropDown(
                anchor, 0,
                yOffset
            )
        }
    }
}

