package lyon.kotlin.SecondModel

import android.graphics.Bitmap
import android.os.AsyncTask
import android.graphics.BitmapFactory
import android.R.attr.src
import lyon.kotlin.LogL
import lyon.kotlin.Tool
import org.json.JSONArray
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.io.InputStream




abstract class ImageGetModel : AsyncTask<String, Void, Bitmap>(){
    val TAG = "ImageGetModel"
    val isDebug=true
    override fun doInBackground(vararg params: String?): Bitmap? {
        try {
            val url = URL(params[0])
            if(isDebug)
                LogL.d(TAG,"bitmap url:"+url)
            var bitmap: Bitmap? = null
            var imageUrl = params[0]
            imageUrl = "https://i.stack.imgur.com/9qWf6.png"
            if(isDebug)
                LogL.d(TAG,"bitmap imageUrl:"+imageUrl)
            bitmap = imageUrl?.let { downloadImage(it) }

            return bitmap

        } catch (e: IOException) {
            // Log exception
            LogL.e(TAG, "image get error:"+Tool.FormatStackTrace(e)+"")
            return null
        }
    }

    override fun onPostExecute(bitmap: Bitmap?) {
        if(isDebug)
            LogL.d(TAG,"bitmap:$bitmap")
        parseBitmap(bitmap)
    }

    abstract fun parseBitmap(bitmap: Bitmap?)

    fun downloadImage( imageUrl: String): Bitmap? {
        var bitmap: Bitmap? = null
        try {

            val url = URL(imageUrl)
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream())

        } catch (e1: IOException) {
            LogL.e(TAG,"downloadImage:"+Tool.FormatStackTrace(e1))
        }

        return bitmap
    }

}