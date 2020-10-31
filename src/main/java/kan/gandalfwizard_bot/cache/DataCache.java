package kan.gandalfwizard_bot.cache;

import kan.gandalfwizard_bot.botapi.BotState;
import kan.gandalfwizard_bot.botapi.handlers.fillingprofile.UserProfileData;

public interface DataCache {
    void setUsersCurrentBotState(int userId, BotState botState);

    BotState getUsersCurrentBotState(int userId);

    UserProfileData getUserProfileData(int userId);

    void saveUserProfileData(int userId, UserProfileData userProfileData);

}
