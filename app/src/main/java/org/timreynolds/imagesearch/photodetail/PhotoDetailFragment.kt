package org.timreynolds.imagesearch.photodetail

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.*
import kotlinx.android.synthetic.main.fragment_photo_detail.*
import org.timreynolds.imagesearch.R
import org.timreynolds.imagesearch.data.db.Gallery
import org.timreynolds.imagesearch.util.NetworkUtils
import org.timreynolds.imagesearch.util.inflate
import org.timreynolds.imagesearch.util.loadImage
import org.timreynolds.imagesearch.util.toast

class PhotoDetailFragment : Fragment(), PhotoDetailContract.View {

    private var photoDetailPresenter: PhotoDetailContract.Presenter? = null

    companion object {
        fun newInstance(): PhotoDetailFragment {
            return PhotoDetailFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRetainInstance(true);
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        //view?.setBackgroundColor(resources.getColor(R.color.colorAccent))

        return container?.inflate(R.layout.fragment_photo_detail)
    }

    override fun onError(resId: Int) {
        val message = getString(resId)
        context.toast(message)
    }

    override fun onResume() {
        super.onResume()
        photoDetailPresenter?.subscribe()
    }

    override fun onPause() {
        super.onPause()
        photoDetailPresenter?.unsubscribe()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun setPresenter(presenter: PhotoDetailContract.Presenter) {
        photoDetailPresenter = presenter
    }

    override fun showPhoto(url: String) {
        photo.visibility = View.VISIBLE
        photo.loadImage(url, true)
    }

    override fun showMetadata(photoData: Gallery) {

        with(metadata) {
            visibility = View.VISIBLE
            ownerId.text = photoData.owner
            title.text = photoData.title.trim()
        }
    }

    override fun isNetworkConnected(): Boolean {
        return NetworkUtils.isNetworkConnected(context)
    }

    override fun isActive(): Boolean = isAdded

}
