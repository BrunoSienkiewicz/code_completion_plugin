package com.brunosienkiewicz.code_completion_plugin

import io.github.ollama4j.OllamaAPI
import io.github.ollama4j.types.OllamaModelType
import io.github.ollama4j.utils.PromptBuilder
import org.ini4j.spi.OptionsBuilder




object OllamaClient {
    private const val HOST = "http://localhost:11434"
    private const val MODEL: String = OllamaModelType.PHI

    var ollamaAPI: OllamaAPI = OllamaAPI(HOST)

    /**
     * Example use of OllamaAPI was found in official docs
     * https://github.com/ollama4j/ollama4j/blob/main/docs/docs/apis-generate/prompt-builder.md
     */
    fun getCompletion(context: String): String? {
        val prompt = generatePrompt(context)
        val raw = false
        val response: OllamaResult = ollamaAPI.generate(MODEL, prompt, raw, OptionsBuilder().build())
        return response.getResponse()
    }

    private fun generatePrompt(context: String): String {
        val promptBuilder: PromptBuilder = PromptBuilder()
                .addLine("You are an expert coder and understand different programming languages.")
                .addLine("Given a question, answer ONLY with code.")
                .addLine("Produce clean, formatted and indented code in markdown format.")
                .addLine(
                        "DO NOT include ANY extra text apart from code. Follow this instruction very strictly!")
                .addLine("If there's any additional information you want to add, use comments within code.")
                .addLine("Answer only in the programming language that has been asked for.")
                .addSeparator()
                .addLine("Example: Sum 2 numbers in Python")
                .addLine("Answer:")
                .addLine("```python")
                .addLine("def sum(num1: int, num2: int) -> int:")
                .addLine("    return num1 + num2")
                .addLine("```")
                .addSeparator()
                .add("Return code completion to code given below:")
                .addLine("```")
                .addLine(context)
                .addLine("```")
        return promptBuilder.build()
    }
}