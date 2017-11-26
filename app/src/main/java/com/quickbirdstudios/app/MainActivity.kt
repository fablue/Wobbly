package com.quickbirdstudios.app

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drawableRound = ContextCompat.getDrawable(this, R.drawable.round)
        val drawableRect = ContextCompat.getDrawable(this, R.drawable.rectangular)


        test.setOnClickListener { view ->
            view.wobble()
        }

    }
}
