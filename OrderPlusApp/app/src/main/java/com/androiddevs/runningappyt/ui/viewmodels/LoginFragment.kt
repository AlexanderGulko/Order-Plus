package com.androiddevs.runningappyt.ui.viewmodels

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.androiddevs.runningappyt.R
import com.androiddevs.runningappyt.ui.MainActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {

    var auth: FirebaseAuth? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        forgot_password.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_resetPasswordActivity)
        }
        btn_login.setOnClickListener {
            val txt_email = email?.text.toString()
            val txt_password = password?.text.toString()
            if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
                Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
            } else {
                auth!!.signInWithEmailAndPassword(txt_email, txt_password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            requireActivity().startActivity(Intent(activity,MainActivity::class.java))
                        } else {
                            Toast.makeText(requireContext(), "Authentication failed!", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }
    }
}