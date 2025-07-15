package com.kanterroman.weatherntfy.service;

import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

public class TelegramBotUndefinedCommandStrategy extends AbstractTelegramBotCommandStrategy implements TelegramBotCommandStrategy {
    public TelegramBotUndefinedCommandStrategy() {
        reply = "Недействительная команда. Пожалуйста, введите команду из списка доступных.";
    }
}
