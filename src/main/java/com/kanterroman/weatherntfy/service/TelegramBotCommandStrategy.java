package com.kanterroman.weatherntfy.service;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public interface TelegramBotCommandStrategy {
    void execute(TelegramClient telegramClient, long chatId) throws TelegramApiException;
}
