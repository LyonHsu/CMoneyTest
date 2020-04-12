package lyon.kotlin.Model

import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import android.graphics.BitmapFactory
import lyon.kotlin.LogL
import lyon.kotlin.Tool
import java.net.URL
import java.net.HttpURLConnection
import java.io.*
import java.net.MalformedURLException
import javax.net.ssl.HttpsURLConnection
import lyon.kotlin.SSL
import javax.net.ssl.X509TrustManager
import javax.security.cert.CertificateException


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
            if(isDebug)
                LogL.d(TAG,"bitmap imageUrl:"+imageUrl)
            bitmap = imageUrl?.let{
                LogL.d(TAG,"bitmap it:"+it)
                if(it.contains("http:"))
                    downloadImage(it)
                else
                    downLoadImage(it)
            }

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
            val conn = url.openConnection() as HttpURLConnection
            conn.setDoInput(true);
            conn.setConnectTimeout(1000);
            conn.setReadTimeout(1000);
            conn.connect();
            val stream = conn.getInputStream()
            val streamm = ByteArrayOutputStream()
            val byteArray = streamm.toByteArray()
            bitmap = BitmapFactory.decodeStream(stream)

        } catch (e1: IOException) {
            LogL.e(TAG,"downloadImage:"+Tool.FormatStackTrace(e1))
        }

        return bitmap
    }
    protected fun downLoadImage(strurl: String): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val url = URL( strurl)
            val conn = url.openConnection() as HttpsURLConnection
            SSL.ignoreVerifyHttpsHostName()
            val sc=SSL.ignoreVerifyHttpsTrustManager()
            conn.setSSLSocketFactory(sc.socketFactory)
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setConnectTimeout(5*1000);
            conn.setReadTimeout(5*1000);
            conn.connect();
            if (conn.getResponseCode() == 200) {
                var inputStream = conn.inputStream as InputStream
                val buffer = ByteArray(8192)
                var bytesRead: Int
                val output = ByteArrayOutputStream()
                bytesRead = inputStream.read(buffer)
                while (bytesRead != -1) {
                    output.write(buffer, 0, bytesRead)
                    bytesRead = inputStream.read(buffer)
                }
                val byte = output.toByteArray()
                bitmap = BitmapFactory.decodeByteArray(byte, 0, byte.size);

            }else{
                LogL.e(TAG,"getResponseCode:"+conn.getResponseCode())
            }
            conn.disconnect()
            return bitmap
        } catch (e: MalformedURLException) {
            LogL.e(TAG,"downloadImage:"+Tool.FormatStackTrace(e))
            return null
        } catch (e: IOException) {
            LogL.e(TAG,"downloadImage:"+Tool.FormatStackTrace(e))
            return null
        }
    }



}