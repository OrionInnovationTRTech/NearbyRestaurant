package com.gurkan.nearbyrestaurantapp.ui.comment.commentList


import android.annotation.SuppressLint
import android.content.Context

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.model.Place

import com.google.android.libraries.places.api.net.*
import com.gurkan.nearbyrestaurantapp.R
import com.gurkan.nearbyrestaurantapp.model.Comment
import com.gurkan.nearbyrestaurantapp.ui.map.recyclerView.placesClient


class CommentListAdapter(
    private var commentList: ArrayList<Comment>,
    private val context: Context
) :
    RecyclerView.Adapter<CommentListAdapter.MyViewHolder>() {

    class MyViewHolder(
        private val view: View,
    ) :
        RecyclerView.ViewHolder(view) {
        val placeName: TextView = view.findViewById(R.id.placeNameComment)
        val placeComment: TextView = view.findViewById(R.id.placeComment)
        val userName: TextView = view.findViewById(R.id.tbUser)

        // val placeRating: RatingBar = view.findViewById(R.id.ratingBar)
        val placeImage: ImageView = view.findViewById(R.id.commentImage)


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.comment_items, parent, false
            )
        return MyViewHolder(view)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.placeName.text = commentList[position].placeName
        holder.placeComment.text = commentList[position].placeComment
        holder.userName.text = commentList[position].userName
        getPhoto(commentList[position].placeId,holder.placeImage)

        // holder.placeRating.rating=commentList[position].placeRating.toFloat()

    }
    override fun getItemCount() = commentList.size
}

private fun getPhoto(placeId: String, imageView: ImageView) {

// Define a Place ID.
// Specify fields. Requests for photos must always have the PHOTO_METADATAS field.
    val fields = listOf(Place.Field.PHOTO_METADATAS)

// Get a Place object (this example uses fetchPlace(), but you can also use findCurrentPlace())
    val placeRequest = FetchPlaceRequest.newInstance(placeId, fields)

    placesClient?.fetchPlace(placeRequest)
        ?.addOnSuccessListener { response: FetchPlaceResponse ->
            val place = response.place

            // Get the photo metadata.
            val metada = place.photoMetadatas
            if (metada == null || metada.isEmpty()) {
                imageView.setImageResource(com.gurkan.nearbyrestaurantapp.R.drawable.ic_launcher_foreground)
                return@addOnSuccessListener
            }
            val photoMetadata = metada.first()

            // Get the attribution text.
            val attributions = photoMetadata?.attributions

            // Create a FetchPhotoRequest.
            val photoRequest = FetchPhotoRequest.builder(photoMetadata)
                .setMaxWidth(500) // Optional.
                .setMaxHeight(300) // Optional.
                .build()
            placesClient.fetchPhoto(photoRequest)
                .addOnSuccessListener { fetchPhotoResponse: FetchPhotoResponse ->
                    val bitmap = fetchPhotoResponse.bitmap
                    imageView.setImageBitmap(bitmap)
                }.addOnFailureListener { exception: Exception ->
                    if (exception is ApiException) {

                        val statusCode = exception.statusCode
                        TODO("Handle error with given status code.")
                    }
                }
        }


}








