package com.benji.controllers;

import com.benji.ejb.OwnerFacade;
import com.benji.ejb.PlatformFacade;
import com.benji.entities.Game;
import com.benji.entities.Owner;
import com.benji.entities.Platform;
import com.benji.entitywrappers.PlatformWrapper;
import com.benji.utils.Link;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
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
@Path("platform")
public class PlatformController {

    private final String JSON = MediaType.APPLICATION_JSON;

    @EJB
    PlatformFacade platformFacade;
    @EJB
    OwnerFacade ownerFacade;

    @GET
    @Path("{platformId}")
    @Produces(JSON)
    public Response getPlatformById(
            @Context UriInfo uriInfo,
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
            return Response.status(Status.OK).entity(wrappedPlatform).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
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
     * @param uriInfo Context used to build uri leading to specific requests.
     * @param ownerId int representing the old owners id.
     * @param platformId int representing the id of the platform about to be
     * traded
     * @param newOwnerId int representing the new owners id.
     * @param tradeGames boolean representing the choice to trade the games as
     * well.
     * @return the Platform that has changed owner.
     */
    @PUT
    @Produces(JSON)
    @Path("trade")
    public Response tradePlatform(
            @Context UriInfo uriInfo,
            @FormParam("ownerId") int ownerId,
            @FormParam("platformId") int platformId,
            @FormParam("newOwnerId") int newOwnerId,
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
                    .path(OwnerController.class)
                    .path(Integer.toString(ownerId))
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
            return Response.status(Status.NOT_FOUND).build();
        }
    }

}