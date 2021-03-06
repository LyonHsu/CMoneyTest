package lyon.kotlin

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import lyon.kotlin.Model.ImageGetModel
import lyon.kotlin.Tool.LogL
import lyon.kotlin.Tool.Tool
import org.json.JSONArray
import org.json.JSONObject


class RyvAdapter(private var context: Context, private var jsonArray: JSONArray) :
    RecyclerView.Adapter<RyvAdapter.ViewHolder>(){
    val TAG = "RyvAdapter"

    lateinit var holder: ViewHolder
    var itemClick:ItemClick?=null;

    interface ItemClick {
        fun onCLick(v: View, position: Int, jsonObject:JSONObject)
    }

    fun setOnItemClickListener(itemClick: ItemClick) {
        this.itemClick = itemClick
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view= LayoutInflater.from(parent.getContext()).inflate(R.layout.second_cell, parent, false);
        val viewHolder = ViewHolder(view,viewType)

        this.holder=viewHolder
        return holder
    }

    override fun getItemCount(): Int {
        return  jsonArray?.length() ?:0
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        jsonArray?:return
        holder?.bindModel(position)
        holder.itemView.setOnClickListener {
            if(itemClick!=null){
                val jsonObject = jsonArray.optJSONObject(position)
                itemClick!!.onCLick(holder.itemView,position,jsonObject)
            }
        }
    }

    fun setData(jsonArray:JSONArray){
        this.jsonArray=jsonArray
        notifyDataSetChanged()
    }


    inner class ViewHolder(itemView: View, var viewType: Int) : RecyclerView.ViewHolder(itemView) {
        lateinit var idTextView: TextView
        lateinit var titleTextView: TextView
        lateinit var urlView: TextView
        lateinit var imageView: ImageView
        lateinit var webView:WebView
        fun bindModel(position:Int) {
            imageView = itemView.findViewById(R.id.imageView)
            idTextView = itemView.findViewById(R.id.id_txt)
            titleTextView = itemView.findViewById(R.id.title_txt)
            urlView = itemView.findViewById(R.id.thumbnailUrl_txt)
            webView = itemView.findViewById(R.id.webview)
            val mWebSettings: WebSettings = webView.getSettings()
            mWebSettings.useWideViewPort = true
            mWebSettings.loadWithOverviewMode = true
            webView.visibility=View.GONE
            var title = jsonArray.getJSONObject(position).optString("title")
            var id = jsonArray.getJSONObject(position).optString("id")
            var url = jsonArray.getJSONObject(position).optString("thumbnailUrl")
            titleTextView.text="title:"+title
            idTextView.text="id:"+id
            urlView.text="thumbnailUrl:"+url
            imageView.tag=url
            object : ImageGetModel(context){
                override fun parseBitmap(bitmap: Bitmap?,imageUrl:String) {
                    try {
                            if (bitmap != null)
                                imageView.setImageBitmap(bitmap)
                            else {
                                imageView.visibility = View.GONE
                                webView.visibility = View.VISIBLE
                                webView.loadUrl(url)
                            }
                    }catch (e:NullPointerException){
                        LogL.e(TAG,"ImageGetModel:"+ Tool.FormatStackTrace(e))
                    }
                }
            }.execute(url)
//

        }
    }
}