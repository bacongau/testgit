package com.example.t9b11

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_dial.setOnClickListener {
            val callIntent: Intent = Uri.parse("tel:5551234").let {
                Intent(Intent.ACTION_DIAL, it)
            }
            val callIntent2 = Intent(Intent.ACTION_DIAL)
            startActivity(callIntent)
        }

        btn_map.setOnClickListener {
            val mapIntent: Intent = Uri.parse(
                "geo:0,0?q=1600+Amphitheatre+Parkway,+Mountain+View,+California"
            ).let { location ->
                // Or map point based on latitude/longitude
                // val location: Uri = Uri.parse("geo:37.422219,-122.08364?z=14") // z param is zoom level
                Intent(Intent.ACTION_VIEW, location)
            }

            try {
                startActivity(mapIntent)
            } catch (e: ActivityNotFoundException) {
                // Define what your app should do if no activity can handle the intent.
            }
        }

        btn_web.setOnClickListener {
            val webIntent: Intent = Uri.parse("https://google.com").let {
                Intent(Intent.ACTION_VIEW, it)
            }
            startActivity(webIntent)
        }

        btn_send_email.setOnClickListener {
            val sendEmailIntent = Intent(Intent.ACTION_SEND).apply {
                // The intent does not have a URI, so declare the "text/plain" MIME type
                type = "text/plain"
                putExtra(Intent.EXTRA_EMAIL, arrayOf("jan@example.com")) // recipients
                putExtra(Intent.EXTRA_SUBJECT, "Email subject")
                putExtra(Intent.EXTRA_TEXT, "Email message text")
                putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/email/attachment"))
                // You can also attach multiple items by passing an ArrayList of Uris
            }

            val title = resources.getString(R.string.chooser_title)
            val chooser = Intent.createChooser(sendEmailIntent, title)

            try {
                startActivity(chooser)
            } catch (e: ActivityNotFoundException) {
                // Define what your app should do if no activity can handle the intent.
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
            }

        }

        btn_calendar_event.setOnClickListener {
            startActivity(Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI).apply {
                val beginTime: Calendar = Calendar.getInstance().apply {
                    set(2021, 0, 23, 7, 30)
                }
                val endTime = Calendar.getInstance().apply {
                    set(2021, 0, 23, 10, 30)
                }
                putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.timeInMillis)
                putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.timeInMillis)
                putExtra(CalendarContract.Events.TITLE, "Ninja class")
                putExtra(CalendarContract.Events.EVENT_LOCATION, "Secret dojo")
            })
        }

        btn_send.setOnClickListener {
            getContent.launch("image/*")
        }
    }


    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            // Handle the returned Uri
            Log.d(TAG, " -- : ${uri.toString()}")
            Glide.with(this).load(uri).into(imageView)
        }


}