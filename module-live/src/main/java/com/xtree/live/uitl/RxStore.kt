package com.xtree.live.uitl

import androidx.annotation.CheckResult
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject

class RxStore<T : Any>(
    defaultValue: T,
    scheduler: Scheduler = Schedulers.computation()
) : Disposable {

    private val behaviorProcessor = BehaviorProcessor.createDefault(defaultValue)
    private val actionSubject = PublishSubject.create<(T) -> T>().toSerialized()

    val state: T get() = behaviorProcessor.value!!
    val stateFlowable: Flowable<T> = behaviorProcessor.onBackpressureLatest()

    val actionDisposable: Disposable = actionSubject
        .observeOn(scheduler)
        .scan(defaultValue) { v, f -> f(v) }
        .subscribe { behaviorProcessor.onNext(it) }

    fun update(transformer: (T) -> T) {
        actionSubject.onNext(transformer)
    }

    @CheckResult
    fun <U : Any> update(flowable: Flowable<U>, transformer: (U, T) -> T): Disposable {
        return flowable.subscribe {
            actionSubject.onNext { t -> transformer(it, t) }
        }
    }

    fun addTo(disposable: CompositeDisposable): RxStore<T> {
        disposable.add(this)
        return this
    }

    fun <R : Any> mapDistinctForUi(map: (T) -> R): Flowable<R> {
        return stateFlowable
            .map(map)
            .distinctUntilChanged()
            .observeOn(AndroidSchedulers.mainThread())
    }

    /**
     * Dispose of the underlying scan chain. This is terminal.
     */
    override fun dispose() {
        actionDisposable.dispose()
    }

    override fun isDisposed(): Boolean {
        return actionDisposable.isDisposed
    }
}
