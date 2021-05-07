package com.enableets.edu.pakage.framework.book.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookFileInfoBO {

    private String fileId;

    private String fileName;

    private String fileExt;

    private String url;

    private String md5;

    private Long size;

    private String sizeDisplay;


}
