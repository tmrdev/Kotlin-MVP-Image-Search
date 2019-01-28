package org.timreynolds.imagesearch.gallery.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ViewGroup
import org.timreynolds.imagesearch.data.db.Gallery
import org.timreynolds.imagesearch.util.TAG


class GalleryAdapter(listener: PhotoDelegateAdapter.onViewSelectedListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var galleryItems: ArrayList<Gallery> = ArrayList()

    private var delegateAdapter = PhotoDelegateAdapter(listener)

    override fun getItemCount() = galleryItems.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        Log.i(TAG, "** onCreateViewHolder hit **")
        return delegateAdapter.onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.i(TAG, "** onBindViewHolder hit **")
        return delegateAdapter.onBindViewHolder(holder, this.galleryItems[position])
    }

    fun addSelectedPhotos(photos: List<Gallery>) {
        Log.i(TAG, "** addSelectedPhotos hit ** -> list size ->  " + photos.size)
        galleryItems.clear()
        galleryItems.addAll(photos)
        notifyDataSetChanged()
    }

    fun clear() {
        galleryItems.clear();
        notifyDataSetChanged();
    }

}