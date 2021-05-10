package com.fds;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Dung de ho tro get value tu file config cho component khong quan ly boi
 * Spring
 */
@Component
public class SpringConfigurationValueHelper {

	@Value("${path.template.report}")
	private String pathTempReport;

	@Value("${time.refresh.content}")
	private int sTimeRefreshContent;
	
	@Value("${spring.datasource2nd.url}")
	private String urlDatasource2nd;
	
	@Value("${spring.datasource2nd.username}")
	private String usernameDatasource2nd;
	
	@Value("${spring.datasource2nd.password}")
	private String passwordDatasource2nd;
	
	@Value("${spring.datasource2nd.turnon}")
	private String turnonDatasource2nd;
	
	public String getPathTemplateReport() {
		return pathTempReport;
	}

	public int sTimeRefreshContent() {
		return sTimeRefreshContent;
	}

	/**
	 * @return the urlDatasource2nd
	 */
	public String getUrlDatasource2nd() {
		return urlDatasource2nd;
	}

	/**
	 * @return the usernameDatasource2nd
	 */
	public String getUsernameDatasource2nd() {
		return usernameDatasource2nd;
	}

	/**
	 * @return the passwordDatasource2nd
	 */
	public String getPasswordDatasource2nd() {
		return passwordDatasource2nd;
	}

	/**
	 * @return the turnonDatasource2nd
	 */
	public String getTurnonDatasource2nd() {
		return turnonDatasource2nd;
	}

}
