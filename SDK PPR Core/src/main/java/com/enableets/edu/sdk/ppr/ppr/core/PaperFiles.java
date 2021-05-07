package com.enableets.edu.sdk.ppr.ppr.core;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/17
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("files")
public class PaperFiles {

    @XStreamImplicit
    private List<FileItem> items;
}
