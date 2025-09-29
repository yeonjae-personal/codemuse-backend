package com.lgcns.svcp.prod.config;

import java.util.List;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.lgcns.svcp.prod.interceptor.AuditInterceptor;

@Configuration
public class MyBatisConfig {

	@Bean
	public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}

	@Bean
	public Interceptor auditInterceptor() {
		return new AuditInterceptor();
	}

	@Bean
	public ConfigurationCustomizer configurationCustomizer(List<Interceptor> interceptors) {
		return configuration -> {
			for (Interceptor interceptor : interceptors) {
				configuration.addInterceptor(interceptor);
			}
		};
	}
}
