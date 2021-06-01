package com.androiddevs.runningappyt.ui.viewmodels

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.androiddevs.runningappyt.R
import com.androiddevs.runningappyt.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.fragment_register.*
import java.util.*


class RegisterFragment : Fragment() {
    var auth: FirebaseAuth? = null
    var reference: DatabaseReference? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        btn_register.setOnClickListener{
                val txtUsername = username.text.toString()
                val txt_email = email.text.toString()
                val txt_password = password.text.toString()
                if (TextUtils.isEmpty(txtUsername) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                    Toast.makeText(
                        requireContext(),
                        "All fields are required",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                } else if (txt_password.length < 6) {
                    Toast.makeText(
                        requireContext(),
                        "password must be at least 8 characters",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    register(txtUsername, txt_email, txt_password)
                }
            }
        }

    private fun register(username: String, email: String, password: String) {
        auth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth!!.currentUser!!
                    val userid = firebaseUser.uid
                    reference = FirebaseDatabase.getInstance().getReference("Users").child(userid)
                    val hashMap = HashMap<String, String>()
                    hashMap["id"] = userid
                    hashMap["username"] = username
                    reference!!.setValue(hashMap).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            requireActivity().startActivity(Intent(activity, MainActivity::class.java))
                        }
                    }
                } else {
                    Toast.makeText(
                        requireContext(),
                        "You can't register with this email or password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}