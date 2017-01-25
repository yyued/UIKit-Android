package com.yy.codex.uikit

import android.content.Context
import android.view.LayoutInflater

/**
 * Created by cuiminghui on 2017/1/25.
 */

class UIXMLLoader {

    companion object {

        fun loadViewFromXML(context: Context, resID: Int): UIView? {
            val view = LayoutInflater.from(context).inflate(resID, null) as? UIView ?: return null
            view.awakeFromXML()
            return view
        }

    }

}
