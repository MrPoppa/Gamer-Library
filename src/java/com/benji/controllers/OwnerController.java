package com.benji.controllers;

import com.benji.ejb.GameBrandFacade;
import com.benji.ejb.GameFacade;
import com.benji.ejb.GameRatingFacade;
import com.benji.ejb.GameReceiptFacade;
import com.benji.ejb.GenreFacade;
import com.benji.ejb.OwnerFacade;
import com.benji.ejb.PlatformBrandFacade;
import com.benji.ejb.PlatformFacade;
import com.benji.ejb.PlatformReceiptFacade;
import com.benji.entities.Game;
import com.benji.entities.GameReceipt;
import com.benji.entities.Owner;
import com.benji.entities.Platform;
import com.benji.entitywrappers.OwnerWrapper;
import com.benji.entitywrappers.PlatformWrapper;
import com.benji.entities.Link;
import com.benji.entities.PlatformReceipt;
import com.benji.exceptions.DataNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
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
@Path("/owner")
public class OwnerController {

    private static final String JSON = MediaType.APPLICATION_JSON;

    @EJB
    OwnerFacade ownerFacade;
    @EJB
    PlatformFacade platformFacade;
    @EJB
    GameFacade gameFacade;
    @EJB
    GameReceiptFacade gameRecieptFacade;
    @EJB
    PlatformReceiptFacade platformReceiptFacade;

