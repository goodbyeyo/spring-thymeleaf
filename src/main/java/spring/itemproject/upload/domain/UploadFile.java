package spring.itemproject.upload.domain;

import lombok.Data;

@Data
public class UploadFile {

    private String uploadFileName;  // 고객이 업로드한 파일명
    private String storeFIleName;   // 서버내부에서 관리하는 파일명

    public UploadFile(String uploadFileName, String storeFIleName) {
        this.uploadFileName = uploadFileName;
        this.storeFIleName = storeFIleName;
    }
}
