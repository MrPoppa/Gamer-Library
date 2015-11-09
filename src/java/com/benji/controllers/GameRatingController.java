package com.benji.controllers;

import com.benji.ejb.GameRatingFacade;
import com.benji.entities.GameRating;
import com.benji.entities.Link;
import com.benji.entitywrappers.GameRatingWrapper;
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
@Path("rating")
public class GameRatingController {

    private static final String JSON = MediaType.APPLICATION_JSON;

    @EJB
    GameRatingFacade gameRatingFacade;

    @GET
    @Produces(JSON)
    public Response getAllGameRatings(
            @Context UriInfo uriInfo
    ) {
        List<GameRating> gameRatings = gameRatingFacade.findAll();
        List<GameRatingWrapper> wrappedGameRatings = new ArrayList<>();
        for (GameRating gameRating : gameRatings) {
            GameRatingWrapper wrappedGameRating = new GameRatingWrapper();
            String selfUri = uriInfo.getBaseUriBuilder()
                    .path(GameRatingController.class)
                    .path(Integer.toString(gameRating.getId()))
                    .build()
                    .toString();
            Link selfLink = new Link(selfUri, "self");
            wrappedGameRating.setGameRating(gameRating);
            wrappedGameRating.getLinks().add(selfLink);
            wrappedGameRatings.add(wrappedGameRating);
        }

        GenericEntity<List<GameRatingWrapper>> gameRatingList
                = new GenericEntity<List<GameRatingWrapper>>(wrappedGameRatings) {
                };
        return Response.status(Status.OK).entity(gameRatingList).build();
    }

    @GET
    @Path("{gameRatingId}")
    @Produces(JSON)
    public Response getGameRatingById(
            @Context UriInfo uriInfo,
            @PathParam("gameRatingId") Integer gameRatingId
    ) {
        GameRating gameRating = gameRatingFacade.find(gameRatingId);
        if (gameRating == null) {
            throw new DataNotFoundException("Game rating with id "
                    + gameRatingId + " does not exist.");
        } else {
            GameRatingWrapper wrappedGameRating = new GameRatingWrapper();
            String selfUri = uriInfo.getBaseUriBuilder()
                    .path(GameRatingController.class)
                    .path(Integer.toString(gameRating.getId()))
                    .build()
                    .toString();
            Link selfLink = new Link(selfUri, "self");
            wrappedGameRating.setGameRating(gameRating);
            wrappedGameRating.getLinks().add(selfLink);
            return Response.status(Status.OK).entity(wrappedGameRating).build();
        }
    }
}
