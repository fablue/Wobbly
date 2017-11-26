package com.quickbirdstudios.wobbly

import android.view.animation.OvershootInterpolator


internal fun wobbly(wobbleCount: Number, overshoot: Number) = HarmonicInterpolator
        .create(wobbleCount.toDouble(), overshoot.toDouble())


class Wobbly{
    companion object {

        /**
         * Create a 'wobbling' interpolator
         * This creates a physical correct representation of a
         * spring (harmonic oscillator) with dampening under given constraints.
         *
         * *For simplicity* : Params are described by using an example of a
         * given mass attached to a spring which has its rest position at the
         * value you want to animate to.
         *
         *
         * @param wobbles The number of times the the object passes the rest position.
         * This can also be called the 'wobbleCount'. In other words: How often it wobbles.
         *
         * @param overshoot This regulates the dampening of the spring: Overshoot=1 means no
         * dampening at all (which means the oscillation won't get any smaller over time)
         * Overshoot=0 means that it is completely damped to the 'aperiodic limit'.
         * This means no wobble!
         *
         * Overshoot is the maximum overshoot for the first turning point. This
         * can be treated like the androids [OvershootInterpolator]'s Overshhot
         *
         * See [OvershootInterpolator]
         *
         *
         */
        fun interpolator(wobbles: Number, overshoot: Number)
         = wobbly(wobbles, overshoot)

        /**
         * A common parametrization for wobbly
         */
        val STANDARD_INTERPOLATOR = HarmonicInterpolator.STANDARD

        /**
         * A common reverse parametrization of wobbly
         */
        val STANDARD_INTERPOLATOR_REVERSE = HarmonicInterpolator.STANDARD_REVERSE

        /**
         * A interpolator for smooth object shrinkage (scale animation)
         */
        val STANDARD_INTERPOLATOR_SHRINK = interpolator(wobbles = 0, overshoot = 1)

        /**
         * A interpolator for smooth object expansion (scale animation)
         */
        val STANDARD_INTERPOLATOR_EXPAND = interpolator(wobbles = 2, overshoot = 0.3)

        /**
         * The most common value for [interpolator]: wobbles
         */
        val STANDARD_WOBBLES = 2.0

        /**
         * The most common value for [interpolator]: overshoot
         */
        val STANDARD_OVERSHOOT = 0.2


        /**
         * A very common ratio for in/out fading animations.
         * Multiply duration with this value for resulting animation
         * And multiply duration with [STANDARD_DURATION_RATIO_REVERSE] for a reverse animation
         */
        val STANDARD_DURATION_RATIO = 2.0/3.0

        /**
         * @see STANDARD_DURATION_RATIO
         */
        val STANDARD_DURATION_RATIO_REVERSE = 1.0/3.0

    }
}