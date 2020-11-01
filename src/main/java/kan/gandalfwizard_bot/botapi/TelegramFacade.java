package kan.gandalfwizard_bot.botapi;


import kan.gandalfwizard_bot.botapi.handlers.fillingprofile.UserProfileData;
import kan.gandalfwizard_bot.cache.UserDataCache;
import kan.gandalfwizard_bot.service.MainMenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class TelegramFacade {
    private BotStateContext botStateContext;
    private UserDataCache userDataCache;
    private MainMenuService mainMenuService;

    public TelegramFacade(BotStateContext botStateContext, UserDataCache userDataCache, MainMenuService mainMenuService) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
        this.mainMenuService = mainMenuService;
    }


    public BotApiMethod<?> handleUpdate(Update update) {
        SendMessage replyMessage = null;

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info("New callbackQuery from user: {}, userId: {}, with data: {}",
                    callbackQuery.getFrom().getUserName(),
                    callbackQuery.getFrom().getId(),
                    callbackQuery.getData()
            );
            return processCallbackQuery(callbackQuery);
        }

        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.info("New message from user: {}, chatId: {}, with text: {}",
                    message.getFrom(), message.getChatId(), message.getText());
            replyMessage = handleInputMessage(message);
        }

        return replyMessage;

    }

    private BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery) {
        final long chatId = buttonQuery.getMessage().getChatId();
        final int userId = buttonQuery.getFrom().getId();

        BotApiMethod<?> callBackAnswer = mainMenuService.getMainMenuMessage(chatId, "Воспользуйтес главным меню!");

        if (buttonQuery.getData().equals("buttonYes")) {
            callBackAnswer = new SendMessage(chatId, "Как тебя зовут?");
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_AGE);
        } else if (buttonQuery.getData().equals("buttonNo")) {
            callBackAnswer = sendAnswerCallbackQuery("Возвращайся когда будешь готов!!", false, buttonQuery);

        } else if (buttonQuery.getData().equals("buttonIwillThink")) {
            callBackAnswer = sendAnswerCallbackQuery("Возвращайся когда будешь готов!!", true, buttonQuery);
        }

        //From Gender choose buttons
        else if (buttonQuery.getData().equals("buttonMan")) {
            UserProfileData userProfileData = userDataCache.getUserProfileData(userId);
            userProfileData.setGender("М");
            userDataCache.saveUserProfileData(userId, userProfileData);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_NUMBER);
            callBackAnswer = new SendMessage(chatId, "Твой любимый цвет");
        } else if (buttonQuery.getData().equals("buttonWoman")) {
            UserProfileData userProfileData = userDataCache.getUserProfileData(userId);
            userProfileData.setGender("Ж");
            userDataCache.saveUserProfileData(userId, userProfileData);
            userDataCache.setUsersCurrentBotState(userId, BotState.ASK_NUMBER);
            callBackAnswer = new SendMessage(chatId, "Твоя любимый цвет");

        } else {
            userDataCache.setUsersCurrentBotState(userId, BotState.SHOW_MAIN_MENU);
        }


        return callBackAnswer;
    }

    private AnswerCallbackQuery sendAnswerCallbackQuery(String text, boolean alert, CallbackQuery callbackquery) {
        AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
        answerCallbackQuery.setCallbackQueryId(callbackquery.getId());
        answerCallbackQuery.setShowAlert(alert);
        answerCallbackQuery.setText(text);
        return answerCallbackQuery;
    }

    private SendMessage handleInputMessage(Message message) {
        String inputMsg = message.getText();
        int userId = message.getFrom().getId();
        BotState botState;

        SendMessage replyMessage;

        switch (inputMsg) {
            case "/start":
                botState = BotState.ASK_DESTINY;
                break;
            case "Получить предсказание":
                botState = BotState.FILLING_PROFILE;
                break;
            case "Помощь":
                botState = BotState.SHOW_HELP_MENU;
                break;
            case "Моя анкета":
                botState = BotState.SHOW_USER_PROFILE;
                break;
            default:
                botState = userDataCache.getUsersCurrentBotState(userId);
                break;
        }

        userDataCache.setUsersCurrentBotState(userId, botState);

        replyMessage = botStateContext.processInputMessage(botState, message);

        return replyMessage;


    }
}
