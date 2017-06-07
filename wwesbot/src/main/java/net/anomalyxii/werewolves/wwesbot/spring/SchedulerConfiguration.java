package net.anomalyxii.werewolves.wwesbot.spring;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * Spring {@link Configuration} for a Quartz {@link Scheduler}.
 * <p>
 * Created by Anomaly on 07/06/2017.
 */
public class SchedulerConfiguration {

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
        Scheduler scheduler = sf.getScheduler();
        scheduler.start();
        return scheduler;
    }

}
