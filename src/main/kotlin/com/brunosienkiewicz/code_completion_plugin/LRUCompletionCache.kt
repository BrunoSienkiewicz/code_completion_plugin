package com.brunosienkiewicz.code_completion_plugin

import java.util.*

class LRUCompletionCache(config: LRUCompletionCacheConfig) : CompletionCache(config) {
    override val config: LRUCompletionCacheConfig = config
    /**
     * LRU Cache implementation using LinkedHashMap
     * I have overridden removeEldestEntry to implement functionality of LRU cache
     */
    override val cache = object : LinkedHashMap<String, String>(config.maxSize, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, String>?): Boolean {
            return size > config.maxSize
        }
    }

    /**
     * Uses only part of the context to hash the text
     */
    private fun getContextWindow(context: String): String {
        val lines = context.lines()
        val start = maxOf(0, lines.size - config.windowSize)
        return lines.subList(start, lines.size).joinToString("\n")
    }

    override fun getContextHash(context: String): String {
        val window = getContextWindow(context)
        return window.hashCode().toString()
    }
}