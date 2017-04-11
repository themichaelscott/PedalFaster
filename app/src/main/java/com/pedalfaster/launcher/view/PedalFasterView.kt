package com.pedalfaster.launcher.view

import android.content.Context
import android.media.AudioManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import com.pedalfaster.launcher.R
import kotlinx.android.synthetic.main.view_pedal_faster_popup.view.*

class PedalFasterView : FrameLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    private val audioManager: AudioManager

    init {
        val inflater = LayoutInflater.from(context)
        audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        inflater.inflate(R.layout.view_pedal_faster_popup, this, true)
        setOnTouchListener { _, _ -> true } // intercepts all touch events so user can't touch activity behind
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        // requesting audio focus pauses the video
        audioManager.requestAudioFocus({ }, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
        cyclerImageView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.enter_from_left))
    }

    override fun onDetachedFromWindow() {
        audioManager.abandonAudioFocus { }
        // TODO - EXIT DOESN'T ANIMATE OUT
        cyclerImageView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.exit_to_right))
        super.onDetachedFromWindow()
    }

}