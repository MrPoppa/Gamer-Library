package com.benji.controllers;

import com.benji.ejb.GameBrandFacade;
import com.benji.ejb.GameFacade;
import com.benji.ejb.GameRatingFacade;
import com.benji.ejb.GenreFacade;
import com.benji.ejb.OwnerFacade;
import com.benji.ejb.PlatformBrandFacade;
import com.benji.ejb.PlatformFacade;
import com.benji.entities.Game;
import com.benji.entities.GameBrand;
import com.benji.entities.GameRating;
import com.benji.entities.Genre;
import com.benji.entities.Owner;
import com.benji.entities.Platform;
import com.benji.entities.PlatformBrand;
import com.benji.entitywrappers.OwnerWrapper;
import com.benji.entitywrappers.PlatformWrapper;
import com.benji.entities.Link;
import com.benji.exceptions.DataNotFoundException;
import com.benji.exceptions.IllegalPropertyException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
@Path("/owner")
public class OwnerController {

    private final String JSON = MediaType.APPLICATION_JSON;

    @EJB
    OwnerFacade ownerFacade;
    @EJB
    PlatformBrandFacade platformBrandFacade;
    @EJB
    GameBrandFacade gameBrandFacade;
    @EJB
    GameRatingFacade gameRatingFacade;
    @EJB
    GenreFacade genreFacade;
    @EJB
    PlatformFacade platformFacade;
    @EJB
    GameFacade gameFacade;

    /**
     * Method to fetch all Owner objects saved in the database.
     *
     * @param uriInfo Context used to build uri leading to specific requests.
     * @return A list containing all Owner objects including a response code.
     */
    @GET
    @Produces(JSON)
    public Response getAllOwners(@Context UriInfo uriInfo) {
        List<Owner> owners = ownerFacade.findAll();
        List<OwnerWrapper> wrappedOwners = new ArrayList<>();
        for (Owner owner : owners) {
            String uri = uriInfo.getBaseUriBuilder()
                    .path(OwnerController.class)
                    .path(Integer.toString(owner.getId()))
                    .build()
                    .toString();
            Link link = new Link(uri, "self");
            OwnerWrapper wrappedOwner = new OwnerWrapper();
            wrappedOwner.setOwner(owner);
            wrappedOwner.getLinks().add(link);
            wrappedOwners.add(wrappedOwner);
        }
        GenericEntity<List<OwnerWrapper>> ownerList
                = new GenericEntity<List<OwnerWrapper>>(wrappedOwners) {
                };
        return Response.status(Status.OK).entity(ownerList).build();
    }

    /**
     * Method to fetch a specific Owner object saved in the database.
     *
     * @param uriInfo Context used to build uri leading to specific requests.
     * @param ownerId Specifies the id of the wanted Owner object.
     * @return The Owner object with the specific ownerId including a response
     * code
     */
    @GET
    @Produces(JSON)
    @Path("{ownerId}")
    public Response getOwnerById(
            @Context UriInfo uriInfo,
            @PathParam("ownerId") int ownerId
    ) {
        Owner owner = ownerFacade.find(ownerId);
        if (owner == null) {
            throw new DataNotFoundException("Owner with " + ownerId + " does not exist");
        } else {
            String uri = uriInfo.getBaseUriBuilder()
                    .path(OwnerController.class)
                    .path(Integer.toString(ownerId))
                    .build()
                    .toString();
            Link link = new Link(uri, "self");
            OwnerWrapper wrappedOwner = new OwnerWrapper();
            wrappedOwner.setOwner(owner);
            wrappedOwner.getLinks().add(link);
            return Response.status(Status.OK).entity(wrappedOwner).build();
        }
    }

