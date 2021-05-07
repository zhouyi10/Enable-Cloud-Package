package com.enableets.edu.sdk.ppr.ppr.core.paper;


import com.enableets.edu.sdk.ppr.ppr.core.Header;
import com.enableets.edu.sdk.ppr.ppr.core.PaperFiles;
import com.enableets.edu.sdk.ppr.xml.xstream.EntityToXml;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author caleb_liu@enable-ets.com
 * @date 2020/06/11 15:53
 */

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("paper")
public class PaperXML extends EntityToXml {

    public PaperXML(Header header, Body body, PaperFiles files) {
        this.header = header;
        this.body = body;
        this.files = files;
    }

    private Header header;

    private Body body;

    private PaperFiles files;

    private Map<String, com.enableets.edu.sdk.ppr.ppr.core.question.Question> questionMap;
}
