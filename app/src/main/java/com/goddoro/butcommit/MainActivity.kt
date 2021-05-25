package com.goddoro.butcommit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.goddoro.butcommit.databinding.ActivityMainBinding
import com.goddoro.butcommit.presentation.signIn.SignInActivity
import com.goddoro.butcommit.utils.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import io.reactivex.disposables.CompositeDisposable
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class MainActivity : AppCompatActivity() {

    private val TAG = MainActivity::class.java.simpleName

    private lateinit var mBinding: ActivityMainBinding

    private val mViewModel: MainViewModel by viewModel()

    private val compositeDisposable = CompositeDisposable()

    private val appPreference: AppPreference by inject()

    private val toastUtil : ToastUtil by inject()

    private val dateUtil : DateUtil by inject()

    private var diff = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(LayoutInflater.from(this))

        mBinding.lifecycleOwner = this
        mBinding.vm = mViewModel

        setContentView(mBinding.root)

        Log.d(TAG, appPreference.githubId + "님 환영합니다")
        Log.d(TAG, appPreference.startDate + "부터 시작하셨네요")

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor =  (ContextCompat.getColor(this,R.color.background_1))

        initView()
        initFirebaseSetting()
        observeViewModel()
        checkIsLogin()
        setupBroadcast()
        setupRecyclerView()
        setupSwipeRefreshLayout()



    }

    private fun initView() {

        var title : String = if ( appPreference.startDate != "") {

            diff = dateUtil.calculateDiff(appPreference.startDate, dateUtil.getToday())

            if ( diff == 0 ) {
                "오늘 처음 잔디를 심으셨네요"
            } else {
                "잔디를 ${diff+1}번 심으셨습니다."
            }

        } else {
            "커밋 쉬는중"
        }

        mBinding.txtTitle.text = title

    }

    private fun initFirebaseSetting() {

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                // Get new Instance ID token
                val newFCMToken = task.result

                val savedFCMToken = appPreference.curFcmToken
                if (newFCMToken != null) {
                    var uuid = appPreference.curDeviceUUid
                    if (uuid.isEmpty()) {
                        uuid = UUID.randomUUID().toString() // randon UUID 생성
                    }
                    appPreference.curDeviceUUid = uuid
                    if (savedFCMToken.isNotEmpty() && newFCMToken != savedFCMToken) {
                        appPreference.curFcmToken = newFCMToken
                    } else {
                        appPreference.curFcmToken = newFCMToken
                    }
                }
            })

    }


    private fun checkIsLogin() {

        if (appPreference.githubId == "") {
            startActivity(SignInActivity::class)
        }
    }

    private fun setupSwipeRefreshLayout() {
        mBinding.layoutRefresh.setOnRefreshListener {
            mViewModel.refresh()
        }

        mBinding.layoutRefresh.post {
            mViewModel.refresh()
        }
    }

    private fun setupRecyclerView() {

        mBinding.recyclerview.apply {

            adapter = CommitBindingAdapter()


        }
    }

    private fun observeViewModel() {

        mViewModel.apply {

            clickSignIn.observeOnce(this@MainActivity) {
                startActivity(SignInActivity::class, R.anim.slide_in_from_right, R.anim.slide_out_to_left)
            }

            clickShare.observeOnce(this@MainActivity){

            }

            clickShare.observeOnce(this@MainActivity){

            }

            onLoadCompleted.observe(this@MainActivity, Observer {
                if (it) toastUtil.makeToast("커밋 기록을 불러왔습니다").show()
                if (it && mBinding.layoutRefresh.isRefreshing) {

                    mBinding.layoutRefresh.isRefreshing = false

                }
            })

            errorInvoked.observe(this@MainActivity, Observer {
                toastUtil.makeToast(it.message ?: "").show()
            })
        }

    }

    private fun setupBroadcast() {

        Broadcast.apply {

            onLoginCompleted.subscribe {
                mViewModel.refresh()
            }.disposedBy(compositeDisposable)
        }
    }


    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.clear()
    }
}