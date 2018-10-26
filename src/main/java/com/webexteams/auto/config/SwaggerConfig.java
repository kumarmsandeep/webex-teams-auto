package com.webexteams.auto.config;

import static springfox.documentation.builders.PathSelectors.regex;

import java.util.ArrayList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.google.common.collect.Ordering;
import com.webexteams.auto.filter.SwaggerUIRebrandFilter;
import com.webexteams.auto.utils.ManifestUtil;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiDescription;
import springfox.documentation.service.ApiListingReference;
import springfox.documentation.service.Operation;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@ComponentScan
@EnableSwagger2
public class SwaggerConfig {

	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2).enable(true).operationOrdering(new Ordering<Operation>() {

			@Override
			public int compare(Operation arg0, Operation arg1) {
				return Integer.compare(arg0.getMethod().ordinal(), arg1.getMethod().ordinal());
			}
		}).apiListingReferenceOrdering(new Ordering<ApiListingReference>() {
			@Override
			public int compare(ApiListingReference left, ApiListingReference right) {
				return Integer.compare(right.getPosition(), left.getPosition());
			}
		}).apiDescriptionOrdering(new Ordering<ApiDescription>() {

			@Override
			public int compare(ApiDescription left, ApiDescription right) {
				int leftPos = left.getOperations().size() == 1 ? left.getOperations().get(0).getMethod().ordinal() : 0;
				int rightPos = right.getOperations().size() == 1 ? right.getOperations().get(0).getMethod().ordinal()
						: 0;

				int position = Integer.compare(leftPos, rightPos);

				return position;
			}
		}).select().apis(RequestHandlerSelectors.basePackage("com.webexteams.auto")).paths(regex("/.*")).build()
				.tags(new Tag("Login", "-- Operations pertaining to Webex Teams login.", new ArrayList<>()))
				.apiInfo(new ApiInfoBuilder().title(ManifestUtil.getProjectTitle() + " - API doc")
						.version(ManifestUtil.getProjectVersion()).build());
	}

	@Bean
	public SwaggerUIRebrandFilter swaggerUIRebrandFilter() {
		return new SwaggerUIRebrandFilter();
	}
}
