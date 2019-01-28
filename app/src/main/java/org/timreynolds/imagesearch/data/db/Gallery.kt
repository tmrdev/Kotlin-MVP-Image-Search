package org.timreynolds.imagesearch.data.db

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Gallery - note data is Parcelized so that it can be easily passed to other Activities
 * Indexes have been set on the fields below (flickrId, farm, server, etc...) so that duplicate images do not appear
 */
@Parcelize
@Entity(tableName = "photos", indices = arrayOf(Index(value = ["flickrId", "farm", "server", "secret", "owner"],
        unique = true)))
data class Gallery (
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "photoId")
        var id : Int,

        @ColumnInfo(name = "flickrId")
        var flickrId : Long,

        @ColumnInfo(name = "farm")
        var farm : Int,

        @ColumnInfo(name = "server")
        var server : String,

        @ColumnInfo(name = "secret")
        var secret : String,

        @ColumnInfo(name = "owner")
        var owner : String,

        @ColumnInfo(name = "title")
        var title: String
) : Parcelable