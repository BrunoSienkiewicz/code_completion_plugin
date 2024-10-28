package com.brunosienkiewicz.code_completion_plugin


abstract class CompletionCacheConfig protected constructor(
        open val maxSize: Int
) {
    abstract class Builder<B : Builder<B>> {
        protected var maxSize: Int = 100

        fun setMaxSize(size: Int) = apply { this.maxSize = size } as B

        abstract fun build(): CompletionCacheConfig
    }
}