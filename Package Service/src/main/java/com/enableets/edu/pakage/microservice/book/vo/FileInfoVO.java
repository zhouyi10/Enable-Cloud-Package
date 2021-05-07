package com.enableets.edu.pakage.microservice.book.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileInfoVO {

    private String fileId;

    private String fileName;

    private String fileExt;

    private String url;

    private String md5;

    private Long size;

    private String sizeDisplay;


}
