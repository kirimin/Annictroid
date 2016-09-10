package me.kirimin.annictroid._common.extensions

/**
 * @return 整数値の場合は小数点を抜いた文字列を返す。小数点以下がある場合はそのままtoString
 */
fun Float.toIntegerString() = if (this % 1 == 0f) toInt().toString() else toString()