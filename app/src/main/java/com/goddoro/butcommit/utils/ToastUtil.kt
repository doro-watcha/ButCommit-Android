package com.goddoro.butcommit.utils

import android.content.Context
import android.content.res.Resources
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.goddoro.butcommit.R
import com.goddoro.butcommit.databinding.ViewCustomToastBinding

class ToastUtil ( val context : Context) {

    fun makeToast( message: String, position : Int? = Gravity.BOTTOM): Toast {
        val inflater = LayoutInflater.from(context)
        val binding: ViewCustomToastBinding =
            DataBindingUtil.inflate(inflater, R.layout.view_custom_toast, null, false)

        binding.tvSample.text = message

        return Toast(context).apply {
            setGravity(position ?: Gravity.BOTTOM, 0, 16.toPx())
            duration = Toast.LENGTH_SHORT
            view = binding.root
        }
    }

    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
}