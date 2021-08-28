package io.chillplus.tvshow;

import java.util.List;

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


@Path("/api/tv")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TvShowResource {


    static long nextId = 0;

    final TvShowService tvShowService;

    public TvShowResource(TvShowService tvShowService) {
        this.tvShowService = tvShowService;
    }

    private List<TvShow> getTvShows() {
        return tvShowService.getTvShows();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        return Response.ok(getTvShows()).build();
    }

    @POST
    public Response create(CreateTvShowCommand createTvShowCommand) {
        if (createTvShowCommand.getTitle() == null 
            || createTvShowCommand.getTitle().isEmpty()) {
            throw new WebApplicationException("No title specified. A title is required", 400);
        }
        TvShow tvShow = new TvShow(nextId++, createTvShowCommand.title, createTvShowCommand.category);
        getTvShows().add(tvShow);
        return Response.ok(tvShow).status(201).build();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOneById(@PathParam("id") long id) {
        TvShow tvShow = getTvShows().stream().filter(t -> t.id == id).findFirst().orElse(null);
        return tvShow != null ? Response.ok(tvShow).build() : Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteById(@PathParam("id") long id) {
        return tvShowService.deleteById(id) ?  Response.ok().build() :  Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("/")
    public Response deleteAll() {
        getTvShows().clear();
        return Response.ok().build();
    }
}
