package com.benji.controllers;

import com.benji.ejb.PlatformBrandFacade;
import com.benji.entities.Link;
import com.benji.entities.PlatformBrand;
import com.benji.entitywrappers.PlatformBrandWrapper;
import com.benji.exceptions.DataNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Benjamin
 */
@Path("platformbrand")
public class PlatformBrandController {
    private static final String JSON = MediaType.APPLICATION_JSON;
    
    @EJB
    PlatformBrandFacade platformBrandFacade;
    
    @GET
    @Produces(JSON)
    public Response getAllPlatformBrands(
            @Context UriInfo uriInfo
    ) {
        List<PlatformBrand> platformBrands = platformBrandFacade.findAll();
        List<PlatformBrandWrapper> wrappedPlatformBrands =  new ArrayList<>();
        for(PlatformBrand platformBrand : platformBrands) {
            PlatformBrandWrapper wrappedPlatformBrand =  new PlatformBrandWrapper();
            String selfUri = uriInfo.getAbsolutePathBuilder()
                    .path(PlatformBrandController.class)
                    .path(Integer.toString(platformBrand.getId()))
                    .build()
                    .toString();
            Link selfLink = new Link(selfUri, "self");
            wrappedPlatformBrand.setPlatformBrand(platformBrand);
            wrappedPlatformBrand.getLinks().add(selfLink);
            wrappedPlatformBrands.add(wrappedPlatformBrand);
        }
        GenericEntity<List<PlatformBrandWrapper>> platformBrandList
                = new GenericEntity<List<PlatformBrandWrapper>>(wrappedPlatformBrands) {
                };
        return Response.status(Status.OK).entity(platformBrandList).build();
    }
    
    @GET
    @Path("{platformBrandId}")
    @Produces(JSON)
    public Response getPlatformBrandById(
            @Context UriInfo uriInfo,
            @PathParam("platformBrandId") Integer platformBrandId
    ) {
       PlatformBrand platformBrand = platformBrandFacade.find(platformBrandId);
       if(platformBrand == null) {
           throw new DataNotFoundException("Platform brand with id " 
                   + platformBrandId + " does not exist.");
       } else {
           PlatformBrandWrapper wrappedPlatformBrand =  new PlatformBrandWrapper();
            String selfUri = uriInfo.getAbsolutePathBuilder()
                    .path(PlatformBrandController.class)
                    .path(Integer.toString(platformBrand.getId()))
                    .build()
                    .toString();
            Link selfLink = new Link(selfUri, "self");
            wrappedPlatformBrand.setPlatformBrand(platformBrand);
            wrappedPlatformBrand.getLinks().add(selfLink);
            return Response.status(Status.OK).entity(wrappedPlatformBrand).build();
       }
    }
    
}
