package com.codepath.apps.restclienttemplate.models

import com.codepath.apps.restclienttemplate.TimeFormatter
import org.json.JSONArray
import org.json.JSONObject

class Tweet {

    fun getFormattedTimestamp (createdAt: String): String? {
        return TimeFormatter.getTimeDifference(createdAt)
    }

    var body: String = ""
    var createdAt: String = ""
    var user: User? = null

    companion object{
        fun fromJson(jsonObject: JSONObject): Tweet{
            val tweet = Tweet()
            tweet.body = jsonObject.getString("text")
            tweet.createdAt = jsonObject.getString("created_at")
            //String formattedTime = TimeFormatter.getTimeDifference(String dateString);
            //TimeFormatter.getTimeDifference("created_at")
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"))
            return tweet
        }

        fun fromJsonArray(jsonArray: JSONArray): List<Tweet> {
            val tweets = ArrayList<Tweet>()
            for(i in 0 until jsonArray.length()){
                tweets.add(fromJson(jsonArray.getJSONObject(i)))
            }
            return tweets

        }
    }
}