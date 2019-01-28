package org.timreynolds.imagesearch.gallery.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ViewGroup
import kotlinx.android.synthetic.main.gallery_item_photo.view.*
import org.timreynolds.imagesearch.R
import org.timreynolds.imagesearch.data.db.Gallery
import org.timreynolds.imagesearch.util.TAG
import org.timreynolds.imagesearch.util.inflate
import org.timreynolds.imagesearch.util.loadImage

class PhotoDelegateAdapter(val viewActions: onViewSelectedListener) {

    interface onViewSelectedListener {
        fun onItemSelected(photo: Gallery)
    }

    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
        return PhotoViewHolder(parent)
    }

    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Gallery) {
        holder as PhotoViewHolder
        holder.bind(item)
    }

    inner class PhotoViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
            parent.inflate(R.layout.gallery_item_photo)) {

        fun bind(item: Gallery) = with(itemView) {
            var staticUrl: String?
            // Create static url  and center crop larger image
            staticUrl = "https://farm" + item.farm + ".staticflickr.com/"+
                    item.server + "/" + item.flickrId + "_" + item.secret + ".jpg"
            Log.i(TAG, "** bind gallery image url ??? ->" + staticUrl)
            photo.loadImage(staticUrl)
            super.itemView.setOnClickListener { viewActions.onItemSelected(item) }
        }
    }
}

