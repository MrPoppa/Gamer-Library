package com.benji.entitywrappers;

import com.benji.entities.Link;
import com.benji.entities.PlatformBrand;
import java.util.List;

/**
 *
 * @author Benjamin Bengtsson
 */
public class PlatformBrandWrapper {

    private PlatformBrand platformBrand;
    private List<Link> links;

    public PlatformBrandWrapper() {
    }

    public PlatformBrandWrapper(PlatformBrand platformBrand, List<Link> links) {
        this.platformBrand = platformBrand;
        this.links = links;
    }

    public PlatformBrand getPlatformBrand() {
        return platformBrand;
    }

    public void setPlatformBrand(PlatformBrand platformBrand) {
        this.platformBrand = platformBrand;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
