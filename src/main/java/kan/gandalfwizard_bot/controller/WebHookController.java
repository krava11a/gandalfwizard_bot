package kan.gandalfwizard_bot.controller;

import kan.gandalfwizard_bot.GandalfWizardTelegramBot;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@RestController
public class WebHookController {

    private final GandalfWizardTelegramBot gandalfBot;


    public WebHookController(GandalfWizardTelegramBot gandalfBot) {
        this.gandalfBot = gandalfBot;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update){
        return gandalfBot.onWebhookUpdateReceived(update);
    }

    @GetMapping("/status")
    public String getStatus(){
        return "The Bot is work!";
    }
}
