package com.example.tmg_test.repository

import io.reactivex.*
import io.reactivex.schedulers.Schedulers

class SchedulersRepository(private val uiScheduler: Scheduler, private val ioScheduler: Scheduler) {

    fun <T> flowableTransformer(): FlowableTransformer<T, T> =
        FlowableTransformer { upstream ->
            upstream
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
        }

    fun <T> singleTransformer(): SingleTransformer<T, T> =
        SingleTransformer { upstream ->
            upstream
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
        }

    fun completableTransformer(): CompletableTransformer =
        CompletableTransformer { upstream ->
            upstream
                .subscribeOn(ioScheduler)
                .observeOn(uiScheduler)
        }
}
