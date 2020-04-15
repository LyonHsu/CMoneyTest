package lyon.kotlin

import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_second.*
import lyon.kotlin.Model.ViewModel
import lyon.kotlin.Ui.AlertProgressDialog
import org.json.JSONArray
import org.json.JSONObject

val JSON = "json"
class SecondActivity: AppCompatActivity() {
    val TAG = "SecondActivity"
    lateinit var context: Context
    lateinit var ryvAdapter:RyvAdapter
    var jsonArray= JSONArray()
    var lastItemPosition =0;
    var totalCount =0
    lateinit var progressDialog: AlertProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        setContentView(R.layout.activity_second)
        progressDialog =  AlertProgressDialog(this)
        progressDialog.setOnDismissListener(object :DialogInterface.OnDismissListener{
            override fun onDismiss(dialog: DialogInterface?) {
                if(ryvAdapter.itemCount<1){
                    finish()
                }
            }
        })
        val mLayoutManager = LinearLayoutManager(this)
        ryvAdapter = RyvAdapter(this, jsonArray)
        var context = this
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.adapter = ryvAdapter
        ryvAdapter.setOnItemClickListener(object: RyvAdapter.ItemClick{
            override fun onCLick(v: View, position: Int,jsonObject: JSONObject) {
                val intent = Intent(context,ThirdActivity::class.java)
                intent.putExtra(JSON,jsonObject.toString())
                startActivity((intent))
            }
        })
        progressDialog.show()
        val viewModel=ViewModel()
        viewModel.onGetResponse(object :ViewModel.GetRespone{
            override fun Respone(jsonArray: JSONArray) {
                ryvAdapter.setData(jsonArray)
                progressDialog.setSize(0)
                progressDialog.dismiss()
            }

        })
        viewModel.onGetSize(object :ViewModel.GetSize{
            override fun Size(size: Int) {
                if(progressDialog!=null){
                    progressDialog.setSize(size)
                }
            }
        })

    }
}