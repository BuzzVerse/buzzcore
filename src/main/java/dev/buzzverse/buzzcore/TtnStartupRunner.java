package dev.buzzverse.buzzcore;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TtnStartupRunner implements CommandLineRunner {

    private final TtnSubscribeService ttnSubscribeService;

    public TtnStartupRunner(TtnSubscribeService ttnSubscribeService) {
        this.ttnSubscribeService = ttnSubscribeService;
    }

    @Override
    public void run(String... args) {
        ttnSubscribeService.startSubscription();
    }
}