package kan.gandalfwizard_bot.botapi.handlers.askdestiny;

import kan.gandalfwizard_bot.botapi.BotState;
import kan.gandalfwizard_bot.botapi.InputMessageHandler;
import kan.gandalfwizard_bot.cache.UserDataCache;
import kan.gandalfwizard_bot.service.ReplyMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

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
        long chatId = message.getChatId();

        SendMessage replyToUser = replyMessageService.getReplyMessage(chatId,"reply.askDestiny");
        replyToUser.setReplyMarkup(getInlineMessageButtons());
        return replyToUser;
    }

    private InlineKeyboardMarkup getInlineMessageButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        InlineKeyboardButton buttonYes = new InlineKeyboardButton().setText("Да");
        InlineKeyboardButton buttonNo = new InlineKeyboardButton().setText("Нет, спасибо");
        InlineKeyboardButton buttonIwillThink = new InlineKeyboardButton().setText("Я подумаю");
        InlineKeyboardButton buttonIdontKnow = new InlineKeyboardButton().setText("Еще не определился");

        //Every button must have callBackData, or else not work !
        buttonYes.setCallbackData("buttonYes");
        buttonNo.setCallbackData("buttonNo");
        buttonIwillThink.setCallbackData("buttonIwillThink");
        buttonIdontKnow.setCallbackData("-");

        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(buttonYes);
        keyboardButtonsRow1.add(buttonNo);

        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow2.add(buttonIwillThink);
        keyboardButtonsRow2.add(buttonIdontKnow);


        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ASK_DESTINY;
    }
}
