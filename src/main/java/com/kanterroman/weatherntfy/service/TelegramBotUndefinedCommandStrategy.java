package com.kanterroman.weatherntfy.service;

public class TelegramBotUndefinedCommandStrategy extends AbstractTelegramBotCommandStrategy implements TelegramBotCommandStrategy {
    public TelegramBotUndefinedCommandStrategy() {
        reply = "Недействительная команда. Пожалуйста, введите команду из списка доступных.";
    }
}
