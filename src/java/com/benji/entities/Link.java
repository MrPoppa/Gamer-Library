package com.benji.entities;

import java.util.Objects;

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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this.href);
        hash = 29 * hash + Objects.hashCode(this.ref);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Link other = (Link) obj;
        if (!Objects.equals(this.href, other.href)) {
            return false;
        }
        if (!Objects.equals(this.ref, other.ref)) {
            return false;
        }
        return true;
    }
}
