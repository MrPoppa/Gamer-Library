package com.benji.controllers;

import com.benji.ejb.GameReceiptFacade;
import com.benji.ejb.OwnerFacade;
import com.benji.ejb.PlatformBrandFacade;
import com.benji.ejb.PlatformFacade;
import com.benji.ejb.PlatformReceiptFacade;
import com.benji.entities.Game;
import com.benji.entities.GameReceipt;
import com.benji.entities.Owner;
import com.benji.entities.Platform;
import com.benji.entitywrappers.PlatformWrapper;
import com.benji.entities.Link;
import com.benji.entities.PlatformBrand;
import com.benji.entities.PlatformReceipt;
import com.benji.exceptions.DataNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
 * @author Benjamin Bengtsson
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
    @EJB
    GameReceiptFacade gameRecieptFacade;
    @EJB
    PlatformReceiptFacade platformRecieptFacade;

    @GET
    @Path("{platformId}")
    @Produces(JSON)
    public Response getPlatformById(
            @Context UriInfo uriInfo,
            @Context Request request,
            @PathParam("platformId") Integer platformId
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

            int hashValue = wrappedPlatform.getPlatform().hashCode();
            System.out.println(hashValue);
            for (Link link : wrappedPlatform.getLinks()) {
                hashValue += link.hashCode();
            }
            CacheControl cc = new CacheControl();
            cc.setMaxAge(86400);
            cc.setPrivate(true);
            EntityTag etag = new EntityTag(Integer.toString(hashValue));
            Response.ResponseBuilder rbuilder = request.evaluatePreconditions(etag);
            if (rbuilder == null) {
                rbuilder = Response.ok(wrappedPlatform);
                rbuilder.tag(etag);
            }
            rbuilder.cacheControl(cc);

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

    @POST
    @Produces(JSON)
    public Response createPlatform(
            @Context UriInfo uriInfo,
            @FormParam("platformName") String platformName,
            @FormParam("brandId") Integer brandId
    ) {
        PlatformBrand platformBrand = platformBrandFacade.find(brandId);
        if (platformBrand == null) {
            throw new DataNotFoundException("Platform brand with id "
                    + brandId + " does not exist.");
        } else {
            Platform platform = new Platform();
            platform.setPlatformName(platformName);
            platform.setBrand(platformBrand);
            platformFacade.create(platform);
            String selfUri = uriInfo.getBaseUriBuilder()
                    .path(PlatformController.class)
                    .path(Integer.toString(platform.getId()))
                    .build()
                    .toString();
            Link selfLink = new Link(selfUri, "self");
            PlatformWrapper wrappedPlatform = new PlatformWrapper();
            wrappedPlatform.setPlatform(platform);
            wrappedPlatform.getLinks().add(selfLink);
            return Response.status(Status.OK).entity(wrappedPlatform).build();
        }
    }

    /**
     * Lets owners trade platforms.
     *
     * @param uriInfo Context used to build URI leading to specific requests.
     * @param ownerId Integer representing the old owners id.
     * @param platformId Integer representing the id of the platform about to be
     * traded
     * @param newOwnerId Integer representing the new owners id.
     * @param recieptId
     * @param tradeGames boolean representing the choice to trade the games as
     * well.
     * @return the Platform that has changed owner and the new owners receipts.
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
            @FormParam("recieptId") Integer recieptId,
            @NotNull
            @FormParam("tradeGames") boolean tradeGames
    ) {
        Platform platform = platformFacade.find(platformId);
        Owner oldOwner = ownerFacade.find(ownerId);
        Owner newOwner = ownerFacade.find(newOwnerId);
        List<Owner> platformOwners = platform.getOwnerList();
        PlatformReceipt platformReciept = platformRecieptFacade.find(recieptId);
        if (platformOwners.contains(oldOwner)) {
            platformReciept.setOwner(newOwner);
            platform.getOwnerList().remove(oldOwner);
            platform.getOwnerList().add(newOwner);
            platformRecieptFacade.edit(platformReciept);
            if (tradeGames) {
                List<Game> games = ownerFacade.getOwnedPlatformGamesByPlatformId(platformId);
                for (Game game : games) {
                    GameReceipt gameReciept = ownerFacade.getGameRecieiptByGameAndOwnerId(game.getId(), ownerId);
                    gameReciept.setOwner(newOwner);
                    gameRecieptFacade.edit(gameReciept);
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
            wrappedPlatform.setPlatformReceipt(ownerFacade.getPlatformRecieiptByPlatformAndOwnerId(platformId, newOwnerId));
            wrappedPlatform.getLinks().add(selfLink);
            wrappedPlatform.getLinks().add(tradeLink);
            return Response.status(Status.OK).entity(wrappedPlatform).build();
        } else {
            throw new DataNotFoundException("Owner with id " + ownerId
                    + " does not own the platform with id " + platformId);
        }
    }

    @DELETE
    @Path("{platformId}/delete")
    @Produces(JSON)
    public Response removePlatform(
            @Context UriInfo uriInfo,
            @PathParam("platformId") Integer platformId
    ) {
        Platform platform = platformFacade.find(platformId);
        if (platform == null) {
            throw new DataNotFoundException("Platform with id "
                    + platformId + " does not exist.");
        } else {
            platformFacade.remove(platform);
            String selfUri = uriInfo.getBaseUriBuilder()
                    .path(PlatformController.class)
                    .path(Integer.toString(platformId))
                    .build()
                    .toString();
            Link selfLink = new Link(selfUri, "self");
            PlatformWrapper wrappedPlatform = new PlatformWrapper();
            wrappedPlatform.setPlatform(platform);
            wrappedPlatform.getLinks().add(selfLink);
            return Response.status(Status.OK).entity(wrappedPlatform).build();
        }
    }
}
