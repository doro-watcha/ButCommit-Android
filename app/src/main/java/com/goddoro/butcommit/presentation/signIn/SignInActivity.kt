package com.goddoro.butcommit.presentation.signIn

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
import com.goddoro.butcommit.R
import com.goddoro.butcommit.data.api.UnWrappingDataException
import com.goddoro.butcommit.databinding.ActivitySignInBinding
import com.goddoro.butcommit.utils.AppPreference
import com.goddoro.butcommit.utils.Broadcast.registerCompleteBroadcast
import com.goddoro.butcommit.utils.DateUtil
import com.goddoro.butcommit.utils.ToastUtil
import com.goddoro.butcommit.utils.component.TextDoubleDialog
import com.goddoro.butcommit.utils.component.showInformationDialog
import com.goddoro.butcommit.utils.observeOnce
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInActivity : AppCompatActivity() {

    private val TAG = SignInActivity::class.java.simpleName

    private lateinit var mBinding : ActivitySignInBinding

    private val mViewModel : SignInViewModel by viewModel()

    private val appPreference : AppPreference by inject()

    private val dateUtil : DateUtil by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivitySignInBinding.inflate(LayoutInflater.from(this))
        mBinding.vm = mViewModel
        mBinding.lifecycleOwner = this
        setContentView(mBinding.root)

        observeViewModel()

        window.statusBarColor =  (ContextCompat.getColor(this, R.color.white))
    }

    private fun observeViewModel() {

        mViewModel.apply {

            clickLoginButton.observeOnce(this@SignInActivity){
                TextDoubleDialog.show(
                    fm = supportFragmentManager,
                    title = "경고",
                    body = "${appPreference.githubId}의 커밋 기록이 초괴화 됩니다.\ngithub 계정을 변경하시겠습니까?",
                    onPositive = {
                        update()
                    },
                    onNegative = {

                    }
                )
            }

            onRegisterCompleted.observe(this@SignInActivity, Observer {

                if ( it == true ) {
                    appPreference.githubId = githubId.value ?: ""
                    appPreference.startDate = dateUtil.getToday()
//                    appPreference.startDate = "2020-05-20"
                    showInformationDialog(appPreference.githubId)
                    registerCompleteBroadcast.onNext(appPreference.githubId)

                    /**
                     * update후 확인 버튼의 동기화를 위해서 값 재설정
                     */
                    githubId.value = appPreference.githubId

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


    override fun onBackPressed() {
        super.onBackPressed()

        this.overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right)
    }
}