package com.xj.webview.sample

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.xj.webview.sample.simple.SimpleWebViewPagerActivity2
import com.xj.webview.sample.x5.X5Helper
import com.xj.webview.sample.x5.X5WebViewPagerActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)
        X5Helper.init(this)

        findViewById<Button>(R.id.btn_x5).setOnClickListener {
            startActivity(Intent(this, X5WebViewPagerActivity::class.java))
        }
        findViewById<Button>(R.id.btn_webview).setOnClickListener {
            startActivity(Intent(this, SimpleWebViewPagerActivity2::class.java))
        }

    }
}