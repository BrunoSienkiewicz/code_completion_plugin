package com.brunosienkiewicz.code_completion_plugin


abstract class CompletionCacheConfig protected constructor(
        open val maxSize: Int
) {
    /**
     * Abstract cache configuration mechanism.
     * Provides builder for creating cache configurations,
     * that can extended with new properties when needed.
     * Variables are then used by the cache implementations.
     * */
    abstract class Builder<B : Builder<B>> {
        protected var maxSize: Int = 100

        fun setMaxSize(size: Int) = apply { this.maxSize = size } as B

        abstract fun build(): CompletionCacheConfig
    }
}