    @POST
    @Produces(JSON)
    public Response createOwner(
            @Context UriInfo uriInfo,
            @NotNull
            @FormParam("firstName") String firstName,
            @NotNull
            @FormParam("lastName") String lastName,
            @NotNull
            @Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message = "Invalid email")
            @FormParam("email") String email,
            @NotNull
            @FormParam("ssn") String ssn,
            @NotNull
            @FormParam("userName") String userName,
            @NotNull
            @FormParam("password") String password
    ) {
        Owner owner = new Owner();
        owner.setFirstName(firstName);
        owner.setLastName(lastName);
        owner.setEmail(email);
        owner.setSsn(ssn);
        owner.setUserName(userName);
        owner.setPassword(password);
        ownerFacade.create(owner);
        String selfUri = uriInfo.getBaseUriBuilder()
                .path(OwnerController.class)
                .path(Integer.toString(owner.getId()))
                .build()
                .toString();
        Link selfLink = new Link(selfUri, "self");
        String createUri = uriInfo.getBaseUriBuilder()
                .path(OwnerController.class)
                .build()
                .toString();
        Link createLink = new Link(createUri, "create");
        OwnerWrapper wrappedOwner = new OwnerWrapper();
        wrappedOwner.setOwner(owner);
        wrappedOwner.getLinks().add(selfLink);
        wrappedOwner.getLinks().add(createLink);
        return Response.status(Response.Status.OK).entity(wrappedOwner).build();
    }

    @PUT
    @Path("{ownerId}")
    @Produces(JSON)
    public Response updateOwner(
            @Context UriInfo uriInfo,
            @PathParam("ownerId") int ownerId,
            @NotNull
            @FormParam("firstName") String firstName,
            @NotNull
            @FormParam("lastName") String lastName,
            @NotNull
            @Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message = "Invalid email")
            @FormParam("email") String email,
            @NotNull
            @FormParam("ssn") String ssn,
            @NotNull
            @FormParam("userName") String userName,
            @NotNull
            @FormParam("password") String password
    ) {
        Owner owner = ownerFacade.find(ownerId);
        if (owner == null) {
            throw new DataNotFoundException("Owner with " + ownerId + " does not exist");
        } else {
            owner.setFirstName(firstName);
            owner.setLastName(lastName);
            owner.setEmail(email);
            owner.setSsn(ssn);
            owner.setUserName(userName);
            owner.setPassword(password);
            String selfUri = uriInfo.getBaseUriBuilder()
                    .path(OwnerController.class)
                    .path(Integer.toString(owner.getId()))
                    .build()
                    .toString();
            Link selfLink = new Link(selfUri, "self");
            String updateUri = uriInfo.getBaseUriBuilder()
                    .path(OwnerController.class)
                    .path(Integer.toString(ownerId))
                    .build()
                    .toString();
            Link updateLink = new Link(updateUri, "update");
            OwnerWrapper wrappedOwner = new OwnerWrapper();
            wrappedOwner.setOwner(owner);
            wrappedOwner.getLinks().add(updateLink);
            wrappedOwner.getLinks().add(selfLink);
            ownerFacade.edit(owner);
            return Response.status(Status.OK).entity(wrappedOwner).build();
        }
    }

    @DELETE
    @Path("{ownerId}/delete")
    @Produces(JSON)
    public Response deleteOwner(
            @Context UriInfo uriInfo,
            @PathParam("ownerId") int ownerId
    ) {
        Owner owner = ownerFacade.find(ownerId);
        System.out.println(ownerId);
        if (owner != null) {
            String selfUri = uriInfo.getBaseUriBuilder()
                    .path(OwnerController.class)
                    .path(Integer.toString(owner.getId()))
                    .build()
                    .toString();
            Link selfLink = new Link(selfUri, "self");
            String deleteUri = uriInfo.getBaseUriBuilder()
                    .path(OwnerController.class)
                    .path(Integer.toString(ownerId))
                    .path("/delete")
                    .build()
                    .toString();
            Link deleteLink = new Link(deleteUri, "delete");
            OwnerWrapper wrappedOwner = new OwnerWrapper();
            wrappedOwner.setOwner(owner);
            wrappedOwner.getLinks().add(selfLink);
            wrappedOwner.getLinks().add(deleteLink);
            ownerFacade.remove(owner);
            return Response.status(Status.OK).entity(wrappedOwner).build();
        } else {
            throw new DataNotFoundException("Owner with " + ownerId + " does not exist");
        }
    }

    @GET
    @Path("{ownerId}/platform")
    @Produces(JSON)
    public Response getAllOwnedPlatforms(
            @Context UriInfo uriInfo,
            @PathParam("ownerId") int ownerId
    ) {
        List<Platform> platforms = platformFacade.getAllPlatformsByOwnerId(ownerId);
        List<PlatformWrapper> wrappedPlatforms = new ArrayList<>();
        for (Platform p : platforms) {
            String selfUri = uriInfo.getBaseUriBuilder()
                    .path(PlatformController.class)
                    .path(Integer.toString(p.getId()))
                    .build()
                    .toString();
            Link link = new Link(selfUri, "self");
            PlatformWrapper wrappedPlatform = new PlatformWrapper();
            wrappedPlatform.setPlatform(p);
            wrappedPlatform.getLinks().add(link);
            wrappedPlatforms.add(wrappedPlatform);
        }
        GenericEntity<List<PlatformWrapper>> platformList
                = new GenericEntity<List<PlatformWrapper>>(wrappedPlatforms) {
                };
        return Response.status(Status.OK).entity(platformList).build();
    }

    @PUT
    @Path("{ownerId}/platform")
    @Produces(JSON)
    public Response addPlatform(
            @Context UriInfo uriInfo,
            @PathParam("ownerId") int ownerId,
            @FormParam("platformName") String platformName,
            @FormParam("price") int price,
            @FormParam("brandId") int brandId
    ) {
        Owner owner = ownerFacade.find(ownerId);
        PlatformBrand brand = platformBrandFacade.find(brandId);
        if (owner == null) {
            throw new DataNotFoundException("Owner with " + ownerId + " does not exist");
        } else if (brand == null || platformName == null) {
            throw new IllegalPropertyException("Did not recognize or ecountered faulty input parameters");
        } else {
            Platform platform = new Platform();
            platform.setPlatformName(platformName);
            platform.setPrice(price);
            platform.setBuyDate(new Date(System.currentTimeMillis()));
            platform.setBrand(brand);
            platformFacade.create(platform);
            platform.getOwnerList().add(owner);
            owner.getPlatformList().add(platform);
            platformFacade.edit(platform);
            ownerFacade.edit(owner);
            String selfUri = uriInfo.getBaseUriBuilder()
                    .path(PlatformController.class)
                    .path(Integer.toString(platform.getId()))
                    .build()
                    .toString();
            Link selfLink = new Link(selfUri, "self");
            String createUri = uriInfo.getBaseUriBuilder()
                    .path(OwnerController.class)
                    .path(Integer.toString(ownerId))
                    .path(PlatformController.class)
                    .build()
                    .toString();
            Link createLink = new Link(createUri, "create");
            PlatformWrapper wrappedPlatform = new PlatformWrapper();
            wrappedPlatform.setPlatform(platform);
            wrappedPlatform.getLinks().add(selfLink);
            wrappedPlatform.getLinks().add(createLink);
            return Response.status(Status.OK).entity(wrappedPlatform).build();
        }
    }

    @PUT
    @Path("{ownerId}/platform/{platformId}/game")
    @Produces(JSON)
    public Response addGame(
            @Context UriInfo uriInfo,
            @PathParam("ownerId") int ownerId,
            @PathParam("platformId") int platformId,
            @FormParam("gameName") String gameName,
            @FormParam("price") int price,
            @FormParam("ratingId") int ratingId,
            @FormParam("brandId") int brandId,
            @FormParam("genreIds") int genreIds
    ) {
        Owner owner = ownerFacade.find(ownerId);
        Platform platform = platformFacade.find(platformId);
        GameRating gameRating = gameRatingFacade.find(ratingId);
        GameBrand gameBrand = gameBrandFacade.find(brandId);
        Genre gameGenre = genreFacade.find(genreIds);
        Game game = new Game();
//        for (int genreId: genreIds) {
//        }
        game.setGameName(gameName);
        game.setPrice(price);
        game.setPlatform(platform);
        game.setRating(gameRating);
        game.setBrand(gameBrand);
        gameFacade.create(game);
        game.getGenreList().add(gameGenre);
        game.getOwnerList().add(owner);
        owner.getGameList().add(game);
        platform.getGameList().add(game);
        gameFacade.edit(game);
        ownerFacade.edit(owner);
        platformFacade.edit(platform);

        //TODO: Fix links
        return Response.status(Status.OK).entity(game).build();
    }
}
