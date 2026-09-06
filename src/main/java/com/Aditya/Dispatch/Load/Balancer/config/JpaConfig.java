package com.Aditya.Dispatch.Load.Balancer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.Aditya.Dispatch.Load.Balancer.repository")
public class JpaConfig {
}
