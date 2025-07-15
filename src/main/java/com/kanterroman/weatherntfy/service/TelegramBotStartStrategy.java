package com.kanterroman.weatherntfy.service;

public class TelegramBotStartStrategy extends AbstractTelegramBotCommandStrategy implements TelegramBotCommandStrategy {
    public TelegramBotStartStrategy() {
        reply = "Привет! Я - бот для отслеживания погодных условий.";
    }
}
