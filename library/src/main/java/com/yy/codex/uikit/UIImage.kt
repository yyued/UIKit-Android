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

    constructor(scale: Double) {
        bitmap = Bitmap.createBitmap(0, 0, Bitmap.Config.ARGB_8888)
        this.scale = scale
    }

    constructor(context: Context, resID: Int) {
        if (sResCache.get(resID) is Bitmap) {
            bitmap = sResCache.get(resID)
            when (bitmap.density) {
                160 -> this.scale = 1.0
                240 -> this.scale = 1.5
                320 -> this.scale = 2.0
                480 -> this.scale = 3.0
                640 -> this.scale = 4.0
                else -> this.scale = bitmap.density / 160.0
            }
        } else {
            val options = BitmapFactory.Options()
            options.inScaled = false
            bitmap = BitmapFactory.decodeResource(context.resources, resID, options)
            when (bitmap.density) {
                160 -> this.scale = 1.0
                240 -> this.scale = 1.5
                320 -> this.scale = 2.0
                480 -> this.scale = 3.0
                640 -> this.scale = 4.0
                else -> this.scale = bitmap.density / 160.0
            }
            sResCache.put(resID, bitmap)
        }
    }

    constructor(data: ByteArray, scale: Double = 1.0) {
        bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
        this.scale = scale
    }

    constructor(base64String: String, scale: Double = 1.0) {
        val data = Base64.decode(base64String, Base64.DEFAULT)
        bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
        this.scale = scale
    }

    constructor(bitmap: Bitmap, scale: Double = 1.0) {
        this.bitmap = bitmap
        this.scale = scale
    }

    /* Scale */

    val scale: Double

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

    override fun equals(other: Any?): Boolean {
        var other = other as? UIImage ?: return false
        return scale == other.scale && renderingMode == other.renderingMode && bitmap == other.bitmap;
    }

    companion object {
        private val sResCache = LruCache<Number, Bitmap>(8 * 1024 * 1024)
    }

}
