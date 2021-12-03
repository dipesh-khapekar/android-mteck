package com.slack.exercise.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.slack.exercise.R

internal class ViewPager2Adapter     // Constructor of our ViewPager2Adapter class
    (private val ctx: Context) :
    RecyclerView.Adapter<ViewHolder>() {
    // Array of images
    // Adding images from drawable folder
    private val images = intArrayOf(
        R.drawable.image_a,
        R.drawable.image_b,
        R.drawable.image_c
    )

    // This method returns our layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.images_holder, parent, false)
        return CardViewHolder(view)
    }

    // This Method returns the size of the Array
    override fun getItemCount(): Int {
        return images.size
    }

    // The ViewHolder class holds the view
    class CardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var images: ImageView = itemView.findViewById(R.id.images)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder as CardViewHolder).images.setImageResource(images[position])
    }
}
