package com.benji.entitywrappers;

import com.benji.entities.Owner;
import com.benji.utils.Link;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Benjamin
 */
public class OwnerWrapper {
    private Owner owner;
    private List<Link> links =  new ArrayList<>();

    public OwnerWrapper() {
    }

    public OwnerWrapper(Owner owner, List<Link> links) {
        this.owner = owner;
        this.links = links;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

}
