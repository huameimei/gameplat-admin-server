package com.gameplat.admin.config;

import com.gameplat.security.authz.URIAdapter;
import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

@Configuration
@EnableOpenApi
@EnableKnife4j
public class Swagger2Config {

  @Autowired private URIAdapter uriAdapter;

  @Bean
  public Docket createRestApi() {
    return new Docket(DocumentationType.OAS_30)
        .apiInfo(apiInfo())
        .securityContexts(this.securityContext())
        .securitySchemes(this.securitySchemes())
        .groupName("1.0")
        .select()
        .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))
        .paths(PathSelectors.any())
        .build();
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder().title("租户后台API文档").description("API文档").version("1.0").build();
  }

  private List<SecurityContext> securityContext() {
    return Collections.singletonList(
        SecurityContext.builder()
            .securityReferences(this.securityReferences())
            .operationSelector(this.forExcludeAntPaths())
            .build());
  }

  private Predicate<OperationContext> forExcludeAntPaths() {
    return (context) ->
        Arrays.stream(uriAdapter.getPermitUri())
            .noneMatch(url -> PathSelectors.ant(url).test(context.requestMappingPattern()));
  }

  private List<SecurityReference> securityReferences() {
    SecurityReference securityReference =
        SecurityReference.builder()
            .scopes(new AuthorizationScope[0])
            .reference("Authorization")
            .build();
    return Collections.singletonList(securityReference);
  }

  private List<SecurityScheme> securitySchemes() {
    return Collections.singletonList(
        HttpAuthenticationScheme.JWT_BEARER_BUILDER
            .name("Authorization")
            .description("Authorization Bearer")
            .build());
  }
}
