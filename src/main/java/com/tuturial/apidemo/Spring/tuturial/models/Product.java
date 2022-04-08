package com.tuturial.apidemo.Spring.tuturial.models;

import net.bytebuddy.dynamic.loading.InjectionClassLoader;
import org.springframework.context.annotation.EnableMBeanExport;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Objects;

@Entity                                     //Báo đây là 1 thực thể
@Table(name = "tblProduct")                 //Thực thể tương ứng với 1 bảng
public class Product {
    //primary key
    @Id
    ////GeneratedValue - auto increment
    //@GeneratedValue(strategy = GenerationType.AUTO)
    //Ngoài ra có thể sử dụng sequence: tạo ra 1 quy tắc khi thêm mới, tạo ra giá trị của tường Id
    @SequenceGenerator(
            name = "product_sequence",
            sequenceName = "product_sequence",
            allocationSize = 1 //imp by 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_sequence"
    )
    private Long id;
    //validate = constraint
    @Column(nullable = false /*không được null*/,unique = true /*độc nhất*/,length = 300 )
    private String productName;
    private int year;
    private Double price;
    private String url;
    //defaul constructor
    public  Product(){}

    public Product(String productName, int year, Double price, String url) {
        this.productName = productName;
        this.year = year;
        this.price = price;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", productName='" + productName + '\'' +
                ", year=" + year +
                ", price=" + price +
                ", url='" + url + '\'' +
                '}';
    }
    //calculated field = transient
    //Tính tuổi
    @Transient
    private  int age; //=year-year
    public int getAge(){
        return Calendar.getInstance().get(Calendar.YEAR) - year;
    }
    //hàm equals : so sánh 2 đối tượng khác nhau
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return year == product.year && age == product.age && Objects.equals(id, product.id) && Objects.equals(productName, product.productName) && Objects.equals(price, product.price) && Objects.equals(url, product.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productName, year, price, url, age);
    }
}
