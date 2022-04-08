package com.tuturial.apidemo.Spring.tuturial.controllers;

import com.tuturial.apidemo.Spring.tuturial.models.Product;
import com.tuturial.apidemo.Spring.tuturial.models.ResponseObject;
import com.tuturial.apidemo.Spring.tuturial.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.convert.Delimiter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//        - Viết các Rest API: GET, POST, PUT, DELETE trong Java Spring Boot
//        - Kết nối cơ sở dữ liệu MySQL sử dụng Docker
//        - Upload file, ảnh lên Server
//        - Controllers, Repositories, Services  trong ứng dụng Spring Boot
//        - Sử dụng Dependency Injection, Service Injection

@RestController                                     //Báo cho Spring biết class này là Controller
@RequestMapping(path = "/api/v1/Products")       //Gửi request đến cho Controller
public class ProductController {
    //DI = Dependency Injection
    @Autowired                                      //Đối tượng ProductRepository(kho/danh sach) sẽ đc tạo ra ngay khi app đc tạo - chỉ cần tạo 1 lần
    private ProductRepository repository;

    //GET
    //Tìm kiếm tất cả Product
    @GetMapping("")
    //http://localhost:8080/api/v1/Products
    List<Product> getAllProducts(){
        return repository.findAll();                //Data?
    }

    //Tìm kiếm product theo Id, trả về format: Status, message, data
    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> findById(@PathVariable Long id){
        Optional<Product> foundProduct = repository.findById(id);
        return foundProduct.isPresent()?            //= if...else
             ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok","Query product successfully", foundProduct)
             ):
             ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Cannot find product with id = "+id,"")
             );
    }

    //POST
    //Postman: Raw,JSON
    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertProduct(@RequestBody Product newProduct){
        //Product không được trùng tên
        List<Product> foundProducts = repository.findByProductName(newProduct.getProductName().trim());
        return foundProducts.size()>0?
                ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                  new ResponseObject("failed","Insert Product Failed. Product name has already existed","")
                ):
                ResponseEntity.status(HttpStatus.OK).body(
                  new ResponseObject("ok","Insert Product Successfully", repository.save(newProduct))
        );
    }

    //PUT
    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateProduct(@RequestBody Product newProduct, @PathVariable Long id){
        Product updateProduct = repository.findById(id).map(product -> {
            product.setProductName(newProduct.getProductName());
            product.setYear(newProduct.getYear());
            product.setPrice(newProduct.getPrice());
            product.setUrl(newProduct.getUrl());
            return repository.save(product);
        }).orElseGet(()->{
            newProduct.setId(id);
            return  repository.save(newProduct);
        });
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok","Update Product Successfully",updateProduct)
        );
    }

    //DELETE
    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteProduct(@PathVariable Long id) {
        boolean exists = repository.existsById(id);
        if (exists) {
            repository.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Delete Product Successfully", "")
            );
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Delete Product Failed", "")
            );
        }
    }
}
