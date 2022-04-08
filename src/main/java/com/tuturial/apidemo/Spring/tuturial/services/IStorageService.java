package com.tuturial.apidemo.Spring.tuturial.services;

import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface IStorageService {
    public String storeFile(MultipartFile file);
    public Stream<Path> loadAll();  //load taats car file been trong thuw mucj chuwas anh
    public  byte[] readFileContent(String fileName);
    public void deleteAllFile();
}
