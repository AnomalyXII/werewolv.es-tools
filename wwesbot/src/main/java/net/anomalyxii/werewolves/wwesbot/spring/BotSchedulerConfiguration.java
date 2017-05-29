package net.anomalyxii.werewolves.wwesbot.spring;

import net.anomalyxii.bot.api.Bot;
import net.anomalyxii.bot.api.scheduler.BotScheduler;
import net.anomalyxii.bot.impl.scheduler.QuartzBotScheduler;
import net.anomalyxii.werewolves.services.GameService;
import net.anomalyxii.werewolves.wwesbot.schedulers.GameCheckScheduler;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;
import java.util.function.Consumer;

/**
 * Configures a {@link BotScheduler}.
 * <p>
 * Created by Anomaly on 29/05/2017.
 */
@Configuration
public class BotSchedulerConfiguration {

    // ******************************
    // Beans
    // ******************************

    @Bean(destroyMethod = "shutdown")
    public static Scheduler scheduler() throws SchedulerException {
        Properties properties = new Properties();
        properties.setProperty(StdSchedulerFactory.PROP_SCHED_SKIP_UPDATE_CHECK, "true");
        properties.setProperty(StdSchedulerFactory.PROP_JOB_STORE_CLASS, "org.quartz.simpl.RAMJobStore");
        properties.setProperty(StdSchedulerFactory.PROP_THREAD_POOL_CLASS, "org.quartz.simpl.SimpleThreadPool");
        properties.setProperty(StdSchedulerFactory.PROP_THREAD_POOL_PREFIX + ".threadCount", "2");

        SchedulerFactory sf = new StdSchedulerFactory(properties);
        return sf.getScheduler();
    }

    @Bean
    @Autowired
    public static BotScheduler botScheduler(Scheduler scheduler) {
        return new QuartzBotScheduler(scheduler);
    }

    @Bean
    @Autowired
    public static GameCheckScheduler gameCheckScheduler(GameService service) {
        return new GameCheckScheduler(service);
    }

}
