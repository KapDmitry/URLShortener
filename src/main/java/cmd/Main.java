package cmd;

import controller.Controller;
import linkrepository.inmemory.InMemoryLinkRepository;
import notificationsrepository.inmemory.InMemoryNotificationsRepository;
import service.URLShortener;
import service.URLShortenerConfig;
import urlgenerator.generator.URLGeneratorImpl;
import userrepository.inmemory.InMemoryUserRepository;


public class Main {
    public static void main(String[] args) {

        URLShortenerConfig cfg = new URLShortenerConfig();

        try {
            cfg.loadFromEnvironment();
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка при создании конфига: " + e.getMessage());
            return;
        }

        URLShortener shortener = new URLShortener(
                new InMemoryLinkRepository(),
                new InMemoryUserRepository(),
                new InMemoryNotificationsRepository(),
                new URLGeneratorImpl(),
                cfg
        );

        Controller controller = new Controller(shortener);

        controller.mainLoop();
    }
}