    /**
     * Method to fetch all Owner objects saved in the database.
     *
     * @param uriInfo Context used to build URI leading to specific requests.
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
     * @param uriInfo Context used to build URI leading to specific requests.
     * @param ownerId Specifies the id of the wanted Owner object.
     * @return The Owner object with the specific ownerId including a response
     * code
     */
    @GET
    @Produces(JSON)
    @Path("{ownerId}")
    public Response getOwnerById(
            @Context UriInfo uriInfo,
            @PathParam("ownerId") Integer ownerId
    ) {
        Owner owner = ownerFacade.find(ownerId);
        if (owner == null) {
            throw new DataNotFoundException("Owner with "
                    + ownerId + " does not exist");
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
            @Size(max = 24)
            @FormParam("firstName") String firstName,
            @NotNull
            @Size(max = 24)
            @FormParam("lastName") String lastName,
            @NotNull
            @Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'"
                    + "*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)"
                    + "+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
                    message = "Invalid email")
            @FormParam("email") String email,
            @NotNull
            @Size(min = 8, max = 12,
                    message = "Social security number must be 8 or 12 characters")
            @Pattern(regexp = "\\d+",
                    message = "Social security number must only contain numbers")
            @FormParam("ssn") String ssn,
            @NotNull
            @Size(min = 2, message = "Username must contain atleast 2 characters.")
            @FormParam("userName") String userName,
            @NotNull
            @Size(min = 6, message = "Password needs to be atleast 6 characters.")
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
            @PathParam("ownerId") Integer ownerId,
            @NotNull
            @FormParam("firstName") String firstName,
            @NotNull
            @FormParam("lastName") String lastName,
            @NotNull
            @Pattern(regexp = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'"
                    + "*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)"
                    + "+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
                    message = "Invalid email")
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
            throw new DataNotFoundException("Owner with "
                    + ownerId + " does not exist");
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
            @PathParam("ownerId") Integer ownerId
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
            throw new DataNotFoundException("Owner with "
                    + ownerId + " does not exist");
        }
    }

    @GET
    @Path("{ownerId}/platform")
    @Produces(JSON)
    public Response getAllOwnedPlatforms(
            @Context UriInfo uriInfo,
            @Context Request request,
            @PathParam("ownerId") Integer ownerId
    ) {
        List<Platform> platforms
                = platformFacade.getAllPlatformsByOwnerId(ownerId);
        List<PlatformWrapper> wrappedPlatforms = new ArrayList<>();
        int hashValue = 0;
        for (Platform platform : platforms) {
            String selfUri = uriInfo.getBaseUriBuilder()
                    .path(PlatformController.class)
                    .path(Integer.toString(platform.getId()))
                    .build()
                    .toString();
            Link link = new Link(selfUri, "self");
            PlatformWrapper wrappedPlatform = new PlatformWrapper();
            wrappedPlatform.setPlatform(platform);
            wrappedPlatform.setPlatformReceipt(ownerFacade.getPlatformRecieiptByPlatformAndOwnerId(platform.getId(), ownerId));
            wrappedPlatform.getLinks().add(link);
            wrappedPlatforms.add(wrappedPlatform);
            hashValue += wrappedPlatform.hashCode();
        }

        GenericEntity<List<PlatformWrapper>> platformList
                = new GenericEntity<List<PlatformWrapper>>(wrappedPlatforms) {
                };

        CacheControl cc = new CacheControl();
        cc.setMaxAge(86400);
        cc.setPrivate(true);
        EntityTag etag = new EntityTag(Integer.toString(hashValue));
        Response.ResponseBuilder rbuilder = request.evaluatePreconditions(etag);
        if (rbuilder == null) {
            rbuilder = Response.ok(platformList);
            rbuilder.tag(etag);
        }
        rbuilder.cacheControl(cc).build();
        return rbuilder.build();
//                Response.status(Status.OK).entity(platformList).build();
    }

    @PUT
    @Path("{ownerId}/platform")
    @Produces(JSON)
    public Response addPlatform(
            @Context UriInfo uriInfo,
            @PathParam("ownerId") Integer ownerId,
            @NotNull
            @FormParam("platformId") Integer platformId,
            @NotNull
            @FormParam("price") Integer price
    ) {
        Owner owner = ownerFacade.find(ownerId);
        Platform platform = platformFacade.find(platformId);
        if (owner == null) {
            throw new DataNotFoundException("Owner with "
                    + ownerId + " does not exist.");
        } else if (platform == null) {
            throw new DataNotFoundException("Platform with "
                    + platformId + " does not exist.");
        } else {
            PlatformReceipt platformReceipt = new PlatformReceipt();
            platformReceipt.setOwner(owner);
            platformReceipt.setPlatform(platform);
            platformReceipt.setPrice(price);
            platformReceipt.setBuyDate(new Date(System.currentTimeMillis()));
            platformReceiptFacade.create(platformReceipt);
            platform.getOwnerList().add(owner);
            platform.getPlatformReceiptList().add(platformReceipt);
            owner.getPlatformReceiptList().add(platformReceipt);
            owner.getPlatformList().add(platform);
            platformReceiptFacade.edit(platformReceipt);
            platformFacade.edit(platform);
            ownerFacade.edit(owner);
            String selfUri = uriInfo.getBaseUriBuilder()
                    .path(PlatformController.class)
                    .path(Integer.toString(platform.getId()))
                    .build()
                    .toString();
            Link selfLink = new Link(selfUri, "self");
            String addPlatformUri = uriInfo.getBaseUriBuilder()
                    .path(OwnerController.class)
                    .path(Integer.toString(ownerId))
                    .path(PlatformController.class)
                    .build()
                    .toString();
            Link createLink = new Link(addPlatformUri, "add");
            PlatformWrapper wrappedPlatform = new PlatformWrapper();
            wrappedPlatform.setPlatform(platform);
            wrappedPlatform.getLinks().add(selfLink);
            wrappedPlatform.getLinks().add(createLink);
            return Response.status(Status.OK).entity(wrappedPlatform).build();
        }
    }

    @PUT
    @Path("{ownerId}/game")
    @Produces(JSON)
    public Response addGame(
            @Context UriInfo uriInfo,
            @PathParam("ownerId") Integer ownerId,
            @NotNull
            @FormParam("platformId") Integer platformId,
            @NotNull
            @FormParam("gameId") Integer gameId,
            @NotNull
            @FormParam("price") Integer price
    ) {
        Owner owner = ownerFacade.find(ownerId);
        Platform platform = platformFacade.find(platformId);
        Game game = gameFacade.find(gameId);
        GameReceipt gameReciept = new GameReceipt();
        gameReciept.setBuyDate(new Date(System.currentTimeMillis()));
        gameReciept.setGame(game);
        gameReciept.setPrice(price);
        gameReciept.setOwner(owner);
        game.getOwnerList().add(owner);
        owner.getGameList().add(game);
        platform.getGameList().add(game);
        gameRecieptFacade.create(gameReciept);
        gameFacade.edit(game);
        ownerFacade.edit(owner);
        platformFacade.edit(platform);

        //TODO: Fix links
        return Response.status(Status.OK).entity(game).build();
    }
}
