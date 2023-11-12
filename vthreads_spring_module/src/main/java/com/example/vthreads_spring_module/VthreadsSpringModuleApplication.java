package com.example.vthreads_spring_module;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.concurrent.Executors;

@SpringBootApplication
public class VthreadsSpringModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(VthreadsSpringModuleApplication.class, args);
    }

    @RestController
    public static class MyController {

        private final MyService myService;

        public MyController(MyService myService) {
            this.myService = myService;
        }

        @GetMapping("/data")
        public String getMessage() {
            return myService.generateMessage();
        }
    }

    @Service
    public static class MyService {
        public String generateMessage() {
            return "answer";
        }
    }

    @Bean(TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME)
    public AsyncTaskExecutor asyncTaskExecutor() {
        return new TaskExecutorAdapter(Executors.newVirtualThreadPerTaskExecutor());
    }

    @Bean
    public TomcatProtocolHandlerCustomizer<?> protocolHandlerVirtualThreadExecutorCustomizer() {
        return protocolHandler -> {
            protocolHandler.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
        };
    }

}
