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

    fun <T> maybeTransformer(): MaybeTransformer<T, T> =
        MaybeTransformer { upstream ->
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

    fun <T> computationAsyncTransformer(): ObservableTransformer<T, T> =
        ObservableTransformer { upstream ->
            upstream
                .subscribeOn(ioScheduler)
                .observeOn(Schedulers.computation())
        }
}
