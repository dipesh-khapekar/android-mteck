package com.slack.exercise.model

/**
 * Models users returned by the API.
 */
data class UserSearchResult(val avatar_url: String, val display_name: String, val username: String)