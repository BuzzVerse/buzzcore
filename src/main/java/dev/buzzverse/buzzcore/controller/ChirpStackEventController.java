package dev.buzzverse.buzzcore.controller;

import dev.buzzverse.buzzcore.service.ChirpStackEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
class ChirpStackEventController {

    private final ChirpStackEventService chirpStackEventService;

    @PostMapping(consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void handleEvent(@RequestParam String event, @RequestBody byte[] requestBody) {
        System.out.println("Received event: " + event);
        chirpStackEventService.handleUplinkEvent(requestBody);
    }
}
