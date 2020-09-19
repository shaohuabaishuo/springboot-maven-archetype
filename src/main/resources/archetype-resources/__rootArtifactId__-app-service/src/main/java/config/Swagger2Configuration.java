package ${package}.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Swagger分类，controller和facade配置分离
 *
 * @author jingzhong
 */
@ConditionalOnClass(Swagger2Configuration.class)
@ConditionalOnProperty(value = "common.enable-swagger", havingValue = "true")
@Configuration
@EnableSwagger2
public class Swagger2Configuration {

    @Bean
    public Docket controllerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("controller")
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("${package}.controller"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(buildParameter())
                .apiInfo(buildApiInfo());
    }

    @Bean
    public Docket facadeApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("facade")
                .useDefaultResponseMessages(false)
                .forCodeGeneration(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("${package}.facade"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(buildParameter())
                .apiInfo(buildApiInfo());
    }


    private ApiInfo buildApiInfo() {
        return new ApiInfoBuilder().title("jz测试API").version("1.0.0").build();
    }

    private List<Parameter> buildParameter() {
        //添加head参数配置start
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<>();
        tokenPar.name("user")
                .description("用户")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(true)
                .defaultValue("{\"userName\":\"jingzhong@xx.cn\", \"email\":\"jingzhong@xx.cn\", \"userId\":\"123\"}")
                .order(0)
                .build();
        pars.add(tokenPar.build());
        return pars;
    }
}
