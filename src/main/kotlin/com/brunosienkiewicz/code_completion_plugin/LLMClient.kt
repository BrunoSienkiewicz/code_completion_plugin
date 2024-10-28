package com.brunosienkiewicz.code_completion_plugin

interface LLMClient {
    fun getCompletion(context: String): String?;
}