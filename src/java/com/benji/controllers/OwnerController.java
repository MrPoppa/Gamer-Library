package com.benji.controllers;

import com.benji.ejb.OwnerFacade;
import com.benji.entities.Owner;
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
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

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
    public Response getAllOwners() {
        List<Owner> owners = ownerFacade.findAll();
        GenericEntity<List<Owner>> ownerList = new GenericEntity<List<Owner>>(owners) {
        };
        return Response.status(Status.OK).entity(ownerList).build();
    }

    @GET
    @Produces(JSON)
    @Path("{ownerId}")
    public Response getOwnerById(@PathParam("ownerId") int ownerId) {
        Owner owner = ownerFacade.find(ownerId);
        return Response.status(Status.OK).entity(owner).build();
    }

    @POST
    @Produces(JSON)
    public Response createOwner(
            @FormParam("firstName") String firstName,
            @FormParam("lastName") String lastName,
            @FormParam("ssn") String ssn,
            @FormParam("userName") String userName,
            @FormParam("password") String password
    ) {
        if (firstName == null
                || lastName == null
                || ssn == null
                || userName == null
                || password == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            Owner owner = new Owner();
            owner.setFirstName(firstName);
            owner.setLastName(lastName);
            owner.setSsn(ssn);
            owner.setUserName(userName);
            owner.setPassword(password);
            ownerFacade.create(owner);
            return Response.status(Response.Status.OK).entity(owner).build();
        }
    }

    @PUT
    @Path("{ownerId}")
    @Produces(JSON)
    public Response updateOwner(
            @PathParam("ownerId") int ownerId,
            @FormParam("firstName") String firstName,
            @FormParam("lastName") String lastName,
            @FormParam("ssn") String ssn,
            @FormParam("userName") String userName,
            @FormParam("password") String password
    ) {
        if (firstName == null
                || lastName == null
                || ssn == null
                || userName == null
                || password == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            Owner owner = ownerFacade.find(ownerId);
            owner.setFirstName(firstName);
            owner.setLastName(lastName);
            owner.setSsn(ssn);
            owner.setUserName(userName);
            owner.setPassword(password);
            ownerFacade.edit(owner);
            return Response.status(Status.OK).entity(owner).build();
        }
    }

    @DELETE
    @Path("{ownerId}/delete")
    @Produces(JSON)
    public Response deleteOwner(@PathParam("ownerId") int ownerId) {
        Owner owner = ownerFacade.find(ownerId);
        System.out.println(ownerId);
        if (owner != null) {
            ownerFacade.remove(owner);
            return Response.status(Status.OK).entity(owner).build();
        } else {
            return Response.status(Status.NOT_FOUND).build();
        }
    }
}
