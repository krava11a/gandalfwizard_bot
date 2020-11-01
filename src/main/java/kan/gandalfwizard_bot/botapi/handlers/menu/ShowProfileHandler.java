package kan.gandalfwizard_bot.botapi.handlers.menu;

import kan.gandalfwizard_bot.botapi.BotState;
import kan.gandalfwizard_bot.botapi.InputMessageHandler;
import kan.gandalfwizard_bot.botapi.handlers.fillingprofile.UserProfileData;
import kan.gandalfwizard_bot.cache.UserDataCache;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

@Component
public class ShowProfileHandler implements InputMessageHandler {
    private UserDataCache dataCache;

    public ShowProfileHandler(UserDataCache dataCache) {
        this.dataCache = dataCache;
    }

    @Override
    public SendMessage handle(Message message) {
        final int userId = message.getFrom().getId();
        final UserProfileData userProfileData = dataCache.getUserProfileData(userId);

        dataCache.setUsersCurrentBotState(userId,BotState.SHOW_MAIN_MENU);

        return new SendMessage(message.getChatId(),
                String.format("%s%n -------------------%n" +
                                "Имя: %s%n" +
                                "Возраст: %d%n" +
                                "Пол: %s%n" +
                                "Любимая цифра: %d%n" +
                                "Цвет: %s%n" +
                                "Фильм: %s%n" +
                                "Песня: %s%n",
                "Данные по вашей анкете",
                userProfileData.getName(),
                userProfileData.getAge(),
                userProfileData.getGender(),
                userProfileData.getNumber(),
                userProfileData.getColor(),
                userProfileData.getMovie(),
                userProfileData.getSong()));
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_USER_PROFILE;
    }
}
