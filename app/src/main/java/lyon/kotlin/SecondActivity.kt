package lyon.kotlin

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_second.*
import org.json.JSONArray
import androidx.recyclerview.widget.LinearLayoutManager
import lyon.kotlin.SecondModel.ViewModel


class SecondActivity: AppCompatActivity() {
    val TAG = "SecondActivity"
    lateinit var context: Context
    lateinit var ryvAdapter:RyvAdapter
    var jsonArray= JSONArray()
    var lastItemPosition =0;
    var totalCount =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this
        setContentView(R.layout.activity_second)

        val mLayoutManager = LinearLayoutManager(this)
        ryvAdapter = RyvAdapter(this, jsonArray)
        var context = this
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.adapter = ryvAdapter

        ViewModel(this)

    }

}