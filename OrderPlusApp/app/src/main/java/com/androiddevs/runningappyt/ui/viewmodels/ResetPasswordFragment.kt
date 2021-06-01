package com.androiddevs.runningappyt.ui.viewmodels

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.androiddevs.runningappyt.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_reset_password.*


class ResetPasswordFragment : Fragment() {
    var firebaseAuth: FirebaseAuth? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reset_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseAuth = FirebaseAuth.getInstance()

        btn_reset.setOnClickListener{
                val email = send_email.getText().toString()
                if (email == "") {
                    Toast.makeText(requireContext(), "All fields are required!", Toast.LENGTH_SHORT).show()
                } else {
                    firebaseAuth!!.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(requireContext(), "Please check your Email", Toast.LENGTH_SHORT).show()
                            startActivity(
                                Intent(
                                    requireContext(),
                                    LoginFragment::class.java
                                )
                            )
                        } else {
                            val error = task.exception!!.message
                            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
    }
}