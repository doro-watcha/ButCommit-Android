package com.goddoro.butcommit.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.inputmethod.InputMethodManager
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.File
import kotlin.reflect.KClass


@Suppress("UNCHECKED_CAST")
fun HashMap<String, out Any?>.filterValueNotNull(): HashMap<String, Any> {
    return this.filterNot { it.value == null } as HashMap<String, Any>
}

fun String.toUri(): Uri = Uri.parse(this)
fun String.toFile(): File = File(this)

infix fun Fragment.getString(@StringRes id: Int): String = requireContext().resources.getString(id)
infix fun Fragment.getColor(@ColorRes id: Int): Int =
    ResourcesCompat.getColor(resources, id, this.activity?.theme)

infix fun AppCompatActivity.getString(@StringRes id: Int): String = resources.getString(id)
infix fun AppCompatActivity.getColor(@ColorRes id: Int): Int =
    ResourcesCompat.getColor(resources, id, this.theme)

fun AppCompatActivity.hideKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
}

fun Fragment.hideKeyboard() {
    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
}

fun BottomSheetDialogFragment.hideKeyboard() {
    val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
}

inline fun <reified T> AppCompatActivity.startActivity(clazz: KClass<out T>, enterAnim : Int? = null, exitAnim : Int? = null, flags: Int? = null) where T : AppCompatActivity {
    val intent = Intent(this, clazz.java).apply {
        flags?.let { this.flags = it }
    }

    startActivity(intent)

    if ( exitAnim != null && enterAnim != null) {
        this.overridePendingTransition(enterAnim, exitAnim)
    }
}

inline fun <reified T> Fragment.startActivity(clazz: KClass<out T>, flags: Int? = null) where T : AppCompatActivity {
    val intent = Intent(requireActivity(), clazz.java).apply {
        flags?.let { this.flags = it }
    }
    startActivity(intent)
}
open class Once<out T>(private val content: T) {

    private var hasBeenHandled = false

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}

fun <T> LiveData<Once<T>>.observeOnce(lifecycle: LifecycleOwner, listener: (T) -> Unit) {
    this.observe(lifecycle, Observer {
        it?.getContentIfNotHandled()?.let {
            listener(it)
        }
    })
}