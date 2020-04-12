package lyon.kotlin.Model

import android.util.Log
import lyon.kotlin.SecondActivity
import org.json.JSONArray

class ViewModel(var activity:SecondActivity)   {

    init {
        object : TransTaskMode(){
            override fun parseJSON(jsonArray: JSONArray) {
                val len =jsonArray.length()?:0
                Log.d(TAG,"TransTaskMode len:"+len)
                if(len>0){
                    activity.jsonArray=jsonArray
                    activity.ryvAdapter.setData(jsonArray)
                }
            }
        }.execute("https://jsonplaceholder.typicode.com/photos")
    }

}