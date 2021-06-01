package com.androiddevs.runningappyt.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.androiddevs.runningappyt.R
import com.androiddevs.runningappyt.db.OrdersData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_add_order.*

class AddOrdersFragment: Fragment() {

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        database = FirebaseDatabase.getInstance()
        reference = database.getReference("/orders").push()

        btn_send.setOnClickListener{
            sendData()
            findNavController().navigateUp()
        }
    }

    /*class DatePickerFragment : DialogFragment(), OnDateSetListener {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            val c = Calendar.getInstance()
            val year = c[Calendar.YEAR]
            val month = c[Calendar.MONTH]
            val day = c[Calendar.DAY_OF_MONTH]

// Create a new instance of DatePickerDialog and return it
            return DatePickerDialog(getActivity(), this, year, month, day)
        }

        override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
            val c = Calendar.getInstance()
            c[year, month] = day
            val sdf = SimpleDateFormat("yyyy-MM-dd")
            val formattedDate = sdf.format(c.time)
        }
    }*/

    private fun sendData() {

        var start_point = from_ord.text.toString().trim()
        var finish_point = upto_ord.text.toString().trim()
        var product = prod_ord.text.toString().trim()
        var number = number_ord.text.toString().trim()
        var deadline = limit_ord.text.toString().trim()
        var name = name_order_ord.text.toString().trim()
        var distance = distance_ord.text.toString().trim()
        var price = price_ord.text.toString().trim()

        if (start_point.isNotEmpty() && finish_point.isNotEmpty() && product.isNotEmpty() && number.isNotEmpty() && deadline.isNotEmpty() && name.isNotEmpty() && distance.isNotEmpty() && price.isNotEmpty() )
        {
            var model = OrdersData(
                start_point,
                finish_point,
                product,
                number,
                deadline,
                name,
                distance,
                price
            )
            reference.setValue(model)
            from_ord.setText("")
            upto_ord.setText("")
            prod_ord.setText("")
            number_ord.setText("")
            limit_ord.setText("")
            name_order_ord.setText("")
            distance_ord.setText("")
            price_ord.setText("")

        } else{
            Toast.makeText(activity, "All field required", Toast.LENGTH_LONG).show()
        }
    }
}