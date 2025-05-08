package com.example.backend.rest;

import com.example.backend.model.Book;
import com.example.backend.service.BookService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.stereotype.Component;

@Component
@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

    private final BookService bookService;

    public BookResource(BookService bookService) {
        this.bookService = bookService;
    }

    @GET
    public Response getAllBooks() {
        return Response.ok(bookService.findAll()).build();
    }

    @GET
    @Path("/{id}")
    public Response getBook(@PathParam("id") Long id) {
        return bookService.findById(id)
                .map(book -> Response.ok(book).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    public Response createBook(Book book) {
        return Response.status(Response.Status.CREATED)
                .entity(bookService.create(book))
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response updateBook(@PathParam("id") Long id, Book book) {
        return bookService.update(id, book)
                .map(updated -> Response.ok(updated).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") Long id) {
        return bookService.delete(id) 
                ? Response.noContent().build() 
                : Response.status(Response.Status.NOT_FOUND).build();
    }
}
