package com.yy.codex.uikit

import android.os.Bundle

/**
 * Created by PonyCui_Home on 2017/1/23.
 */

open class UINavigationActivity: UIActivity() {

    var navigationController: UINavigationController? = null
        set(value) {
            main = value
            field = value
            setContentView(value?.view)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigationController = UINavigationController(this)
    }

    override fun onBackPressed() {
        navigationController?.popViewController(true)
    }

}