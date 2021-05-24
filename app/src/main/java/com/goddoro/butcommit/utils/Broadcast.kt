package com.goddoro.butcommit.utils

import io.reactivex.subjects.PublishSubject

object Broadcast {

    val onLoginCompleted : PublishSubject<Unit> = PublishSubject.create()
}