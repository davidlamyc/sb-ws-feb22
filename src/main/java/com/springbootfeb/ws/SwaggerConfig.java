package com.springbootfeb.ws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.VendorExtension;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Configuration
//@EnableSwagger2
public class SwaggerConfig {

    Contact contact = new Contact(
        "Person Name",
            "www.mysite.com",
            "mymail@mail.com"
    );

    List<VendorExtension> vendorExtensions = new ArrayList<>();

    ApiInfo apiInfo = new ApiInfo(
            "WS docs",
            "This page documents the API",
            "1.0",
            "http://www.localhost.com",
            contact,
            "Apache 2.0",
            "www.apache.org/licenses",
            vendorExtensions
    );

    @Bean
    public Docket aipDocket() {
        Docket docket = new Docket(DocumentationType.OAS_30)
                .protocols(new HashSet<>(Arrays.asList("HTTP", "HTTPS")))
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.springbootfeb.ws"))
                .paths(PathSelectors.any())
                .build();

        return docket;
    }

}
