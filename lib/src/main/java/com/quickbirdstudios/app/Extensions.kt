package com.quickbirdstudios.app

import android.animation.Animator
import android.animation.ObjectAnimator
import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewPropertyAnimator
import android.widget.ImageView

/**
 * Created by sebastiansellmair on 10.11.17.
 *
 * Containing all wobbly extensions
 */


fun View.wobble(overshoot: Number = Wobbly.STANDARD_OVERSHOOT
                , wobbles: Number = Wobbly.STANDARD_WOBBLES,
                duration: Long = 1000) {
    val durationPhase1 = (duration * Wobbly.STANDARD_DURATION_RATIO_REVERSE).toLong()
    val durationPhase2 = (Wobbly.STANDARD_DURATION_RATIO *  duration).toLong()

    var firstPhaseCanceled = false
    this.animate()
            .apply {
                this.scaleX(1+overshoot.toFloat())
                this.scaleY(1+overshoot.toFloat())
                this.interpolator = Wobbly.STANDARD_INTERPOLATOR_SHRINK
                this.duration = durationPhase1
            }
            .setListener(object : Animator.AnimatorListener{
                override fun onAnimationStart(animation: Animator?) = Unit
                override fun onAnimationRepeat(animation: Animator?) = Unit
                override fun onAnimationEnd(animation: Animator?)  = Unit

                override fun onAnimationCancel(animation: Animator?) {
                    firstPhaseCanceled = true
                }


            })
            .start()
    this.postDelayed({
        this.animate()
                .apply {
                    if(firstPhaseCanceled) return@postDelayed
                    this.scaleX(1f)
                    this.scaleY(1f)
                    this.interpolator = Wobbly.interpolator(wobbles=wobbles, overshoot = overshoot)
                    this.duration = durationPhase2
                }
                .start()
    }, durationPhase1)
}

fun ImageView.setWobblyDrawable(drawable: Drawable,
                                duration: Long = 1000,
                                wobbles: Number = Wobbly.STANDARD_WOBBLES) {
    val durationPhase1 = (duration*Wobbly.STANDARD_DURATION_RATIO_REVERSE).toLong()
    val durationPhase2 = (duration*Wobbly.STANDARD_DURATION_RATIO).toLong()
    var animationCanceled = false
    this.animate()
            .apply {
                this.scaleX(0f)
                this.scaleY(0f)
                this.interpolator = Wobbly.STANDARD_INTERPOLATOR_REVERSE
                this.duration = durationPhase1
            }
            .setListener(object : Animator.AnimatorListener{
                override fun onAnimationRepeat(animation: Animator?) = Unit
                override fun onAnimationEnd(animation: Animator?) = Unit
                override fun onAnimationStart(animation: Animator?) = Unit

                override fun onAnimationCancel(animation: Animator?) {
                    animationCanceled = true
                }

            })
            .start()

    this.postDelayed({
        if(animationCanceled) return@postDelayed
        this.setImageDrawable(drawable)
        this.animate()
                .apply {
                    this.scaleX(1f)
                    this.scaleY(1f)
                    this.interpolator = Wobbly.interpolator(wobbles,Wobbly.STANDARD_OVERSHOOT)
                    this.duration = durationPhase2
                }
                .start()
    }, durationPhase1)
}


@Deprecated("I prefer the naming wobblify :)",
        replaceWith = ReplaceWith("wobblify(wobbles,overshoot)"))
fun ViewPropertyAnimator.wobble(wobbles: Number, overshoot: Number): ViewPropertyAnimator {
    this.interpolator = Wobbly.interpolator(wobbles = wobbles, overshoot = overshoot)
    return this
}


@Deprecated("I prefer the naming wobblify :)",
        replaceWith = ReplaceWith("wobblify()"))
fun ViewPropertyAnimator.wobble(): ViewPropertyAnimator {
    this.interpolator = Wobbly.STANDARD_INTERPOLATOR
    return this
}

fun ViewPropertyAnimator.wobblify(wobbles: Number, overshoot: Number): ViewPropertyAnimator {
    this.interpolator = Wobbly.interpolator(wobbles = wobbles, overshoot = overshoot)
    return this
}

fun ViewPropertyAnimator.wobblify(): ViewPropertyAnimator {
    this.interpolator = Wobbly.STANDARD_INTERPOLATOR
    return this
}

fun ObjectAnimator.wobblify(wobbles: Number, overshoot: Number) {
    this.interpolator = Wobbly.interpolator(wobbles, overshoot)
}

fun ObjectAnimator.wobblify(){
    this.interpolator = Wobbly.STANDARD_INTERPOLATOR
}

