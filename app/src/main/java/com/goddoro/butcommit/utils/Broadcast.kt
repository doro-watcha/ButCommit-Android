package com.goddoro.butcommit.utils

import io.reactivex.subjects.PublishSubject

object Broadcast {

    val registerCompleteBroadcast : PublishSubject<String> = PublishSubject.create()
}