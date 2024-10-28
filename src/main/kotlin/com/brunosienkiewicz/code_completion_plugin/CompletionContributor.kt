package com.brunosienkiewicz.code_completion_plugin

import com.intellij.codeInsight.inline.completion.InlineCompletionContributor
import com.intellij.codeInsight.inline.completion.InlineCompletionRequest
import com.intellij.codeInsight.inline.completion.InlineCompletionSuggestion
import com.intellij.openapi.progress.blockingContext
import kotlinx.coroutines.flow.asFlow

class CompletionContributor : InlineCompletionContributor() {
    private val cacheConfig = LRUCompletionCacheConfig.Builder()
            .setWindowSize(20)
            .setMaxSize(500)
            .build()
    private val cache = LRUCompletionCache(cacheConfig)
    private val client = OllamaClient

    override suspend fun getSuggestionDebounced(request: InlineCompletionRequest): InlineCompletionSuggestion {
        val (prefix, suffix) = request.document.text.splitUsingOffset(request.startOffset)
        val elements = blockingContext {
            if (cache.get(prefix)==null) {
                cache.get(prefix)
            } else {
                client.getCompletion(prefix)
            }
        } ?: return InlineCompletionSuggestion.Empty
        return InlineCompletionSingleSuggestion.build(elements = elements.asFlow())
    }
}
