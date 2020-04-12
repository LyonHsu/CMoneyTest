package lyon.kotlin

import android.graphics.Bitmap
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import lyon.kotlin.Model.ImageGetModel
import org.json.JSONObject
import java.lang.NullPointerException


class ThirdActivity: AppCompatActivity() {
    val TAG = "ThirdActivity"
    lateinit var idTextView: TextView
    lateinit var titleTextView: TextView
    lateinit var imageView: ImageView
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
            var title = jsonObject.optString("title")
            var id = jsonObject.optString("id")
            var url = jsonObject.optString("thumbnailUrl")
            titleTextView.text="title:"+title
            idTextView.text="id:"+id


            object : ImageGetModel(){
                override fun parseBitmap(bitmap: Bitmap?) {
                    try {
                        if(bitmap!=null)
                            imageView.setImageBitmap(bitmap)
                    }catch (e: NullPointerException){
                        LogL.e(TAG,"ImageGetModel:"+Tool.FormatStackTrace(e))
                    }
                }
            }.execute(url)
        }
    }
}