package lyon.kotlin.Model

import android.util.Log
import lyon.kotlin.Tool.LogL
import org.json.JSONArray

val url ="https://jsonplaceholder.typicode.com/photos"
class ViewModel{
    var getRespone:GetRespone?=null;
    interface GetRespone {
        fun Respone(jsonArray: JSONArray)
    }
    fun onGetResponse(getRespone: GetRespone) {
        this.getRespone = getRespone
    }
    var getSize:GetSize?=null;
    interface GetSize {
        fun Size(size: Int)
    }
    fun onGetSize(getSize:GetSize) {
        this.getSize = getSize
    }
    init {
        object : TransTaskMode(){
            override fun parseJSON(jsonArray: JSONArray) {
                val len =jsonArray.length()?:0
                Log.d(TAG,"TransTaskMode len:"+len)
                if(getRespone!=null){
                    getRespone!!.Respone(jsonArray)
                }
            }

            override fun getPackageSize(size: Int) {
                LogL.d(TAG,"Size:"+size)
                if(getSize!=null){
                    getSize!!.Size(size)
                }
            }
        }.execute(url)
    }

}