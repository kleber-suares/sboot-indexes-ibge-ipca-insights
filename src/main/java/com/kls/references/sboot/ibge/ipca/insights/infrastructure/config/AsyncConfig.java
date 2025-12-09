package com.kls.references.sboot.ibge.ipca.insights.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    public static final String IPCA_DATA_IMPORT_EXECUTOR = "ipcaImportExecutor";

    @Override
    @Bean(name = IPCA_DATA_IMPORT_EXECUTOR)
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(50);
        executor.setTaskDecorator(new MdcTaskDecorator());
        executor.setThreadNamePrefix("ipca-import-");
        executor.initialize();

        return executor;
    }
}
