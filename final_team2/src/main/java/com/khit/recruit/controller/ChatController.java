package com.khit.recruit.controller;

@Controller
@Log4j2
public class ChatController {
    
    @GetMapping("/chat")
    public String chatGET(){
        log.info("@ChatController, chat GET()");
        return "chater";
    }
}
