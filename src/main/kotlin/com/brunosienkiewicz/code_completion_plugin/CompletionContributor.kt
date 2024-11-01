package com.brunosienkiewicz.code_completion_plugin

import com.intellij.codeInsight.inline.completion.InlineCompletionContributor
import com.intellij.codeInsight.inline.completion.InlineCompletionRequest
import com.intellij.codeInsight.inline.completion.InlineCompletionSuggestion
import com.intellij.openapi.progress.blockingContext
import kotlinx.coroutines.flow.asFlow

class CompletionContributor : InlineCompletionContributor() {
    /**
     * Cache configuration
     * Possibly should be moved to a configuration file
     * that could be set with settings in the IDE
     * which then would be read by the plugin
     * */
    private val cacheConfig = LRUCompletionCacheConfig.Builder()
            .setWindowSize(20)
            .setMaxSize(500)
            .build()

    /**
     * Cache completion mechanism
     * In the future when more implementations are added
     * this could be moved to a factory
     * */
    private val cache = LRUCompletionCache(cacheConfig)
    private val client = OllamaClient

    override suspend fun getSuggestionDebounced(request: InlineCompletionRequest): InlineCompletionSuggestion {
        val (prefix, suffix) = request.document.text.splitUsingOffset(request.startOffset)
        val elements = blockingContext {
            cache.getCachedCompletion(prefix)?.let { return@blockingContext it }
            val completion = client.complete(prefix)
            cache.putCachedCompletion(prefix, completion)
            completion
        } ?: return InlineCompletionSuggestion.Empty
        return InlineCompletionSingleSuggestion.build(elements = elements.asFlow())
    }
}
