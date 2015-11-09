package com.benji.controllers;

import com.benji.ejb.GenreFacade;
import com.benji.entities.Genre;
import com.benji.entities.Link;
import com.benji.entitywrappers.GenreWrapper;
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
@Path("genre")
public class GameGenreController {

    private static final String JSON = MediaType.APPLICATION_JSON;

    @EJB
    GenreFacade genreFacade;

    @GET
    @Produces(JSON)
    public Response getAllGameGenres(
            @Context UriInfo uriInfo
    ) {
        List<Genre> genres = genreFacade.findAll();
        List<GenreWrapper> wrappedGenres = new ArrayList<>();
        for (Genre genre : genres) {
            GenreWrapper wrappedGenre = new GenreWrapper();
            String selfUri = uriInfo.getBaseUriBuilder()
                    .path(GameGenreController.class)
                    .path(Integer.toString(genre.getId()))
                    .build()
                    .toString();
            Link selfLink = new Link(selfUri, "self");
            wrappedGenre.setGenre(genre);
            wrappedGenre.getLinks().add(selfLink);
        }
        GenericEntity<List<GenreWrapper>> genreList
                = new GenericEntity<List<GenreWrapper>>(wrappedGenres) {
                };
        
        return Response.status(Status.OK).entity(genreList).build();
    }
    
    @GET
    @Path("{genreId}")
    @Produces(JSON)
    public Response getGameGenreById(
            @Context UriInfo uriInfo,
            @PathParam("genreId") Integer genreId
    ) {
        Genre genre = genreFacade.find(genreId);
        if(genre == null) {
            throw new DataNotFoundException("Genre with id " 
                    + genreId + " does not exist.");
        } else {
            GenreWrapper wrappedGenre = new GenreWrapper();
            String selfUri = uriInfo.getBaseUriBuilder()
                    .path(GameGenreController.class)
                    .path(Integer.toString(genre.getId()))
                    .build()
                    .toString();
            Link selfLink = new Link(selfUri, "self");
            wrappedGenre.setGenre(genre);
            wrappedGenre.getLinks().add(selfLink);
            return Response.status(Status.OK).entity(wrappedGenre).build();
        }
        
    }

}
