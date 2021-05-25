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
import com.goddoro.butcommit.databinding.DialogTextDoubleBinding
import com.goddoro.butcommit.utils.AutoClearedValue
import com.goddoro.butcommit.utils.ScreenUtil
import com.goddoro.butcommit.utils.setOnDebounceClickListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.ext.android.inject
import kotlin.math.roundToInt

fun AppCompatActivity.showTextDoubleDialog(
    title: String,
    body: String,
    onPositive: (() -> Unit),
    onNegative: (() -> Unit)
) {
    TextDoubleDialog.show(
        supportFragmentManager, title, body, onPositive, onNegative
    )
}

fun Fragment.showTextDoubleDialog(
    title: String,
    body: String,
    onPositive: () -> Unit,
    onNegative: () -> Unit
) {
    TextDoubleDialog.show(
        childFragmentManager, title, body, onPositive, onNegative
    )
}

fun DialogFragment.showTextDoubleDialog(
    title: String,
    body: String,
    onPositive: () -> Unit,
    onNegative: () -> Unit
) {
    TextDoubleDialog.show(
        childFragmentManager, title, body, onPositive, onNegative
    )
}

fun BottomSheetDialogFragment.showTextDoubleDialog(
    title: String,
    body: String,
    onPositive: () -> Unit,
    onNegative: () -> Unit
) {
    TextDoubleDialog.show(
        childFragmentManager, title, body, onPositive, onNegative
    )
}

class TextDoubleDialog(
    private val _title: String,
    private val _body: String,
    private val onPositive: () -> Unit,
    private val onNegative: () -> Unit

) : DialogFragment() {

    class Builder(private val fm: FragmentManager) {

        private var onPositive: (() -> Unit)? = null
        private var onNegative: (() -> Unit)? = null

        fun setOnPositive(onPositive: () -> Unit) = apply { this.onPositive = onPositive }
        fun setOnNegative(onNegative: () -> Unit) = apply { this.onNegative = onNegative }
    }


    companion object {
        fun show(
            fm: FragmentManager,
            title: String,
            body: String,
            onPositive: () -> Unit,
            onNegative: () -> Unit
        ) {
            val dialog = TextDoubleDialog(title, body, onPositive, onNegative)
            dialog.show(fm, dialog.tag)
        }
    }


    private val screenUtil: ScreenUtil by inject()

    private val TAG = TextDoubleDialog::class.java.simpleName

    /**
     * Binding Instance
     */
    private var mBinding: DialogTextDoubleBinding by AutoClearedValue()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DialogTextDoubleBinding.inflate(inflater, container, false)

        if (dialog != null && dialog?.window != null) {
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            //dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        }


        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mBinding.lifecycleOwner = viewLifecycleOwner

        initView()
    }

    private fun initView() {
        mBinding.apply {

            title.text = _title
            body.text = _body

            Log.d(TAG, body.text.toString())

            txtConfirm.setOnDebounceClickListener {
                onPositive.invoke()
                dismiss()
            }
            txtCancel.setOnDebounceClickListener {
                onNegative.invoke()
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