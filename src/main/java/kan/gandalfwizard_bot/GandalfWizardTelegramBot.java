package kan.gandalfwizard_bot;

import kan.gandalfwizard_bot.botapi.TelegramFacade;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class GandalfWizardTelegramBot extends TelegramWebhookBot {
    private String webHookPath;
    private String botUserName;
    private String botToken;

    private TelegramFacade telegramFacade;

    public GandalfWizardTelegramBot(DefaultBotOptions options,TelegramFacade telegramFacade) {
        super(options);
        this.telegramFacade = telegramFacade;
    }

    @Override
    public BotApiMethod onWebhookUpdateReceived(Update update) {
//        if (update.getMessage() != null && update.getMessage().hasText()) {
//            long chatId = update.getMessage().getChatId();
//
//            try {
//                execute(new SendMessage(chatId, "Hi, " + update.getMessage().getText()));
//
//            } catch (TelegramApiException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;

        SendMessage replyMessageToUser = telegramFacade.handleUpdate(update);

        return replyMessageToUser;
    }

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotPath() {
        return webHookPath;
    }

    public void setWebHookPath(String webHookPath) {
        this.webHookPath = webHookPath;
    }

    public void setBotUserName(String botUserName) {
        this.botUserName = botUserName;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }
}
