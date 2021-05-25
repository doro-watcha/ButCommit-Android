package com.goddoro.butcommit.utils.component

import android.content.Context
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.goddoro.butcommit.databinding.DialogInformationBinding
import com.goddoro.butcommit.databinding.DialogTextDoubleBinding
import com.goddoro.butcommit.utils.AutoClearedValue
import com.goddoro.butcommit.utils.ScreenUtil
import com.goddoro.butcommit.utils.setOnDebounceClickListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject
import kotlin.math.roundToInt

fun AppCompatActivity.showInformationDialog(
    username: String

) {
    InformationDialog.show(
        supportFragmentManager, username
    )
}

class InformationDialog(

    private val username : String

) : DialogFragment() {

    companion object {
        fun show(
            fm: FragmentManager,
            username : String
        ) {
            val dialog = InformationDialog(username)
            dialog.show(fm, dialog.tag)
        }
    }


    private val screenUtil: ScreenUtil by inject()

    private val TAG = InformationDialog::class.java.simpleName

    /**
     * Binding Instance
     */
    private var mBinding: DialogInformationBinding by AutoClearedValue()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DialogInformationBinding.inflate(inflater, container, false)

        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }


        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mBinding.lifecycleOwner = viewLifecycleOwner

        initView()
    }

    private fun initView() {
        mBinding.apply {

            txtTitle.text = "${username}님 환영합니다!"


            btnConfirm.setOnDebounceClickListener {
                dismiss()
            }
        }
    }

    /**
     * For Size
     */
    override fun onResume() {
        super.onResume()

        val wm = context!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val point = Point()
        wm.defaultDisplay.getSize(point)
        val width = (point.x * 0.9f).roundToInt()
        val height = screenUtil.toPixel(160).roundToInt()
        dialog?.window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
    }


}