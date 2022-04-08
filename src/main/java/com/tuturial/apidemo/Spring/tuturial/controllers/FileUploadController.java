package com.tuturial.apidemo.Spring.tuturial.controllers;

import com.tuturial.apidemo.Spring.tuturial.models.ResponseObject;
import com.tuturial.apidemo.Spring.tuturial.services.IStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping(path = "api/v1/FileUpload")
public class FileUploadController {
    @Autowired
    private IStorageService storageService;
    @PostMapping("")
    public ResponseEntity<ResponseObject> uploadFile(@RequestParam("file")MultipartFile file){
       try {
           //Lưu file vào thư mục - sử dụng server
           String genaratedFileName = storageService.storeFile(file);
           return ResponseEntity.status(HttpStatus.OK).body(
                   new ResponseObject("ok","Upload File Successfully", genaratedFileName)
           );
       }catch (Exception exception){
            return  ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", exception.getMessage(), "")
            );
       }
    }
    //get image's URL
    //đọc chi tiết file
    @GetMapping("/file/{fileName:.+}")
    public ResponseEntity<byte[]> readDetailFile(@PathVariable String fileName){
        try{
            byte[] bytes = storageService.readFileContent(fileName);
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(bytes);
        }catch (Exception exception){
            return ResponseEntity.noContent().build();
        }
    }
    //load all file thành 1 list
    @GetMapping("")
    public ResponseEntity<ResponseObject> getUploadFile(){
        try{
            List<String> urls = storageService.loadAll()
                    .map(path -> {                              //để dễ dàng debug thì nên đặt thành 1 khối lệnh
                        String urlPath = MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
                                "readDetailFile", path.getFileName().toString())
                                .build().toUri().toString();
                        return urlPath;
                    })
                    .collect(Collectors.toList());
            return  ResponseEntity.ok(new ResponseObject("ok", "Load File List Successfully", urls));
        }catch (Exception exception){
            return ResponseEntity.ok(new ResponseObject("failed","Load File List Failed", new String[]{}));
        }

    }

}
