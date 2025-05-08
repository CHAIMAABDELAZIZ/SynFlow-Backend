package com.example.backend.rest;

import org.springframework.stereotype.Component;

import com.example.backend.model.ApiResponse;
import com.example.backend.model.Book;
import com.example.backend.service.BookService;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
        return Response.ok(new ApiResponse<>(true, bookService.findAll(), "Books fetched successfully")).build();
    }

    @GET
    @Path("/{id}")
    public Response getBook(@PathParam("id") Long id) {
        return bookService.findById(id)
                .map(book -> Response.ok(new ApiResponse<>(true, book, 
                    String.format("Book with ID %d fetched successfully", id))).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Book with ID %d not found", id))).build());
    }

    @POST
    public Response createBook(Book book) {
        Book created = bookService.create(book);
        return Response.status(Response.Status.CREATED)
                .entity(new ApiResponse<>(true, created, 
                    String.format("Book created successfully with ID %d", created.getId())))
                .build();
    }

    @PUT
    @Path("/{id}")  
    public Response updateBook(@PathParam("id") Long id, Book book) {
        return bookService.update(id, book)
                .map(updated -> Response.ok(new ApiResponse<>(true, updated, 
                    String.format("Book with ID %d updated successfully", id))).build())
                .orElse(Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Book with ID %d not found", id))).build());
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBook(@PathParam("id") Long id) {
        return bookService.delete(id)
                ? Response.ok(new ApiResponse<>(true, null, 
                    String.format("Book with ID %d deleted successfully", id))).build()
                : Response.status(Response.Status.NOT_FOUND)
                        .entity(new ApiResponse<>(false, null, 
                            String.format("Book with ID %d not found", id))).build();
    }
}
