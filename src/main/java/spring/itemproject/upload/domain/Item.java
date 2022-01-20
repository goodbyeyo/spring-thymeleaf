package spring.itemproject.upload.domain;

import lombok.Data;

import java.util.List;

@Data
public class Item {

    private Long id;
    private String itemName;
    private UploadFile attachFIle;
    private List<UploadFile> imageFiles;



}
