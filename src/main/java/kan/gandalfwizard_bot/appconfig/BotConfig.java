package kan.gandalfwizard_bot.appconfig;


import kan.gandalfwizard_bot.GandalfWizardTelegramBot;
import kan.gandalfwizard_bot.botapi.TelegramFacade;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig {
    private String webHookPath;
    private String botUserName;
    private String botToken;

//    private DefaultBotOptions.ProxyType proxyType;
//    private String proxyHost;
//    private int proxyPort;

    @Bean
    public GandalfWizardTelegramBot GandalfWizardTelegramBot(TelegramFacade telegramFacade){
        DefaultBotOptions options = ApiContext.getInstance(DefaultBotOptions.class);


//        options.setProxyHost(proxyHost);
//        options.setProxyPort(proxyPort);
//        options.setProxyType(proxyType);

        GandalfWizardTelegramBot bot = new GandalfWizardTelegramBot(options,telegramFacade);
        bot.setBotToken(botToken);
        bot.setBotUserName(botUserName);
        bot.setWebHookPath(webHookPath);

        return bot;

    }

    @Bean
    public MessageSource messageSource(){
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }


}
