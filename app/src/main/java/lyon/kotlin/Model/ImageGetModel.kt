package lyon.kotlin.Model

import android.content.Context
import android.graphics.Bitmap
import android.os.AsyncTask
import android.graphics.BitmapFactory
import android.os.StrictMode
import lyon.kotlin.Tool.LogL
import lyon.kotlin.R
import lyon.kotlin.Tool.Tool
import java.net.URL
import java.net.HttpURLConnection
import java.io.*
import java.net.MalformedURLException
import java.security.KeyStore
import javax.net.ssl.*


abstract class ImageGetModel(val context: Context) : AsyncTask<String, Void, Bitmap>(){
    val TAG = "ImageGetModel"
    val isDebug=true
    val timeOut = 1*1000
    lateinit var imageUrl:String
    override fun doInBackground(vararg params: String?): Bitmap? {
        try {
            val url = URL(params[0])
            if(isDebug)
                LogL.d(TAG,"bitmap url:"+url)
            var bitmap: Bitmap? = null
            imageUrl = params[0].toString()
//            imageUrl = "https://cdn.pixabay.com/photo/2017/09/14/11/07/water-2748640__340.png"
            imageUrl = "http://via.placeholder.com/150/92c952"
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
            LogL.e(TAG, "image get error:"+ Tool.FormatStackTrace(e)+"")
            return null
        }
    }

    override fun onPostExecute(bitmap: Bitmap?) {
        if(isDebug)
            LogL.d(TAG,"bitmap:$bitmap")
        parseBitmap(bitmap,imageUrl)
    }

    abstract fun parseBitmap(bitmap: Bitmap?,url:String)

    fun downloadImage( imageUrl: String): Bitmap? {
        var bitmap: Bitmap? = null
        try {

            val url = URL(imageUrl)
            val conn = url.openConnection() as HttpURLConnection
            conn.setDoInput(true);
            conn.setConnectTimeout(timeOut);
            conn.setReadTimeout(timeOut);
            conn.connect();
            val stream = conn.getInputStream()
            val streamm = ByteArrayOutputStream()
            val byteArray = streamm.toByteArray()
            bitmap = BitmapFactory.decodeStream(stream)

        } catch (e1: IOException) {
            LogL.e(TAG,"downloadImage:"+ Tool.FormatStackTrace(e1))
        }

        return bitmap
    }
    protected fun downLoadImage(strurl: String): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val url = URL(strurl)
            if(isDebug) {
                val policy = StrictMode.ThreadPolicy.Builder().detectNetwork().penaltyDialog().penaltyLog().build()
                StrictMode.setThreadPolicy(policy)
            }
            val conn = url.openConnection() as HttpsURLConnection
            //val certificates = context.getAssets().open("cert.crt")
            val resourceStream = context.resources.openRawResource(R.raw.cert)
            val keyStoreType = KeyStore.getDefaultType()
            val keyStore = KeyStore.getInstance(keyStoreType)
            keyStore.load(resourceStream, null)
            val trustManagerAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
            val trustManagerFactory = TrustManagerFactory.getInstance(trustManagerAlgorithm)
            trustManagerFactory.init(keyStore)
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustManagerFactory.trustManagers, null)

//            SSL.ignoreVerifyHttpsHostName()
//            val socketFactory=SSL.ignoreVerifyHttpsTrustManager()//certificates)
            conn.setSSLSocketFactory(sslContext.socketFactory)
            conn.setHostnameVerifier(object :HostnameVerifier {
                override fun verify(hostname: String?, session: SSLSession?): Boolean {
                    LogL.d(TAG,"HostnameVerifier:"+hostname+" session:"+session)
                    return true
                }
            })
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setConnectTimeout(timeOut);
            conn.setReadTimeout(timeOut);

            var status = conn.getResponseCode()
            if (status == HttpURLConnection.HTTP_OK) {
                var inputStream = conn.inputStream as InputStream
                val buffer = ByteArray(1024)
                var bytesRead: Int
                val output = ByteArrayOutputStream()
                bytesRead = inputStream.read(buffer)
                while (bytesRead != -1) {
                    output.write(buffer, 0, bytesRead)
                    bytesRead = inputStream.read(buffer)
                }
                val byte = output.toByteArray()
                bitmap = BitmapFactory.decodeByteArray(byte, 0, byte.size);

            }else if(status == HttpURLConnection.HTTP_GONE){
                LogL.e(TAG,"getResponseCode 410 Gone:"+conn.getResponseCode())
            }
            else{
                //LogL.e(TAG,"getResponseCode:"+conn.getResponseCode()+","+conn.errorStream.read())
            }
            return bitmap
        } catch (e: MalformedURLException) {
            LogL.e(TAG,"downloadImage:"+ Tool.FormatStackTrace(e))
            return null
        } catch (e: IOException) {
            LogL.e(TAG,"downloadImage:"+ Tool.FormatStackTrace(e))
            return null
        }
    }



}