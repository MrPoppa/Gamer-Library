package com.benji.models;

/**
 *
 * @author Benjamin
 */
public class Link {
    private String href;
    private String ref;

    public Link() {
    }

    public Link(String ref, String href) {
        this.href = ref;
        this.ref = href;
    }
    
    public String getRef() {
        return href;
    }

    public void setRef(String ref) {
        this.href = ref;
    }

    public String getHref() {
        return ref;
    }

    public void setHref(String href) {
        this.ref = href;
    }
}
