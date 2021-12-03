package com.slack.exercise.ui.usersearch

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.slack.exercise.R
import com.slack.exercise.model.UserSearchResult
import kotlinx.android.synthetic.main.item_user_search.view.*

/**
 * Adapter for the list of [UserSearchResult].
 */
class UserSearchAdapter : RecyclerView.Adapter<UserSearchAdapter.UserSearchViewHolder>() {
  private var userSearchResults: List<UserSearchResult> = emptyList()
  private lateinit var context: Context

  fun setResults(results: Set<UserSearchResult>) {
    userSearchResults = results.toList()
    notifyDataSetChanged()
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserSearchViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user_search, parent, false)
    context = parent.context
    return UserSearchViewHolder(view)
  }

  override fun getItemCount(): Int {
    return userSearchResults.size
  }

  override fun onBindViewHolder(holder: UserSearchViewHolder, position: Int) {
    loadProfileImage(userSearchResults[position].avatar_url, holder.userProfile)
    holder.displayName.text = userSearchResults[position].display_name
    holder.username.text = userSearchResults[position].username
  }

  class UserSearchViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val userProfile: ImageView = itemView.imageProfile
    val displayName: TextView = itemView.textDisplayName
    val username: TextView = itemView.textUserName
  }

  private fun loadProfileImage(imgUrl: String, profileImg: ImageView) {
    val requestOptions = RequestOptions().transform(CenterCrop(), RoundedCorners(4))
    Glide.with(context)
      .load(imgUrl)
      .override(50, 50)
      .apply(requestOptions)
      .into(profileImg)
  }

}