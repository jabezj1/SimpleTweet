package com.codepath.apps.restclienttemplate

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.*
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class ComposeActivity : AppCompatActivity() {

    lateinit var etCompose: EditText
    lateinit var btnTweet: Button

    lateinit var client: TwitterClient

    lateinit var wordCount: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose = findViewById(R.id.etTweetCompose)
        btnTweet = findViewById(R.id.btnTweet)
        wordCount = findViewById(R.id.wordCount)

        client = TwitterApplication.getRestClient(this)

        etCompose.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Fires right as the text is being changed (even supplies the range of text)
                val count = s.length.toString()
                wordCount.text = count + "/280"
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Fires right before text is changing
                wordCount.setTextColor(Color.BLUE)
            }

            override fun afterTextChanged(s: Editable) {
                if (s.length > 280){
                    wordCount.setTextColor(Color.RED)
                }

                // Fires right after the text has changed
                // tvDisplay.setText(s.toString())
            }
        })

        // Handles the User's clicks on the tweet button
        btnTweet.setOnClickListener{

            //Grab the content of edittext (etcompose)
            val tweetContent = etCompose.text.toString()

            // 1. Make Sure the tweet isnt empty
            if (tweetContent.isEmpty()){
                Toast.makeText(this,"Empty Tweets not Allowed!", Toast.LENGTH_SHORT).show()
            }
            // 2. Make sure the tweet is under charater count
            if (tweetContent.length > 280){
                //btnTweet.isEnabled = false
                btnTweet.isClickable = false
                btnTweet.setBackgroundColor(Color.RED)
            }
            else {
                client.publishTweet(tweetContent, object: JsonHttpResponseHandler(){

                    override fun onSuccess(statusCode: Int, headers: Headers, json: JSON) {
                        Log.i(TAG, "Successfully published tweet!")

                        val tweet = Tweet.fromJson(json.jsonObject)

                        val intent = Intent()
                        intent.putExtra("tweet", tweet)
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers?,
                        response: String?,
                        throwable: Throwable?
                    ) {
                        Log.e(TAG,"Failed to Push tweet", throwable )
                    }
                })
            }
            //Make an API call to Twitter to push tweet
        }
    }
    companion object {
        val TAG = "ComposeActivity"
    }
}

