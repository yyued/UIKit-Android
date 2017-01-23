package com.yy.codex.uikit

enum class NSLineBreakMode {
    ByWordWrapping, // Wrap at word boundaries, default
    ByTruncatingHead, // Truncate at tail of line: "...abcd"
    ByTruncatingMiddle, // Truncate at tail of line: "ab...cd"
    ByTruncatingTail, // Truncate at tail of line: "abcd..."
}
