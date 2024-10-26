package com.brunosienkiewicz.code_completion_plugin

import java.util.*

class CompletionCache {
    /**
     * Possibly constants can be set in some plugin configuration
     * To simplify code I used constants
     */
    private const val WINDOW_SIZE = 5
    private const val MAX_CACHE_SIZE = 100

    /**
     * LRU Cache implementation using LinkedHashMap
     * I have overridden removeEldestEntry to implement functionality of LRU cache
      */
    private val cache = object : LinkedHashMap<String, String>(MAX_CACHE_SIZE, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<String, String>?): Boolean {
            return size > MAX_CACHE_SIZE
        }
    }

    /**
     * Extracts the last WINDOW_SIZE lines from the text before the caret.
     */
    private fun getContextWindow(context: String): String {
        val lines = context.lines()
        val start = maxOf(0, lines.size - WINDOW_SIZE)
        return lines.subList(start, lines.size).joinToString("\n")
    }

    /**
     * Generates a unique hash key for the context window before the caret.
     */
    private fun getContextHash(context: String): String {
        val window = getContextWindow(context)
        return window.hashCode().toString()
    }

    /**
     * Retrieves a cached completion if available.
     */
    fun getCachedCompletion(context: String): String? {
        val key = getContextHash(context)
        return cache[key]
    }

    /**
     * Stores a completion in the cache.
     */
    fun putCachedCompletion(context: String, suggestion: String) {
        val key = getContextHash(context)
        cache[key] = suggestion
    }
}