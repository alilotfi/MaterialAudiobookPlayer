package de.ph1b.audiobook.misc

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.SeekBar
import android.widget.TextView
import rx.AsyncEmitter
import rx.Observable


fun SeekBar.onProgressChanged(initialNotification: Boolean = false, progressChanged: (Int) -> Unit) {
    val listener = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            progressChanged(progress)
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {
        }

        override fun onStopTrackingTouch(seekBar: SeekBar) {
        }
    }

    setOnSeekBarChangeListener(listener)
    if (initialNotification) listener.onProgressChanged(this, progress, false)
}

fun SeekBar.progressChangedStream(initialNotification: Boolean = false): Observable<Int> = Observable.fromAsync({ emitter ->
    onProgressChanged(initialNotification) { emitter.onNext(it) }
    emitter.setCancellation { setOnSeekBarChangeListener(null) }
}, AsyncEmitter.BackpressureMode.LATEST)

fun <T : View> T.clicks(): Observable<T> = Observable.fromAsync({ emitter ->
    setOnClickListener { emitter.onNext(this) }
    emitter.setCancellation { setOnClickListener(null) }
}, AsyncEmitter.BackpressureMode.BUFFER)

fun <T : Adapter> AdapterView<T>.itemSelections(listener: (Int) -> Unit) {
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) {
        }

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            listener(position)
        }
    }
}

fun <T : Adapter> AdapterView<T>.itemSelectionStream(): Observable<Int> = Observable.fromAsync({ emitter ->
    itemSelections { emitter.onNext(it) }
    emitter.setCancellation { onItemSelectedListener = null }
}, AsyncEmitter.BackpressureMode.LATEST)

fun TextView.leftCompoundDrawable(): Drawable? = compoundDrawables[0]
fun TextView.topCompoundDrawable(): Drawable? = compoundDrawables[1]
fun TextView.rightCompoundDrawable(): Drawable? = compoundDrawables[2]
fun TextView.bottomCompoundDrawable(): Drawable? = compoundDrawables[3]