/**
 * 
 */
package com.fds;

import javax.sql.DataSource;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.vaadin.server.VaadinServlet;

/**
 * tanvh1 Oct 23, 2019
 *
 */
public class SecurityDataSourceConfig {
	private SpringConfigurationValueHelper configurationHelper;
	
    @Bean
    @ConfigurationProperties(prefix="spring.datasourcedw")
    public DataSourceProperties securityDataSourceProperties() {
        return new DataSourceProperties();
    }
 
    @Bean
    public DataSource securityDataSource() {
    	final SpringContextHelper helper = new SpringContextHelper(VaadinServlet.getCurrent().getServletContext());
		configurationHelper = (SpringConfigurationValueHelper) helper.getBean("springConfigurationValueHelper");
		
    	StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword("FDS");
        String decrypted = encryptor.decrypt(configurationHelper.getPasswordDatasource2nd());
        
         return DataSourceBuilder.create()
        .driverClassName("oracle.jdbc.driver.OracleDriver")
        .url(configurationHelper.getUrlDatasource2nd())
        .username(configurationHelper.getUsernameDatasource2nd())
        .password(decrypted)
        .build();
    }
    

}
