package gxj.study.config;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.google.common.base.Throwables;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UrlPathHelper;

import java.text.ParseException;
import java.util.Date;

/**
 * @author xinjie_guo
 * @version 1.0.0 createTime:  2021/10/27 11:03
 * @description
 */
@Configuration(proxyBeanMethods = false)
@Slf4j
public class WebConfig {

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void configurePathMatch(PathMatchConfigurer configurer) {
                UrlPathHelper urlPathHelper = new UrlPathHelper();
                //取消移除分号，即允许request的矩阵变量
                urlPathHelper.setRemoveSemicolonContent(false);
                configurer.setUrlPathHelper(urlPathHelper);
            }

            @Override
            public void addFormatters(FormatterRegistry registry) {
                registry.addConverter(new Converter<String, Date>() {

                    @Override
                    public Date convert(String source) {
                        //reference：SpringBoot笔记.txt#SpringBoot Web请求参数解析原理#客制化
                        if (StringUtils.isBlank(source)) {
                            return null;
                        }
                        try {
                            return DateUtils.parseDate(source, "yyyyMMdd");
                        } catch (ParseException e) {
                            log.error(Throwables.getStackTraceAsString(e));
                            throw new RuntimeException("日期转换错误 source:" + source);
                        }
                    }
                });
            }
        };
    }

}
