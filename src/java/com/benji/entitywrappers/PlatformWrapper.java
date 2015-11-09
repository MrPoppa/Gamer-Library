package com.benji.entitywrappers;

import com.benji.entities.Platform;
import com.benji.entities.Link;
import com.benji.entities.PlatformReceipt;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *
 * @author Benjamin Bengtsson
 */
public class PlatformWrapper {
    private Platform platform;
    private List<PlatformReceipt> platformReceipt =  new ArrayList<>();
    private List<Link> links = new ArrayList<>();

    public PlatformWrapper() {
    }

    public PlatformWrapper(Platform platform) {
        this.platform = platform;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public List<PlatformReceipt> getPlatformReceipt() {
        return platformReceipt;
    }

    public void setPlatformReceipt(List<PlatformReceipt> platformReceipt) {
        this.platformReceipt = platformReceipt;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.platform);
        hash = 59 * hash + Objects.hashCode(this.platformReceipt);
        hash = 59 * hash + Objects.hashCode(this.links);
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
        final PlatformWrapper other = (PlatformWrapper) obj;
        if (!Objects.equals(this.platform, other.platform)) {
            return false;
        }
        if (!Objects.equals(this.platformReceipt, other.platformReceipt)) {
            return false;
        }
        if (!Objects.equals(this.links, other.links)) {
            return false;
        }
        return true;
    }
}
