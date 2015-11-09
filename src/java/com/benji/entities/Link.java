package com.benji.entities;

/**
 *
 * @author Benjamin Bengtsson
 */
public class Link {
    private String href;
    private String ref;

    public Link() {
    }

    public Link(String href, String ref) {
        this.ref = href;
        this.href = ref;
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
