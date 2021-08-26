package io.chillplus.tvshow;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

//import org.jboss.logging.Logger;


@Path("/api/tv")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TvShowResource {

//    private static final Logger LOG = Logger.getLogger(TvShowResource.class);

    static long nextId = 0;
    static List<TvShow> tvShows = new ArrayList<>();

    public TvShowResource() {
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        return Response.ok(tvShows).build();
    }

    @POST
    public Response create(CreateTvShowCommand createTvShowCommand) {
        if (createTvShowCommand.getTitle() == null 
            || createTvShowCommand.getTitle().isEmpty()) {
            throw new WebApplicationException("No title specified. A title is required", 400);
        }
        TvShow tvShow = new TvShow(nextId++, createTvShowCommand.title, createTvShowCommand.category);
        tvShows.add(tvShow);
        return Response.ok(tvShow).status(201).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOneById(@PathParam("id") long id) {
        TvShow tvShow = tvShows.stream().filter(t -> t.id == id).findFirst().orElse(null);
        return tvShow != null ? Response.ok(tvShow).build() : Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteById(@PathParam("id") long id) {
        tvShows = tvShows.stream().filter(t -> t.id != id).collect(Collectors.toList());
        return Response.ok().build();
    }

    @DELETE
    @Path("/")
    public Response deleteAll() {
        tvShows.clear();
        return Response.ok().build();
    }
}
