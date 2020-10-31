package kan.gandalfwizard_bot.botapi.handlers.fillingprofile;

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
public class FillingProfileHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessageService replyMessageService;

    public FillingProfileHandler(UserDataCache userDataCache, ReplyMessageService replyMessageService) {
        this.userDataCache = userDataCache;
        this.replyMessageService = replyMessageService;
    }

    @Override
    public SendMessage handle(Message message) {
        if (userDataCache.getUsersCurrentBotState(message.getFrom().getId()).equals(BotState.FILLING_PROFILE)){
            userDataCache.setUsersCurrentBotState(message.getFrom().getId(),BotState.ASK_NAME);
        }
        return processInputMessage(message);
    }

    private SendMessage processInputMessage(Message message) {
        String usrAnswer = message.getText();
        int usrId = message.getFrom().getId();
        long chatId = message.getChatId();

        UserProfileData userProfileData = userDataCache.getUserProfileData(usrId);
        BotState botState = userDataCache.getUsersCurrentBotState(usrId);

        SendMessage replyToUser = null;

        if (botState.equals(BotState.ASK_NAME)){
            replyToUser = replyMessageService.getReplyMessage(chatId,"reply.askName");
            userDataCache.setUsersCurrentBotState(usrId,BotState.ASK_AGE);
        }

        if (botState.equals(BotState.ASK_AGE)){
            userProfileData.setName(usrAnswer);
            replyToUser = replyMessageService.getReplyMessage(chatId,"reply.askAge");
            userDataCache.setUsersCurrentBotState(usrId,BotState.ASK_GENDER);
        }

        if (botState.equals(BotState.ASK_GENDER)){
            userProfileData.setAge(Integer.parseInt(usrAnswer));
            replyToUser = replyMessageService.getReplyMessage(chatId,"reply.askGender");
            userDataCache.setUsersCurrentBotState(usrId,BotState.ASK_COLOR);
        }

        if (botState.equals(BotState.ASK_COLOR)){
            userProfileData.setGender(usrAnswer);
            replyToUser = replyMessageService.getReplyMessage(chatId,"reply.askColor");
            userDataCache.setUsersCurrentBotState(usrId,BotState.ASK_NUMBER);
        }

        if (botState.equals(BotState.ASK_NUMBER)){
            userProfileData.setColor(usrAnswer);
            replyToUser = replyMessageService.getReplyMessage(chatId,"reply.askNumber");
            userDataCache.setUsersCurrentBotState(usrId,BotState.ASK_MOVIE);
        }

        if (botState.equals(BotState.ASK_MOVIE)){
            userProfileData.setNumber(Integer.parseInt(usrAnswer));
            replyToUser = replyMessageService.getReplyMessage(chatId,"reply.askMovie");
            userDataCache.setUsersCurrentBotState(usrId,BotState.ASK_SONG);
        }

        if (botState.equals(BotState.ASK_SONG)){
            userProfileData.setMovie(usrAnswer);
            replyToUser = replyMessageService.getReplyMessage(chatId,"reply.askSong");
            userDataCache.setUsersCurrentBotState(usrId,BotState.PROFILE_FILLED);
        }

        if (botState.equals(BotState.PROFILE_FILLED)){
            userProfileData.setSong(usrAnswer);
            replyToUser = new SendMessage(chatId,String.format("Введены следующие данные %s",userProfileData));
            userDataCache.setUsersCurrentBotState(usrId,BotState.ASK_DESTINY);
        }

        userDataCache.saveUserProfileData(usrId,userProfileData);

        return replyToUser;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.FILLING_PROFILE;
    }
}
