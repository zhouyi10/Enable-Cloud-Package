package com.enableets.edu.pakage.etm.bean.body;

import com.enableets.edu.pakage.core.bean.Body;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;



import java.util.List;


@XStreamAlias("body")
public class ETMBody extends Body {

    @XStreamImplicit
    private List<Page> pageList;

    public ETMBody() {
    }

    public ETMBody(List<Page> pageList) {
        this.pageList = pageList;
    }

    public List<Page> getPageList() {
        return pageList;
    }

    public void setPageList(List<Page> pageList) {
        this.pageList = pageList;
    }

    @Override
    public String toString() {
        return "ETMBody{" +
                "pageList=" + pageList +
                '}';
    }
}
