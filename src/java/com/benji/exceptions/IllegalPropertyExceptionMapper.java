package com.benji.exceptions;

import com.benji.entities.ErrorMessage;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author Benjamin
 */
@Provider
public class IllegalPropertyExceptionMapper implements ExceptionMapper<IllegalPropertyException> {

    @Override
    public Response toResponse(IllegalPropertyException exception) {
            ErrorMessage errorMessage = new ErrorMessage(exception.getMessage(), 400, "http://digitalunite.com/guides/computer-basics/how-to-use-a-computer-keyboard");
            return Response.status(Response.Status.BAD_REQUEST).entity(errorMessage).build();
    }

}
