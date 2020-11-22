package vn.prostylee.core.configuration.swagger;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

/**
 * Override default petstore spec at swagger-ui/index.html page
 */
@Configuration
public class SpringdocSwaggerFix implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**/*.html")
                .addResourceLocations("classpath:/META-INF/resources/webjars/")
                .resourceChain(false)
                .addResolver(new WebJarsResourceResolver())
                .addResolver(new PathResourceResolver())
                .addTransformer(new IndexPageTransformer());
    }

    public class IndexPageTransformer implements ResourceTransformer {

        private String overwritePetStore(String html) {
            return html.replace("https://petstore.swagger.io/v2/swagger.json",
                    "/api/v3/api-docs");
        }

        @Override
        public org.springframework.core.io.Resource transform(HttpServletRequest request,
                                                              org.springframework.core.io.Resource resource, ResourceTransformerChain transformerChain) throws IOException {
            if (resource.getURL().toString().endsWith("/index.html")) {
                String html = getHtmlContent(resource);
                html = overwritePetStore(html);
                return new TransformedResource(resource, html.getBytes());
            } else {
                return resource;
            }
        }

        private String getHtmlContent(org.springframework.core.io.Resource resource) {
            try {
                InputStream inputStream = resource.getInputStream();
                java.util.Scanner s = new java.util.Scanner(inputStream).useDelimiter("\\A");
                String content = s.next();
                inputStream.close();
                return content;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
