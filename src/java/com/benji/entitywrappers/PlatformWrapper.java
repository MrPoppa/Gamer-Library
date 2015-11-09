package com.benji.entitywrappers;

import com.benji.entities.Platform;
import com.benji.entities.Link;
import com.benji.entities.PlatformReceipt;
import java.util.ArrayList;
import java.util.List;

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
}
