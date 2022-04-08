package com.tuturial.apidemo.Spring.tuturial.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.util.Calendar;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
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

    //calculated field = transient
    //Tính tuổi
    @Transient
    private  int age; //=presentYear-year
    public int getAge(){
        return Calendar.getInstance().get(Calendar.YEAR) - year;
    }

}
