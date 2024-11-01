package com.brunosienkiewicz.code_completion_plugin


class LRUCompletionCacheConfig constructor(
        override val maxSize: Int,
        val windowSize: Int
) : CompletionCacheConfig(maxSize) {
    /**
     * Adds additional property to the cache configuration
     */
    class Builder: CompletionCacheConfig.Builder<Builder>(){
        private var windowSize = 10

        fun setWindowSize(size: Int) = apply { this.windowSize = size }

        override fun build(): LRUCompletionCacheConfig {
            return LRUCompletionCacheConfig(maxSize, windowSize)
        }
    }
}