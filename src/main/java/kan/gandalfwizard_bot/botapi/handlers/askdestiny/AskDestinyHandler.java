package kan.gandalfwizard_bot.botapi.handlers.askdestiny;

import kan.gandalfwizard_bot.botapi.BotState;
import kan.gandalfwizard_bot.botapi.InputMessageHandler;
import kan.gandalfwizard_bot.cache.UserDataCache;
import kan.gandalfwizard_bot.service.ReplyMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Slf4j
@Component
public class AskDestinyHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessageService replyMessageService;

    public AskDestinyHandler(UserDataCache userDataCache, ReplyMessageService replyMessageService) {
        this.userDataCache = userDataCache;
        this.replyMessageService = replyMessageService;
    }


    @Override
    public SendMessage handle(Message message) {
        return processUserInput(message);
    }

    private SendMessage processUserInput(Message message) {
        int userId = message.getFrom().getId();
        long chatId = message.getChatId();

        SendMessage replyToUser = replyMessageService.getReplyMessage(chatId,"reply.askDestiny");
        userDataCache.setUsersCurrentBotState(userId,BotState.FILLING_PROFILE);
        return replyToUser;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ASK_DESTINY;
    }
}
