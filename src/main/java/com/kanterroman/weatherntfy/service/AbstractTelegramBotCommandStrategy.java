package com.kanterroman.weatherntfy.service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class AbstractTelegramBotCommandStrategy implements TelegramBotCommandStrategy {
    protected String reply;

    protected void sendMessage(TelegramClient telegramClient, long chatId, String reply) throws TelegramApiException {
        SendMessage sendMessage = SendMessage
                .builder()
                .chatId(chatId)
                .text(reply)
                .build();

        telegramClient.execute(sendMessage);
    }

    @Override
    public void execute(TelegramClient telegramClient, long chatId) throws TelegramApiException {
        sendMessage(telegramClient, chatId, reply);
    }
}
