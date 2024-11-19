package com.jj.routingservice.util

import android.app.Activity
import android.os.Bundle

class SelfDismissActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        finish()
    }
}
