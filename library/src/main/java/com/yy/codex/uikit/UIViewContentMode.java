package com.yy.codex.uikit;

/**
 * Created by cuiminghui on 2017/1/10.
 */

public enum UIViewContentMode {
    ScaleToFill,
    ScaleAspectFit,      // contents scaled to fit with fixed aspect. remainder is transparent
    ScaleAspectFill,     // contents scaled to fill with fixed aspect. some portion of content may be clipped.
    Redraw,              // redraw on bounds change (calls -setNeedsDisplay)
    Center,              // contents remain same size. positioned adjusted.
    Top,
    Bottom,
    Left,
    Right,
    TopLeft,
    TopRight,
    BottomLeft,
    BottomRight,
}
