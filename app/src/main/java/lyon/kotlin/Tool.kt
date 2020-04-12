package lyon.kotlin

import android.util.Log
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter
import java.io.Writer

object Tool{
    val TAG = "Tool"
    fun FormatStackTrace(throwable: Throwable?): String? {
        if (throwable == null) return ""
        var rtn = throwable.stackTrace.toString()
        try {
            val writer: Writer = StringWriter()
            val printWriter = PrintWriter(writer)
            throwable.printStackTrace(printWriter)
            printWriter.flush()
            writer.flush()
            rtn = writer.toString()
            printWriter.close()
            writer.close()
        } catch (e: IOException) {
            Log.e(TAG,"FormatStackTrace "+e)
        } catch (ex: Exception) {
            Log.e(TAG,"FormatStackTrace "+ex)
        }
        return rtn
    }

    fun findMax(lastPositions:IntArray):Int {
        var max = lastPositions[0]
        for ( i in lastPositions.indices) {
            val value = lastPositions[i]
            if (value > max) {
                max = value
            }
        }
        return max;
    }
}