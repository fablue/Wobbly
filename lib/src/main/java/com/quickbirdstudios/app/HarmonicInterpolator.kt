package com.quickbirdstudios.app

/**
 * Created by sebastiansellmair on 10.11.17.
 *
 * See github: fablue harmonic interpolator android
 */
import android.view.animation.Interpolator

class HarmonicInterpolator private constructor(private val omega: Double, private val gamma: Double) : Interpolator {

    /**
     * Reversing the interpolator result in a animation with negative overshoot.
     * This looks especially good for 'reversed' animations like shrinking an object.
     */
    var reverse = false

    override fun getInterpolation(p0: Float): Float {
        val time = if(reverse) 1.0-p0.toDouble() else p0.toDouble()
        val rawInterpol = interpolate(omega, gamma, time).toFloat()
        return if(reverse) 1- rawInterpol else rawInterpol
    }



    companion object {
        fun create(wobbles: Double, overshoot: Double): HarmonicInterpolator {
            val omega = calculateOmega(wobbles)
            val gamma = calculateGamma(wobbles, overshoot, omega)
            return HarmonicInterpolator(omega, gamma)
        }


        /**
         * Standard means
         *  - restPositionRuns: 4.0
         *  - overshoot: 0.2
         */
        val STANDARD = HarmonicInterpolator(17.27875959474386, 9.681908518387534)
        val STANDARD_REVERSE = HarmonicInterpolator(17.27875959474386, 9.681908518387534).also { it.reverse = true }
    }
}

internal fun interpolate(omega: Double, gamma: Double, time: Double): Double =
        1f - Math.exp(-gamma * time) * Math.cos(omega * time)


/**
 * Calculates the frequency of the oscillator to the constraint of x(1) = 1
 */
internal fun calculateOmega(restPositionRuns: Double): Double {
    //We think of the oscillator being at 0.25 state (full deflection)
    val fullOscillations = restPositionRuns / 2.0 + 0.75
    return 2.0 * Math.PI * fullOscillations
}


/**
 * Calculates the t_max_x (The time for extrem x)
 *
 * This is equivalent to the following problem:
 *
 *  d/dt (1 - exp(-gamma * t) * cos(omega * t ) = 0
 *
 * @param omega The frequency of the oscillator
 * @param gamma  The damping of the oscillator
 * @return The time where the oscillator is expected to have maximum overshoot
 */
internal fun calculateTuningTime(omega: Double, gamma: Double): Double =
        2 * Math.atan(omega / gamma - Math.sqrt(Math.pow(gamma, 2.0) + Math.pow(omega, 2.0)) / gamma) / omega + Math.PI / omega


/**
 * Tries to find gamma with a certain precision
 * @param settings  The settings of the oscillator
 * @param omega  The already calculated omega for the parametrization
 * @return A 'good enough' gamma which leads to the expected overshoot
 */
internal fun calculateGamma(restPositionRuns: Double, overshoot: Double, omega: Double): Double {
    //The precision of the gamma itself. NOT the precision of the deviation from the expected overshoot!
    val precision = 0.01

    //Start by estimating a t_max which should maximize our x(t)
    val time = Math.PI / omega

    //Calculate a naive gamma for our expected time
    var gamma = -Math.log(overshoot) / time

    //Adjust our t_max by the newly calculated gamma
    val adjustedTime = calculateTuningTime(omega, gamma)

    //Calculate the deviation from our expected overshoot
    val normalizedInterpolation = interpolate(omega, gamma, adjustedTime)
    var deviation = Math.abs(overshoot - normalizedInterpolation)


    /*
      Determine whether our naively calculated gamma was to high or to low.
      If our interpolation is higher than the wanted overshoot, than we should increase
      the damping. Else decrease
    */
    val sign = if (normalizedInterpolation - overshoot > 0) 1 else -1


    //Now lets optimize our gamma by finding the lowest deviation, while finetuning!
    while (true) {
        val tunedGamma = gamma + sign * precision
        val tunedTime = calculateTuningTime(omega, gamma)
        val tunedNormalizedInterpolation = interpolate(omega, gamma, tunedTime) -1
        val tunedDeviation = Math.abs(overshoot - tunedNormalizedInterpolation)
        if (tunedDeviation < deviation) {
            deviation = tunedDeviation
            gamma = tunedGamma
        } else return gamma;
    }
}