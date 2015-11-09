package com.benji.controllers;

import com.benji.ejb.GameBrandFacade;
import com.benji.ejb.GameFacade;
import com.benji.ejb.GameRatingFacade;
import com.benji.ejb.GenreFacade;
import com.benji.entities.Game;
import com.benji.entities.GameBrand;
import com.benji.entities.GameRating;
import com.benji.entities.Genre;
import com.benji.entitywrappers.GameWrapper;
import com.benji.entities.Link;
import com.benji.exceptions.DataNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
 * @author Benjamin Bengtsson
 */
@Path("game")
public class GameController {

    private static final String JSON = MediaType.APPLICATION_JSON;

    @EJB
    GameFacade gameFacade;
    @EJB
    GameRatingFacade gameRatingFacade;
    @EJB
    GenreFacade genreFacade;
    @EJB
    GameBrandFacade gameBrandFacade;

    @GET
    @Produces(JSON)
    @Path("{gameId}")
    public Response getGameById(
            @Context UriInfo uriInfo,
            @PathParam("gameId") Integer gameId
    ) {
        Game game = gameFacade.find(gameId);
        if (game != null) {
            String selfUri = uriInfo.getBaseUriBuilder()
                    .path(GameController.class)
                    .path(Integer.toString(gameId))
                    .build()
                    .toString();
            Link selfLink = new Link(selfUri, "self");
            GameWrapper wrappedGame = new GameWrapper();
            wrappedGame.setGame(game);
            wrappedGame.getLinks().add(selfLink);
            return Response.status(Status.OK).entity(wrappedGame).build();
        } else {
            throw new DataNotFoundException("Game with id " + gameId + " was not found.");
        }
    }

    @GET
    @Produces(JSON)
    public Response getAllGames(
            @Context UriInfo uriInfo
    ) {
        List<Game> games = gameFacade.findAll();
        List<GameWrapper> wrappedGames = new ArrayList<>();
        for (Game game : games) {
            GameWrapper wrappedGame = new GameWrapper();
            String selfUri = uriInfo.getBaseUriBuilder()
                    .path(GameController.class)
                    .build()
                    .toString();
            Link selfLink = new Link(selfUri, "self");
            wrappedGame.setGame(game);
            wrappedGame.getLinks().add(selfLink);
            wrappedGames.add(wrappedGame);
        }
        GenericEntity<List<GameWrapper>> gameList
                = new GenericEntity<List<GameWrapper>>(wrappedGames) {
                };
        return Response.status(Status.OK).entity(gameList).build();
    }

    @POST
    @Produces(JSON)
    public Response createGame(
            @Context UriInfo uriInfo,
            @FormParam("gameName") String gameName,
            @FormParam("ratingId") Integer ratingId,
            @FormParam("brandId") Integer brandId,
            @FormParam("genreIds") List<Integer> genreIds
    ) {
        GameBrand gameBrand = gameBrandFacade.find(brandId);
        GameRating gameRating = gameRatingFacade.find(ratingId);

        Game game = new Game();
        game.setGameName(gameName);
        game.setRating(gameRating);
        game.setBrand(gameBrand);
        gameFacade.create(game);
        for (Integer genreId : genreIds) {
            Genre genre = genreFacade.find(genreId);
            if (genre == null) {
                throw new DataNotFoundException("Genre with id " + genreId + " does not exist.");
            } else {
                game.getGenreList().add(genre);
                genre.getGameList().add(game);
                genreFacade.edit(genre);
            }
        }
        gameFacade.edit(game);
        GameWrapper wrappedGame = new GameWrapper();
        String selfUri = uriInfo.getBaseUriBuilder()
                .path(GameController.class)
                .build()
                .toString();
        Link selfLink = new Link(selfUri, "self");
        wrappedGame.setGame(game);
        wrappedGame.getLinks().add(selfLink);
        return Response.status(Status.OK).entity(wrappedGame).build();
    }

    @DELETE
    @Path("{gameId}/delete")
    @Produces(MediaType.TEXT_PLAIN)
    public String removeGame(
            @PathParam("gameId") Integer gameId
    ) {
        gameFacade.remove(gameFacade.find(gameId));
        return "removed";
    }
}
