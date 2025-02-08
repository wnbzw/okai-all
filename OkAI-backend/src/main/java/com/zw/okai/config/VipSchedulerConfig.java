package com.zw.okai.config;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
@Data
public class VipSchedulerConfig {

    @Bean
    public Scheduler vipScheduler() {
        ThreadFactory threadFactory = new ThreadFactory() {
            private final AtomicInteger counter = new AtomicInteger(1);

            /**
             *  创建一个线程
             * @param r a runnable to be executed by new thread instance
             * @return
             */
            @Override
            public Thread newThread(@NotNull Runnable r) {
                Thread t=new Thread(r,"VIPThreadPool-"+counter.getAndIncrement());
                t.setDaemon(false);//设置为非守护线程
                return t;
            }
        };
        // 创建一个线程池
       ExecutorService executor = Executors.newScheduledThreadPool(10,threadFactory);
        return Schedulers.from(executor);
    }
}
