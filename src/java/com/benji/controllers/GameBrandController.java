package com.benji.controllers;

import com.benji.ejb.GameBrandFacade;
import com.benji.entities.GameBrand;
import com.benji.entities.Link;
import com.benji.entitywrappers.GameBrandWrapper;
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
 * @author Benjamin Bengtsson
 */
@Path("gamebrand")
public class GameBrandController {

    private static final String JSON = MediaType.APPLICATION_JSON;

    @EJB
    GameBrandFacade gameBrandFacade;

    @GET
    @Produces(JSON)
    public Response getAllGameBrands(
            @Context UriInfo uriInfo
    ) {
        List<GameBrand> gameBrands = gameBrandFacade.findAll();
        List<GameBrandWrapper> wrappedGameBrands = new ArrayList<>();
        for (GameBrand gameBrand : gameBrands) {
            GameBrandWrapper wrappedGameBrand = new GameBrandWrapper();
            String selfUri = uriInfo.getAbsolutePathBuilder()
                    .path(GameBrandController.class)
                    .path(Integer.toString(gameBrand.getId()))
                    .build()
                    .toString();
            Link selfLink = new Link(selfUri, "self");
            wrappedGameBrand.setGameBrand(gameBrand);
            wrappedGameBrand.getLinks().add(selfLink);
            wrappedGameBrands.add(wrappedGameBrand);
        }
        GenericEntity<List<GameBrandWrapper>> gameBrandList
                = new GenericEntity<List<GameBrandWrapper>>(wrappedGameBrands) {
                };
        return Response.status(Status.OK).entity(gameBrandList).build();
    }

    @GET
    @Path("{gameBrandId}")
    @Produces(JSON)
    public Response getGameBrandById(
            @Context UriInfo uriInfo,
            @PathParam("gameBrandId") Integer gameBrandId
    ) {
        GameBrand gameBrand = gameBrandFacade.find(gameBrandId);
        if(gameBrand == null) {
            throw new DataNotFoundException("Game brand with id " 
                    + gameBrandId + " does not exist.");
        } else {
            GameBrandWrapper wrappedGameBrand =  new GameBrandWrapper();
            String selfUri = uriInfo.getAbsolutePathBuilder()
                    .path(GameBrandController.class)
                    .path(Integer.toString(gameBrand.getId()))
                    .build()
                    .toString();
            Link selfLink = new Link(selfUri, "self");
            wrappedGameBrand.setGameBrand(gameBrand);
            wrappedGameBrand.getLinks().add(selfLink);
            return Response.status(Status.OK).entity(wrappedGameBrand).build();
        }
    }
}
