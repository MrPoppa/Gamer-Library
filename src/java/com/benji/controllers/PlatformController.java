package com.benji.controllers;

import com.benji.ejb.OwnerFacade;
import com.benji.ejb.PlatformBrandFacade;
import com.benji.ejb.PlatformFacade;
import com.benji.entities.Game;
import com.benji.entities.Owner;
import com.benji.entities.Platform;
import com.benji.entitywrappers.PlatformWrapper;
import com.benji.entities.Link;
import com.benji.exceptions.DataNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.validation.constraints.NotNull;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author Benjamin
 */
@Path("platform")
public class PlatformController {

    private static final String JSON = MediaType.APPLICATION_JSON;

    @EJB
    PlatformFacade platformFacade;
    @EJB
    PlatformBrandFacade platformBrandFacade;
    @EJB
    OwnerFacade ownerFacade;

    @GET
    @Path("{platformId}")
    @Produces(JSON)
    public Response getPlatformById(
            @Context UriInfo uriInfo,
            @Context Request request,
            @PathParam("platformId") int platformId
    ) {
        Platform platform = platformFacade.find(platformId);
        if (platform != null) {
            String selfUri = uriInfo.getBaseUriBuilder()
                    .path(PlatformController.class)
                    .path(Integer.toString(platformId))
                    .build()
                    .toString();
            Link selfLink = new Link(selfUri, "self");
            PlatformWrapper wrappedPlatform = new PlatformWrapper();
            wrappedPlatform.setPlatform(platform);
            wrappedPlatform.getLinks().add(selfLink);

            int hashValue = wrappedPlatform.hashCode();
            CacheControl cc = new CacheControl();
            cc.setMaxAge(86400);
            cc.setPrivate(true);
            EntityTag etag = new EntityTag(Integer.toString(hashValue));
            Response.ResponseBuilder rbuilder = request.evaluatePreconditions(etag);
            if (rbuilder == null) {
                rbuilder = Response.ok(wrappedPlatform);
                rbuilder.tag(etag);
            }
            rbuilder.cacheControl(cc).build();

            return rbuilder.build();
//                    Response.status(Status.OK).entity(wrappedPlatform).build();
        } else {
            throw new DataNotFoundException("Platform with id " + platformId
                    + " does not exist.");
        }
    }

    @GET
    @Produces(JSON)
    public Response getAllPlatforms(
            @Context UriInfo uriInfo
    ) {
        List<Platform> platforms = platformFacade.findAll();
        List<PlatformWrapper> wrappedPlatforms = new ArrayList<>();
        for (Platform platform : platforms) {
            String selfUri = uriInfo.getBaseUriBuilder()
                    .path(PlatformController.class)
                    .path(Integer.toString(platform.getId()))
                    .build()
                    .toString();
            Link selfLink = new Link(selfUri, "self");
            PlatformWrapper wrappedPlatform = new PlatformWrapper();
            wrappedPlatform.setPlatform(platform);
            wrappedPlatform.getLinks().add(selfLink);
            wrappedPlatforms.add(wrappedPlatform);
        }
        GenericEntity<List<PlatformWrapper>> platformList
                = new GenericEntity<List<PlatformWrapper>>(wrappedPlatforms) {
                };
        return Response.status(Status.OK).entity(platformList).build();
    }

