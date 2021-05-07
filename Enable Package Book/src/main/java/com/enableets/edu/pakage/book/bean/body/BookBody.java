package com.enableets.edu.pakage.book.bean.body;



import com.enableets.edu.pakage.core.bean.Body;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;


@XStreamAlias("body")
public class BookBody extends Body {

    @XStreamImplicit
    private List<Page> pageList;

    public BookBody() {
    }

    public BookBody(List<Page> pageList) {
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
        return "BookBody{" +
                "pageList=" + pageList +
                '}';
    }
}
