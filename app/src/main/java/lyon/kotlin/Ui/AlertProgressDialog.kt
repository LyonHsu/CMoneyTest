package lyon.kotlin.Ui

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.widget.TextView
import lyon.kotlin.R

class AlertProgressDialog(context: Context) : Dialog(context, R.style.CustomAlertDialogType) {
    val TAG = AlertProgressDialog::class.java.simpleName
    var textView:TextView?=null
    init {
        setContentView(R.layout.alert_progress)
        textView = findViewById(R.id.textView)
        textView!!.text=context.getString(R.string.linking)
    }

    fun setSize(size:Int){
        if(textView==null){
            textView = findViewById(R.id.textView)
        }
        textView!!.text=size.toString()
    }
}