    /**
     * Lets owners trade platforms.
     *
     * @param uriInfo Context used to build URI leading to specific requests.
     * @param ownerId Integer representing the old owners id.
     * @param platformId Integer representing the id of the platform about to be
     * traded
     * @param newOwnerId Integer representing the new owners id.
     * @param tradeGames boolean representing the choice to trade the games as
     * well.
     * @return the Platform that has changed owner.
     */
    @PUT
    @Produces(JSON)
    @Path("trade")
    public Response tradePlatform(
            @Context UriInfo uriInfo,
            @NotNull
            @FormParam("ownerId") Integer ownerId,
            @NotNull
            @FormParam("platformId") Integer platformId,
            @NotNull
            @FormParam("newOwnerId") Integer newOwnerId,
            @NotNull
            @FormParam("tradeGames") boolean tradeGames
    ) {
        Platform platform = platformFacade.find(platformId);
        Owner oldOwner = ownerFacade.find(ownerId);
        Owner newOwner = ownerFacade.find(newOwnerId);
        List<Owner> platformOwners = platform.getOwnerList();
        if (platformOwners.contains(oldOwner)) {

            platform.getOwnerList().clear();
            platform.getOwnerList().add(newOwner);
            if (!tradeGames) {
                platform.getGameList().clear();
                platformFacade.edit(platform);
            } else {
                List<Game> games = platform.getGameList();
                for (Game game : games) {
                    newOwner.getGameList().add(game);
                    oldOwner.getGameList().remove(game);
                }
                ownerFacade.edit(newOwner);
                ownerFacade.edit(oldOwner);
                platformFacade.edit(platform);
            }
            String tradeUri = uriInfo.getBaseUriBuilder()
                    .path(PlatformController.class)
                    .path("/trade")
                    .build()
                    .toString();
            Link tradeLink = new Link(tradeUri, "trade");
            String selfUri = uriInfo.getBaseUriBuilder()
                    .path(PlatformController.class)
                    .path(Integer.toString(platformId))
                    .build()
                    .toString();
            Link selfLink = new Link(selfUri, "self");
            PlatformWrapper wrappedPlatform = new PlatformWrapper();
            wrappedPlatform.setPlatform(platform);
            wrappedPlatform.getLinks().add(selfLink);
            wrappedPlatform.getLinks().add(tradeLink);
            return Response.status(Status.OK).entity(wrappedPlatform).build();
        } else {
            throw new DataNotFoundException("Owner with id " + ownerId
                    + " does not own the platform with id " + platformId);
        }
    }

//    @GET
//    @Produces(JSON)
//    public Response getAllOwnedPlatforms(
//            @Context UriInfo uriInfo,
//            @PathParam("ownerId") int ownerId
//    ) {
//        List<Platform> platforms = platformFacade.getAllPlatformsByOwnerId(ownerId);
//        List<PlatformWrapper> wrappedPlatforms = new ArrayList<>();
//        for (Platform p : platforms) {
//            String selfUri = uriInfo.getBaseUriBuilder()
//                    .path(PlatformController.class)
//                    .path(Integer.toString(p.getId()))
//                    .build()
//                    .toString();
//            Link link = new Link(selfUri, "self");
//            PlatformWrapper wrappedPlatform = new PlatformWrapper();
//            wrappedPlatform.setPlatform(p);
//            wrappedPlatform.getLinks().add(link);
//            wrappedPlatforms.add(wrappedPlatform);
//        }
//        GenericEntity<List<PlatformWrapper>> platformList
//                = new GenericEntity<List<PlatformWrapper>>(wrappedPlatforms) {
//                };
//        return Response.status(Status.OK).entity(platformList).build();
//    }
//
//    @POST
//    @Path("{ownerId}")
//    @Produces(JSON)
//    public Response addPlatform(
//            @Context UriInfo uriInfo,
//            @PathParam("ownerId") int ownerId,
//            @FormParam("platformName") String platformName,
//            @FormParam("price") int price,
//            @FormParam("brandId") int brandId
//    ) {
//        Owner owner = ownerFacade.find(ownerId);
//        PlatformBrand brand = platformBrandFacade.find(brandId);
//        if (owner == null) {
//            return Response.status(Status.NOT_FOUND).build();
//        } else if (brand == null || platformName == null) {
//            return Response.status(Status.BAD_REQUEST).build();
//        } else {
//            Platform platform = new Platform();
//            platform.setPlatformName(platformName);
//            platform.setPrice(price);
//            platform.setBuyDate(new Date(System.currentTimeMillis()));
//            platform.setBrand(brand);
//            platformFacade.create(platform);
//            platform.getOwnerList().add(owner);
//            owner.getPlatformList().add(platform);
//            platformFacade.edit(platform);
//            ownerFacade.edit(owner);
//            String selfUri = uriInfo.getBaseUriBuilder()
//                    .path(PlatformController.class)
//                    .path(Integer.toString(platform.getId()))
//                    .build()
//                    .toString();
//            Link selfLink = new Link(selfUri, "self");
//            String createUri = uriInfo.getBaseUriBuilder()
//                    .path(PlatformController.class)
//                    .path(Integer.toString(ownerId))
//                    .build()
//                    .toString();
//            Link createLink = new Link(createUri, "create");
//            PlatformWrapper wrappedPlatform = new PlatformWrapper();
//            wrappedPlatform.setPlatform(platform);
//            wrappedPlatform.getLinks().add(selfLink);
//            wrappedPlatform.getLinks().add(createLink);
//            return Response.status(Status.OK).entity(wrappedPlatform).build();
//        }
//    }
}
