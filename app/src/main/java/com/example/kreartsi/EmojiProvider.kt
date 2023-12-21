package com.example.kreartsi

import com.vdurmont.emoji.EmojiParser

class EmojiHandler {
    fun handleEmojis(input: String): String {
        return EmojiParser.parseToUnicode(input)
    }
}