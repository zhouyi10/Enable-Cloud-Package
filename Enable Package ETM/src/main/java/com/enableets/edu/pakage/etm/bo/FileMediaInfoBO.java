package com.enableets.edu.pakage.etm.bo;

import lombok.AllArgsConstructor;
import lombok.Data;


@AllArgsConstructor
@Data
public class FileMediaInfoBO  {


    public FileMediaInfoBO(){}

    public FileMediaInfoBO(String url){
        this.url = url;
    }

    private String fileId;

    private String fileName;

    private String fileExt;

    private String url;
}
