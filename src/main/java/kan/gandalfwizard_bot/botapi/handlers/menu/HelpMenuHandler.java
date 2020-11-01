package kan.gandalfwizard_bot.botapi.handlers.menu;

import kan.gandalfwizard_bot.botapi.BotState;
import kan.gandalfwizard_bot.botapi.InputMessageHandler;
import kan.gandalfwizard_bot.service.MainMenuService;
import kan.gandalfwizard_bot.service.ReplyMessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class HelpMenuHandler implements InputMessageHandler {
    private MainMenuService mainMenuService;
    private ReplyMessageService replyMessageService;

    public HelpMenuHandler(MainMenuService mainMenuService, ReplyMessageService replyMessageService) {
        this.mainMenuService = mainMenuService;
        this.replyMessageService = replyMessageService;
    }

    @Override
    public SendMessage handle(Message message) {
        return mainMenuService.getMainMenuMessage(message.getChatId(),
                replyMessageService.getReplyText("reply.showHelpMenu"));
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_HELP_MENU;
    }
}
