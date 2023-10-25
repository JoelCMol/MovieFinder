package com.example.moviefinder

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class MainActivity : AppCompatActivity() {

    var characterImageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchView = findViewById<SearchView>(R.id.searchView)
        val imageView = findViewById<ImageView>(R.id.movieImageView)
        val char_name = findViewById<TextView>(R.id.movieTitleTextView)
        val char_desc = findViewById<TextView>(R.id.movieDescriptionTextView)
        val year = findViewById<TextView>(R.id.yearReleasedRunTimeTextView)
        val genre = findViewById<TextView>(R.id.genreTextView)


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(character: String?): Boolean {
                if (character != null) {
                    search(character, imageView, char_name, char_desc, year, genre)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
    }

    private fun search(character: String, imageView: ImageView, char_name: TextView, char_desc: TextView, year: TextView, genre: TextView) {
        val client = AsyncHttpClient()

        val apiKey = "d3065677"
        // Include the hash in your API request URL
        val url = "http://www.omdbapi.com/?t=$character&apikey=$apiKey"

        client[url, object : JsonHttpResponseHandler() {

            override fun onSuccess(statusCode: Int, headers: Headers, json: JsonHttpResponseHandler.JSON) {

                val jsonObject = json.jsonObject
                Log.d("Character", "response successful")
                Log.d("Character", "response: $jsonObject")

                characterImageUrl = jsonObject.getString("Poster")
                Log.d("characterImageUrl", "character image URL set to: $characterImageUrl")

                Glide.with(this@MainActivity)
                    .load(characterImageUrl)
                    .fitCenter()
                    .into(imageView)

                char_name.text = jsonObject.getString("Title")
                char_desc.text = jsonObject.getString("Plot")
                year.text = "Released · " + jsonObject.getString("Year")+" · " +
                    jsonObject.getString("Runtime")
                genre.text = jsonObject.getString("Genre")

            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.d("Character Error", errorResponse)
            }
        }]
    }
}