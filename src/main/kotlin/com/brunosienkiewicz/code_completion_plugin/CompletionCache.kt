package com.brunosienkiewicz.code_completion_plugin

import java.util.*

abstract class CompletionCache protected constructor(open val config: CompletionCacheConfig) {
    abstract val cache: MutableMap<String, String>;

    protected open fun getContextHash(context: String): String {
        return context.hashCode().toString()
    }

    protected open fun getCachedCompletion(context: String): String? {
        val key = getContextHash(context)
        return cache[key]
    }

    protected open fun putCachedCompletion(context: String, suggestion: String) {
        val key = getContextHash(context)
        cache[key] = suggestion
    }

    protected open fun invalidateCache(context: String) {
        val key = getContextHash(context)
        cache.remove(key)
    }
}
