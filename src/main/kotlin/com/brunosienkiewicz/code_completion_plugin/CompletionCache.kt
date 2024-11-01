package com.brunosienkiewicz.code_completion_plugin

import java.util.*

abstract class CompletionCache protected constructor(open val config: CompletionCacheConfig) {
    /**
     * Abstract cache completion mechanism.
     * Provides base methods for caching completions,
     * that can be overridden by subclasses when needed.
     * This allows for different caching strategies to be implemented
     * and all of them to be used interchangeably by the plugin.
     */
    abstract val cache: MutableMap<String, String>;

    protected open fun getContextHash(context: String): String {
        return context.hashCode().toString()
    }

    open fun getCachedCompletion(context: String): String? {
        val key = getContextHash(context)
        return cache[key]
    }

    open fun putCachedCompletion(context: String, suggestion: String) {
        val key = getContextHash(context)
        cache[key] = suggestion
    }

    protected open fun invalidateCache(context: String) {
        val key = getContextHash(context)
        cache.remove(key)
    }
}
