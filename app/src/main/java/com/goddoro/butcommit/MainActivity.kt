package com.goddoro.butcommit

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMainBinding.inflate(LayoutInflater.from(this))

        mBinding.lifecycleOwner = this
        mBinding.vm = mViewModel

        setContentView(mBinding.root)

        Log.d(TAG, appPreference.githubId + "님 환영합니다")
        Log.d(TAG, appPreference.startDate + "부터 시작하셨네요")

        initFirebaseSetting()
        observeViewModel()
        checkIsLogin()
        setupBroadcast()
        setupRecyclerView()
        setupSwipeRefreshLayout()

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
                startActivity(SignInActivity::class)
            }

            onLoadCompleted.observe(this@MainActivity, Observer {
                if (it) Toast.makeText(this@MainActivity, "커밋 기록을 불러왔습니다", Toast.LENGTH_SHORT)
                    .show()
                if (it && mBinding.layoutRefresh.isRefreshing) {

                    mBinding.layoutRefresh.isRefreshing = false

                }
            })

            errorInvoked.observe(this@MainActivity, Observer {

                Toast.makeText(this@MainActivity, it.message.toString(), Toast.LENGTH_SHORT).show()


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