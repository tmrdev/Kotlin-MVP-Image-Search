package org.timreynolds.imagesearch.util

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout

class SquareLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0,
        defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {


    override fun onMeasure(width: Int, height: Int) {
        super.onMeasure(width, width)
    }

}