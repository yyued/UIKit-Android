package com.yy.codex.uikit

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.util.LruCache
import android.util.Base64

/**
 * Created by cuiminghui on 2017/1/10.
 */

class UIImage {

    enum class RenderingMode {
        Automatic,
        AlwaysOriginal,
        AlwaysTemplate
    }

    constructor() {
        bitmap = Bitmap.createBitmap(0, 0, Bitmap.Config.ARGB_8888)
    }

    constructor(context: Context, resID: Int) {
        if (sResCache.get(resID) is Bitmap) {
            bitmap = sResCache.get(resID)
        } else {
            bitmap = BitmapFactory.decodeResource(context.resources, resID)
            sResCache.put(resID, bitmap)
        }
    }

    constructor(data: ByteArray) {
        bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
    }

    constructor(base64String: String) {
        val data = Base64.decode(base64String, Base64.DEFAULT)
        bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
    }

    constructor(bitmap: Bitmap) {
        this.bitmap = bitmap
    }

    /* Scale */

    var scale = UIScreen.mainScreen.scale()

    /* RenderingMode */

    var renderingMode = RenderingMode.Automatic

    /* Bitmap instance */

    val bitmap: Bitmap?

    val size: CGSize
        get() {
            if (bitmap == null) {
                return CGSize(0.0, 0.0)
            }
            return CGSize(Math.ceil(bitmap.width / scale), Math.ceil(bitmap.height / scale))
        }

    companion object {
        private val sResCache = LruCache<Number, Bitmap>(8 * 1024 * 1024)
    }

}
