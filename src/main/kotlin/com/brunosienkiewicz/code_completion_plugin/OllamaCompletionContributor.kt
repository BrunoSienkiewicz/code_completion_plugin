package com.brunosienkiewicz.code_completion_plugin

import com.intellij.codeInsight.inline.completion.InlineCompletionContributor
import com.intellij.codeInsight.inline.completion.InlineCompletionRequest
import com.intellij.codeInsight.inline.completion.InlineCompletionSuggestion
import com.intellij.openapi.progress.blockingContext
import com.ollama.client.OllamaClient
import kotlinx.coroutines.flow.asFlow
import com.brunosienkiewicz.code_completion_plugin.CompletionCache

class OllamaCompletionContributor : InlineCompletionContributor() {
    private val cache = CompletionCache()

    override suspend fun getSuggestionDebounced(request: InlineCompletionRequest): InlineCompletionSuggestion {
        val (prefix, suffix) = request.document.text.splitUsingOffset(request.startOffset)
        val elements = blockingContext {
            if (cache.getCachedCompletion(prefix)) {
                cache.getCachedCompletion(prefix)
            } else {
                OllamaClient.getCompletion(prefix)
            }
        } ?: return InlineCompletionSuggestion.Empty
        return InlineCompletionSingleSuggestion.build(elements = elements.asFlow())
    }
}
