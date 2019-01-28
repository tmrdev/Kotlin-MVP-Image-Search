package org.timreynolds.imagesearch.search.adapter

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import org.timreynolds.imagesearch.R

class SearchPhotoViewHolder(itemView: View, val r_tap: SearchViewHolderClickListener) : RecyclerView.ViewHolder(itemView),
        View.OnLongClickListener, View.OnClickListener {

    val textView: TextView
    val imageView: ImageView
    val linearLayout: LinearLayout

    init {
        textView = itemView.findViewById(R.id.myTextView)
        imageView = itemView.findViewById(R.id.searchImage)
        linearLayout = itemView.findViewById(R.id.rv_row_layout)
        linearLayout.setOnClickListener(this)
        linearLayout.setOnLongClickListener(this)
    }

    override fun onClick(v: View?) {
        r_tap.onTap(adapterPosition)
    }

    override fun onLongClick(v: View?): Boolean {
        r_tap.onLongTap(adapterPosition)
        return true
    }
}