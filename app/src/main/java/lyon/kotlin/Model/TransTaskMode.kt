package lyon.kotlin.Model

import android.os.AsyncTask
import lyon.kotlin.LogL
import lyon.kotlin.Tool
import org.json.JSONArray
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
            val `in` = BufferedReader(
                InputStreamReader(url.openStream())
            )
            var line = `in`.readLine()
            while (line != null) {
                if(isDebug)
                    LogL.d(TAG + "HTTP", line)
                sb.append(line)
                line = `in`.readLine()
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
        var jsonArray:JSONArray= JSONArray(s)
        parseJSON(jsonArray)
    }

    abstract fun parseJSON(jsonArray: JSONArray)

}