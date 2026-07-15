package com.example;

import com.anthropic.client.AnthropicClient;
import com.anthropic.client.okhttp.AnthropicOkHttpClient;
import com.anthropic.models.messages.Message;
import com.anthropic.models.messages.MessageCreateParams;
import com.anthropic.models.messages.Model;

public class ClaudeAgent {

    private static final String QUESTION = "What is the capital of France?";

    public static void main(String[] args) {
        // Reads ANTHROPIC_API_KEY from the environment
        AnthropicClient client = AnthropicOkHttpClient.fromEnv();

        MessageCreateParams params = MessageCreateParams.builder()
                .model(Model.CLAUDE_HAIKU_4_5)
                .maxTokens(1024L)
                .addUserMessage(QUESTION)
                .build();

        Message response = client.messages().create(params);

        System.out.println("Question: " + QUESTION);
        response.content().stream()
                .flatMap(block -> block.text().stream())
                .forEach(textBlock -> System.out.println("Answer: " + textBlock.text()));
    }
}
