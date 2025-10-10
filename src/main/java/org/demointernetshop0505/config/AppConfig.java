package org.demointernetshop0505.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class AppConfig {
    /*
    s3.accesskey= DO00QKF6YERHM7KMWCQM
s3.secretkey = 8RAulKF4zKO4Yj8g1u2Bp6iqvQLQd2KawBoKpmh5NJY
s3.endpoint = https://fra1.digitaloceanspaces.com
s3.bucket = group0505
s3.region = fra1
     */
    @Value("${s3.accesskey}")
    private String accessKey;

    @Value("${s3.secretkey}")
    private String secretKey;

    @Value("${s3.endpoint}")
    private String endpoint;

    @Value("${s3.region}")
    private String region;


    @Bean
    public Configuration freemakerConfiguration() {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_21);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateLoader(new ClassTemplateLoader(AppConfig.class, "/mail/"));
        return configuration;
    }

    @Bean
    public AmazonS3 amazonS3() {
        // аутентификация нашего приложения в DigitalOcean
        AWSCredentials credentials = new BasicAWSCredentials(
                accessKey,
                secretKey
        );

        //настройка точки подключения к хранилищу

        AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(
                endpoint,
                region
        );

        // создать класс-клиент для загрузки файлов

        AmazonS3ClientBuilder amazonS3ClientBuilder = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials));

        amazonS3ClientBuilder.setEndpointConfiguration(endpointConfiguration);

        AmazonS3 client = amazonS3ClientBuilder.build();

        // клиент для amazon / digital ocean - экземпляр класса, который содержит
        // в себе ВСЮ информацию о песте подключения и правах доступа

        return client;
    }

}
