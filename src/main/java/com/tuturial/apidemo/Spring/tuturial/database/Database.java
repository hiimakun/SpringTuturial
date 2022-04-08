package com.tuturial.apidemo.Spring.tuturial.database;

import com.tuturial.apidemo.Spring.tuturial.models.Product;
import com.tuturial.apidemo.Spring.tuturial.repositories.ProductRepository;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;




@Configuration                                  //chứa các bean method - gọi ngay khi ứng dụng chạy
public class Database {
    //logger
    private  static  final Logger logger = LoggerFactory.getLogger(Database.class);
    @Bean
    //tự động insert data vào database ban đầu
    CommandLineRunner initDatabase(ProductRepository productRepository){
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                Product product1 = new Product("IPhone 11 pro",2021,10000.0,"");
                Product product2 = new Product("Redmi Note 13",2022,20000.0,"");
                logger.info("insert data: "+ productRepository.save(product1));
                logger.info("insert data: "+ productRepository.save(product2));
            }
        };

        }

    }

