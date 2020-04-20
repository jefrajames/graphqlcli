package org.jefrajames.graphqlcli.person.boundary;

import java.net.URI;
import org.jefrajames.graphqlcli.person.entity.AddInputPerson;
import java.util.List;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import lombok.extern.java.Log;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jefrajames.graphqlcli.person.entity.AddPersonRequest;
import org.jefrajames.graphqlcli.person.entity.FindAllPeople;
import org.jefrajames.graphqlcli.person.entity.FindPersonByIdRequest;
import org.jefrajames.graphqlcli.person.entity.PersonBySurname;
import org.jefrajames.graphqlcli.person.control.PersonService;
import org.jefrajames.graphqlcli.person.entity.UpdateInputPerson;
import org.jefrajames.graphqlcli.person.entity.UpdatePersonRequest;

@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log
public class PersonResource {
    
    @Context
    UriInfo uriInfo;

    @Inject
    PersonService personService;

    @APIResponses(
            value = {
                @APIResponse(
                        responseCode = "200",
                        description = "All people.",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = FindAllPeople.class)))})
    @Operation(
            summary = "Find all people.",
            description = "Find all people from the GraphQL backend.")
    @GET
    public List<FindAllPeople> findAllPeople() {
        return personService.findAllPeople();
    }

    @APIResponses(
            value = {
                @APIResponse(
                        responseCode = "404",
                        description = "Person not found",
                        content = @Content(mediaType = "text/plain")),
                @APIResponse(
                        responseCode = "200",
                        description = "Person with the given id.",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = FindPersonByIdRequest.class)))})
    @Operation(
            summary = "Find a person by her id.",
            description = "Find a person by her id from the GraphQL backend.")
    @GET
    @Path("{id}")
    public Response findById(@Parameter(description = "The person id to be fetched (recommended value from 1 to 100).",
            required = true)
            @PathParam(value = "id") int id) {
        FindPersonByIdRequest person = personService.findPersonById(id);
        if (person == null) {
            return Response.status(Status.NOT_FOUND).entity("Person with id not found:" + id).build();
        }
        return Response.ok(person).build();
    }

    @APIResponses(
            value = {
                @APIResponse(
                        responseCode = "200",
                        description = "List of people with the given surname.",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = PersonBySurname.class)))
            }
    )
    @Operation(
            summary = "Find people by surname.",
            description = "Find people by surname from the GraphQL backend. Returned list can be empty.")

    @GET
    @Path("surname/{surname}")
    public List<PersonBySurname> findPeopleBySurname(@Parameter(description = "The surname to be fetched.", required = true)
            @PathParam(value = "surname") String surname) {
        return personService.findPeopleBySurname(surname);

    }

    @APIResponses(
            value = {
                @APIResponse(
                        responseCode = "201",
                        description = "Person added.",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = AddPersonRequest.class)))})
    @Operation(
            summary = "Add a person.",
            description = "Add a person to the GraphQL backend.")
    @POST
    public Response add(@RequestBody(description = "The person to be added.",
            required = true,
            content = @Content(schema = @Schema(implementation = AddInputPerson.class))) AddInputPerson personInput) {
        AddPersonRequest person = personService.addPerson(personInput);
        
        URI personUri = uriInfo.getBaseUriBuilder().path(PersonResource.class).path(PersonResource.class, "findById").build(person.getId());

        return Response.created(personUri).entity(person).build();
    }

    @APIResponses(
            value = {
                @APIResponse(
                        responseCode = "201",
                        description = "Person updated.",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = UpdatePersonRequest.class)))})
    @Operation(
            summary = "Update a person.",
            description = "Update a person to the GraphQL backend.")
    @PATCH
    public UpdatePersonRequest update(@RequestBody(description = "The person to be updated.",
            required = true,
            content = @Content(schema = @Schema(implementation = UpdateInputPerson.class))) UpdateInputPerson personInput) {
        return personService.updatePerson(personInput);
    }

}
