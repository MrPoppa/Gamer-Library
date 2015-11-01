package com.benji.controllers;

import com.benji.ejb.OwnerFacade;
import com.benji.entities.Owner;
import com.benji.entitywrappers.OwnerWrapper;
import com.benji.utils.Link;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
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
@Path("owner")
public class OwnerController {

    private final String JSON = MediaType.APPLICATION_JSON;

    @EJB
    OwnerFacade ownerFacade;

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
            OwnerWrapper wrappedOwner = new OwnerWrapper(owner, link);
            wrappedOwners.add(wrappedOwner);
        }
        GenericEntity<List<OwnerWrapper>> ownerList = 
                new GenericEntity<List<OwnerWrapper>>(wrappedOwners) {
        };
        return Response.status(Status.OK).entity(ownerList).build();
    }

    @GET
    @Produces(JSON)
    @Path("{ownerId}")
    public Response getOwnerById(
            @Context UriInfo uriInfo,
            @PathParam("ownerId") int ownerId
    ) {
        Owner owner = ownerFacade.find(ownerId);
        if (owner == null) {
            return Response.status(Status.NOT_FOUND).build();
        } else {
            String uri = uriInfo.getBaseUriBuilder()
                    .path(OwnerController.class)
                    .path(Integer.toString(ownerId))
                    .build()
                    .toString();
            Link link = new Link(uri, "self");
            OwnerWrapper ownerWrapper = new OwnerWrapper(owner, link);
            return Response.status(Status.OK).entity(ownerWrapper).build();
        }
    }

    @POST
    @Produces(JSON)
    public Response createOwner(
            @Context UriInfo uriInfo,
            @FormParam("firstName") String firstName,
            @FormParam("lastName") String lastName,
            @FormParam("email") String email,
            @FormParam("ssn") String ssn,
            @FormParam("userName") String userName,
            @FormParam("password") String password
    ) {
        if (firstName == null
                || lastName == null
                || email == null
                || ssn == null
                || userName == null
                || password == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            Owner owner = new Owner();
            owner.setFirstName(firstName);
            owner.setLastName(lastName);
            owner.setEmail(email);
            owner.setSsn(ssn);
            owner.setUserName(userName);
            owner.setPassword(password);
            String uri = uriInfo.getBaseUriBuilder()
                    .path(OwnerController.class)
                    .build()
                    .toString();
            Link link = new Link(uri, "create");
            OwnerWrapper ownerWrapper = new OwnerWrapper(owner, link);
            ownerFacade.create(owner);
            return Response.status(Response.Status.OK).entity(ownerWrapper).build();
        }
    }

    @PUT
    @Path("{ownerId}")
    @Produces(JSON)
    public Response updateOwner(
            @Context UriInfo uriInfo,
            @PathParam("ownerId") int ownerId,
            @FormParam("firstName") String firstName,
            @FormParam("lastName") String lastName,
            @FormParam("email") String email,
            @FormParam("ssn") String ssn,
            @FormParam("userName") String userName,
            @FormParam("password") String password
    ) {
        Owner owner = ownerFacade.find(ownerId);
        if (firstName == null
                || lastName == null
                || email == null
                || ssn == null
                || userName == null
                || password == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else if (owner == null) {
            return Response.status(Status.NOT_FOUND).build();
        } else {
            owner.setFirstName(firstName);
            owner.setLastName(lastName);
            owner.setEmail(email);
            owner.setSsn(ssn);
            owner.setUserName(userName);
            owner.setPassword(password);
            String uri = uriInfo.getBaseUriBuilder()
                    .path(OwnerController.class)
                    .path(Integer.toString(ownerId))
                    .build()
                    .toString();
            Link link = new Link(uri, "update");
            OwnerWrapper ownerWrapper = new OwnerWrapper(owner, link);
            ownerFacade.edit(owner);
            return Response.status(Status.OK).entity(ownerWrapper).build();
        }
    }

    @DELETE
    @Path("{ownerId}/delete")
    @Produces(JSON)
    public Response deleteOwner(@Context UriInfo uriInfo, @PathParam("ownerId") int ownerId) {
        Owner owner = ownerFacade.find(ownerId);
        System.out.println(ownerId);
        if (owner != null) {
            String uri = uriInfo.getBaseUriBuilder()
                    .path(OwnerController.class)
                    .path(Integer.toString(ownerId))
                    .path("/delete")
                    .build()
                    .toString();
            Link link = new Link(uri, "delete");
            OwnerWrapper ownerWrapper = new OwnerWrapper(owner, link);
            ownerFacade.remove(owner);
            return Response.status(Status.OK).entity(ownerWrapper).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
}
