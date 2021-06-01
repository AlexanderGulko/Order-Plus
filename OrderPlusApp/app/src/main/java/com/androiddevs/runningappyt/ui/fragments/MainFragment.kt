package com.androiddevs.runningappyt.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.res.stringArrayResource
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.runningappyt.R
import com.androiddevs.runningappyt.db.OrdersData
import com.androiddevs.runningappyt.other.Constants.REQUEST_CODE_LOCATION_PERMISSION
import com.androiddevs.runningappyt.other.TrackingUtility
import com.google.firebase.database.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.fragment_run.*
import kotlinx.android.synthetic.main.orders_item.view.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


@AndroidEntryPoint
class MainFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private val ordersAdapter = OrdersAdapter()

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_run, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermissions()

        database = FirebaseDatabase.getInstance()
        reference = database.getReference("/orders")
        recyclerOrdersCards.adapter = ordersAdapter

        getData()

        add_order.setOnClickListener {
            findNavController().navigate(R.id.action_runFragment_to_addOrdersFragment)
        }
    }

    private fun getData() {
        reference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, previousChildName: String?) {
                val orders = p0.getValue(OrdersData::class.java)
                ordersAdapter.addOrders(orders!!)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            @SuppressLint("LogNotTimber")
            override fun onCancelled(p0: DatabaseError) {
                Log.e("cancel", p0.toString())
            }
        })
    }

    private fun requestPermissions() {
        if (TrackingUtility.hasLocationPermissions(requireContext())) {
            return
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app.",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app.",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }



    /*private fun searchUsers(s: String) {
        val fuser = FirebaseAuth.getInstance().currentUser
        val query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("search")
            .startAt(s)
            .endAt(s + "\uf8ff")
        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                mUsers.clear()
                for (snapshot in dataSnapshot.children) {
                    val user: User = snapshot.getValue(User::class.java)!!
                    assert(fuser != null)
                    if (!user.getId().equals(fuser!!.uid)) {
                        mUsers.add(user)
                    }
                }
                userAdapter = UserAdapter(context, mUsers, false)
                recyclerView.setAdapter(userAdapter)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun readUsers() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val reference = FirebaseDatabase.getInstance().getReference("Users")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (search_users.text.toString() == "") {
                    mUsers.clear()
                    for (snapshot in dataSnapshot.children) {
                        val user: User = snapshot.getValue(User::class.java)!!
                        assert(firebaseUser != null)
                        if (!user.getId().equals(firebaseUser!!.uid)) {
                            mUsers.add(user)
                        }
                    }
                    userAdapter = UserAdapter(context, mUsers, false)
                    recyclerView.setAdapter(userAdapter)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }*/
}

class OrdersAdapter : RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder>() {

    private val ordersList = arrayListOf<OrdersData>()

    class OrdersViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun bind(ordersData: OrdersData) {

            with(view) {
                tv_from_ord.text = ordersData.start_point
                tv_upto_ord.text = ordersData.finish_point
                tv_prod_ord.text = ordersData.product
                tv_number_ord.text = ordersData.number
                tv_limit_ord.text = ordersData.deadline
                tv_name_order_ord.text = ordersData.name
                tv_distance_ord.text = ordersData.distance
                tv_price_ord.text = ordersData.price

                fab.setOnClickListener {
                    findNavController().navigate(R.id.action_runFragment_to_trackingFragment)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {

        return OrdersViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.orders_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        holder.bind(ordersList[position])
    }

    fun addOrders(ordersData: OrdersData) {
        ordersList.add(0, ordersData)
        notifyItemInserted(0)
    }

    /*fun setData(items: MutableList<OrdersData>) {
        orders.clear()
        orders.addAll(items)
    }*/

    override fun getItemCount(): Int {
        return ordersList.size
    }

    fun SortByPrice(ordersData: OrdersData) {
        //if (stringArrayResource(). ) {
            ordersList.sortedBy { it.price.first() }
        //}
    }
}






