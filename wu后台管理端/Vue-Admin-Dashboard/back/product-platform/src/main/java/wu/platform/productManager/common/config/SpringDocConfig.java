package wu.platform.productManager.common.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SpringDoc配置类
 * 配置API文档信息
 */
@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI mallOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("商品管理平台 API")
                        .description("基于DDD架构的商品管理平台API文档")
                        .version("v1.0.0")
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0"))
                        .contact(new Contact()
                                .name("巫杰龙")
                                .email("jielongwu58@gmail.com")
                                .url("https://www.mall.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("项目Wiki文档")
                        .url("https://github.com/mall/wiki"));
    }
} 