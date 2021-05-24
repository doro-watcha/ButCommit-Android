package com.goddoro.butcommit.presentation.signIn

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.goddoro.butcommit.data.api.UnWrappingDataException
import com.goddoro.butcommit.databinding.ActivitySignInBinding
import com.goddoro.butcommit.utils.AppPreference
import com.goddoro.butcommit.utils.Broadcast.onLoginCompleted
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInActivity : AppCompatActivity() {

    private val TAG = SignInActivity::class.java.simpleName

    private lateinit var mBinding : ActivitySignInBinding

    private val mViewModel : SignInViewModel by viewModel()

    private val appPreference : AppPreference by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySignInBinding.inflate(LayoutInflater.from(this))
        mBinding.vm = mViewModel
        mBinding.lifecycleOwner = this
        setContentView(mBinding.root)

        observeViewModel()

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor =  (ContextCompat.getColor(this, R.color.white))
    }

    private fun observeViewModel() {

        mViewModel.apply {

            onRegisterCompleted.observe(this@SignInActivity, Observer {
                Toast.makeText(this@SignInActivity,"로그인에 성공하였습니다",Toast.LENGTH_SHORT).show()
                if ( it == true ) {
                    appPreference.githubId = githubId.value ?: ""
                    appPreference.startDate = "2021-05-19"
                    onLoginCompleted.onNext(Unit)
                    finish()
                }
            })

            errorInvoked.observe(this@SignInActivity, Observer {
                Log.d(TAG, it.message.toString())

                if ( it is UnWrappingDataException) {

                    when ( it.errorCode) {

                        402 -> {
                            Toast.makeText(this@SignInActivity,"잘못된 아이디입니다", Toast.LENGTH_SHORT).show()
                        }
                        102 -> {
                            Toast.makeText(this@SignInActivity,"아이디를 입력해주세요",Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            Toast.makeText(this@SignInActivity,it.message,Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }
    }
}