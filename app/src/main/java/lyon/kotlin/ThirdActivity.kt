package lyon.kotlin

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import lyon.kotlin.Model.ImageGetModel
import lyon.kotlin.Tool.LogL
import lyon.kotlin.Tool.Tool
import org.json.JSONObject
import java.lang.NullPointerException


class ThirdActivity: AppCompatActivity() {
    val TAG = "ThirdActivity"
    lateinit var idTextView: TextView
    lateinit var titleTextView: TextView
    lateinit var imageView: ImageView
    lateinit var webView: WebView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.third_layout)
        val json = intent.getStringExtra(JSON)
        var jsonObject:JSONObject
        LogL.d(TAG,""+json)
        if(!json.isNullOrEmpty()){
            jsonObject= JSONObject(json)

            imageView = findViewById(R.id.imageView)
            idTextView = findViewById(R.id.id_txt)
            titleTextView = findViewById(R.id.title_txt)
            webView = findViewById(R.id.webview)
            val mWebSettings: WebSettings = webView.getSettings()
            mWebSettings.useWideViewPort = true
            mWebSettings.loadWithOverviewMode = true
            webView.visibility= View.GONE
            var title = jsonObject.optString("title")
            var id = jsonObject.optString("id")
            var url = jsonObject.optString("thumbnailUrl")
            titleTextView.text="title:"+title
            idTextView.text="id:"+id


            object : ImageGetModel(this){
                override fun parseBitmap(bitmap: Bitmap?) {
                    try {
                        if(bitmap!=null)
                            imageView.setImageBitmap(bitmap)
                        else{
                            imageView.visibility=View.GONE
                            webView.visibility=View.VISIBLE
                            webView.loadUrl(url)
                        }
                    }catch (e: NullPointerException){
                        LogL.e(TAG,"ImageGetModel:"+ Tool.FormatStackTrace(e))
                    }
                }
            }.execute(url)
        }
    }
}