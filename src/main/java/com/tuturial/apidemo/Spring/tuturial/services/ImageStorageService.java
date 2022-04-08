package com.tuturial.apidemo.Spring.tuturial.services;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Stream;

@Service    //thông báo là 1 services
public class ImageStorageService implements IStorageService {
    //Tạo đường dẫn  tới thư mục
    private  final Path storageFolder = Paths.get("uploads");
    public  ImageStorageService() {
        try {
            Files.createDirectories(storageFolder);         //Tạo thư mục với đường dẫn là storageFolder
            System.out.println("Directory is created!");
        }catch(Exception exception){
            throw  new RuntimeException("Failed to create directory!", exception);
        }
    }
    @Override
    public String storeFile(MultipartFile file) {
        try {
            System.out.println("True");
            if(file.isEmpty()){
                throw new RuntimeException("Failed to store empty file");
            }
            //kiểm tra xem có phải file ảnh hay k?
            if(!isImageFile(file)){
                throw new RuntimeException("Upload file must be image file");
            }
            //kiếm tra dung lượng <= 5Mb
            float fileSIzeInMb = file.getSize()/1_000_000.0f;
            if(fileSIzeInMb > 5){
                throw new RuntimeException("Upload file must be <= 5Mb");
            }
            //rename file - tránh trùng lặp tên file
            String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
            String generatedFileName = UUID.randomUUID().toString().replace("-","");
            generatedFileName = generatedFileName+"."+fileExtension;

            Path destinationFilePath = this.storageFolder.resolve(Paths.get(generatedFileName)).normalize().toAbsolutePath();
            if(!destinationFilePath.getParent().equals(this.storageFolder.toAbsolutePath())){
                throw new RuntimeException("Cannot store file outside current directory");
            }
            try(InputStream inputStream = file.getInputStream()){
                Files.copy(inputStream, destinationFilePath, StandardCopyOption.REPLACE_EXISTING);
            }
            return generatedFileName;
        }
        catch(IOException exception){
            throw  new RuntimeException("Failed to store file", exception);
        }
    }
    private boolean isImageFile(MultipartFile file){
        String fileExtension = FilenameUtils.getExtension((file.getOriginalFilename()));
        return Arrays.asList((new String[] {"png", "jpg","jpeg","bnp"})).contains(fileExtension.trim().toLowerCase());
    }

    //load tất cả ảnh
    @Override
    public Stream<Path> loadAll() {
        try{
            //filter: 1 biểu thức lamda lọc những path k phù hợ
            return Files.walk(this.storageFolder,1)
                    .filter(path ->  !path.equals(this.storageFolder) && !path.toString().contains("._"))
                    .map(this.storageFolder::relativize);

        }catch (IOException exception){
            throw new RuntimeException("Failed to load file", exception);
        }

    }

    @Override
    public byte[] readFileContent(String fileName) {
        try{
            Path file = storageFolder.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()){
                byte[] bytes;
                bytes = StreamUtils.copyToByteArray(resource.getInputStream());
                return bytes;
            }else {
                throw new RuntimeException("Could not read file "+fileName);
            }
        } catch (IOException exception) {
            throw new RuntimeException("Could not read file "+fileName,exception);
        }
    }

    @Override
    public void deleteAllFile() {

    }

}
