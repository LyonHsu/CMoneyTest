package lyon.kotlin
import android.util.Log
object LogL{
    val isDebug=true
    fun e(TAG:String, msg:String){
        if(isDebug)
            android.util.Log.e(TAG,msg)
    }

    fun d(TAG:String, msg:String){
        if(isDebug)
            android.util.Log.d(TAG,msg)
    }

    fun w(TAG:String, msg:String){
        if(isDebug)
            android.util.Log.w(TAG,msg)
    }

    fun i(TAG:String, msg:String){
        if(isDebug)
            android.util.Log.i(TAG,msg)
    }
}