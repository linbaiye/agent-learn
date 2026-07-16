package com.example;

import com.anthropic.client.AnthropicClient;
import com.anthropic.client.okhttp.AnthropicOkHttpClient;
import com.anthropic.models.messages.Message;
import com.anthropic.models.messages.MessageCreateParams;
import com.anthropic.models.messages.MessageParam;
import com.anthropic.models.messages.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ClaudeAgent {

    private static final String QUIT_COMMAND = "quit";

    public static void main(String[] args) throws IOException {
        // Reads ANTHROPIC_API_KEY from the environment
        AnthropicClient client = AnthropicOkHttpClient.fromEnv();

        List<MessageParam> history = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String question = line.trim();
                if (question.equalsIgnoreCase(QUIT_COMMAND)) {
                    break;
                }
                if (question.isEmpty()) {
                    continue;
                }

                history.add(MessageParam.builder()
                        .role(MessageParam.Role.USER)
                        .content(question)
                        .build());

                MessageCreateParams params = MessageCreateParams.builder()
                        .model(Model.CLAUDE_HAIKU_4_5)
                        .maxTokens(1024L)
                        .messages(history)
                        .build();

                Message response = client.messages().create(params);
                history.add(response.toParam());

                response.content().stream()
                        .flatMap(block -> block.text().stream())
                        .forEach(textBlock -> System.out.println("Answer: " + textBlock.text()));
            }
        }
    }
}
