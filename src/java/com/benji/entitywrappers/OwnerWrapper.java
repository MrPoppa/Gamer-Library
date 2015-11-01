package com.benji.entitywrappers;

import com.benji.entities.Owner;
import com.benji.utils.Link;

/**
 *
 * @author Benjamin
 */
public class OwnerWrapper {
    private Owner owner;
    private Link link;

    public OwnerWrapper() {
    }

    public OwnerWrapper(Owner owner, Link link) {
        this.owner = owner;
        this.link = link;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }
}
