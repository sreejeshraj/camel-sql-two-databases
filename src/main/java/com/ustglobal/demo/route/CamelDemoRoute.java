package com.ustglobal.demo.route;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Component
@ConfigurationProperties(prefix = "camel-demo-route")
@Data
@EqualsAndHashCode(callSuper = true)

public class CamelDemoRoute extends RouteBuilder {

	// The value of this property is injected from application.properties based on
	// the profile chosen.
	// private String injectedName;

	private String query;
	private String queryResultTopic;
	private String queryInputTopic;
	private String brokers;
	private String timerFrequency;
	
	/*
	 * @Autowired private JdbcTemplate jdbcTemplate;
	 */
	@Override
	public void configure() throws Exception {

		// @formatter:off
		
		// errorHandler(deadLetterChannel("seda:errorQueue").maximumRedeliveries(5).redeliveryDelay(1000));
		
		//SELECT name FROM person WHERE id=1;
		
		//Table creation script
		/*
		
		CREATE TABLE `person` (
				`id` INT(11) NOT NULL AUTO_INCREMENT,
				`Name` VARCHAR(5) NOT NULL,
				PRIMARY KEY (`id`)
			)
			COLLATE='latin1_swedish_ci'
			ENGINE=InnoDB
			AUTO_INCREMENT=12
			;
		*/
						
		from("timer://dbQueryTimer?period=10s")
		.routeId("DATABASE_QUERY_TIMER_ROUTE")
		//setting constant JSON string {"id":1} as body
		.setBody(constant("{\"id\":1}"))
		.unmarshal().json(JsonLibrary.Jackson)
		.log("After unmarshal body:${body}")
		.log("unmarshal body type:${body.class.name}")
		//.to("sql:SELECT version()?dataSource=#dataSource")	
		.to("sql:SELECT name FROM person WHERE id=:#id?dataSource=#primaryDataSource")
		.log(LoggingLevel.INFO,"******Database query executed - body:${body}******")
		.to("sql:SELECT * FROM customer?dataSource=#secondaryDataSource")
		.log(LoggingLevel.INFO,"******Secondary Database query executed - body:${body}******");
		
		
		// @formatter:on

	}

}
