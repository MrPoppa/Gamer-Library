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
public class DataNotFoundExceptionMapper implements ExceptionMapper<DataNotFoundException>{

    @Override
    public Response toResponse(DataNotFoundException exception) {
        ErrorMessage message = new ErrorMessage(exception.getMessage(),404,"http://cdn.instructables.com/FTU/1BBR/FLI8MT4O/FTU1BBRFLI8MT4O.MEDIUM.jpg");
        return Response.status(Response.Status.NOT_FOUND).entity(message).build();
    }

}
