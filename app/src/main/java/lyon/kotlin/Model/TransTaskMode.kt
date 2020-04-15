package lyon.kotlin.Model

import android.os.AsyncTask
import lyon.kotlin.Tool.LogL
import lyon.kotlin.Tool.Tool
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL

abstract class  TransTaskMode : AsyncTask<String, Void, String>() {
    val TAG = "TransTaskMode"
    val isDebug=false
    override fun doInBackground(vararg params: String?): String {
        val sb = StringBuilder()
        try {
            val url = URL(params[0])
            var inputStreamReader = InputStreamReader(url.openStream())

            LogL.d(TAG,"inputStreamReader:"+inputStreamReader.hashCode())
            val bufferedReader = BufferedReader(inputStreamReader)
            var line = bufferedReader.readLine()
            LogL.d(TAG,"bufferedReader size:"+bufferedReader)
            var total = 0
            while (line != null) {
                if(isDebug)
                    LogL.d(TAG + "HTTP", line)
                sb.append(line)
                getPackageSize(sb.length)
                line = bufferedReader.readLine()

            }
        } catch (e: MalformedURLException) {
            LogL.e(TAG, Tool.FormatStackTrace(e) ?: "")
        } catch (e: IOException) {
            LogL.e(TAG, Tool.FormatStackTrace(e) ?: "")
        }

        return sb.toString()
    }

    override fun onPostExecute(s: String) {
        super.onPostExecute(s)
        if(isDebug)
            LogL.d("JSON", s)
        try {
            var jsonArray: JSONArray = JSONArray(s)
            parseJSON(jsonArray)
        }catch (e:JSONException){
            LogL.e(TAG,"onPostExecute:"+ Tool.FormatStackTrace(e))
        }
    }

    abstract fun parseJSON(jsonArray: JSONArray)

    abstract fun getPackageSize( size:Int)

}