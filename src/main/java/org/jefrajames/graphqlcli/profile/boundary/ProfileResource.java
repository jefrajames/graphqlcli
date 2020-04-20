/*
 * Copyright 2020 jefrajames.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jefrajames.graphqlcli.profile.boundary;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.extern.java.Log;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jefrajames.graphqlcli.profile.entity.FindProfileByPersonIdRequest;
import org.jefrajames.graphqlcli.profile.entity.FindProfileFullByPersonIdRequest;
import org.jefrajames.graphqlcli.profile.control.ProfileService;

/**
 *
 * @author jefrajames
 */
@Path("/profiles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Log
public class ProfileResource {

    @Inject
    ProfileService profileService;

    @APIResponses(
            value = {
                @APIResponse(
                        responseCode = "404",
                        description = "Profile not found",
                        content = @Content(mediaType = "text/plain")),
                @APIResponse(
                        responseCode = "200",
                        description = "Profile with the given person id.",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = FindProfileByPersonIdRequest.class)))})
    @Operation(
            summary = "Find a profile by person id.",
            description = "Find a profile by person id from the GraphQL backend. No score returned.")
    @GET
    @Path("{personId}")
    public Response findProfileById(@Parameter(description = "The person id(recommended value from 1 to 100).", required = true) 
                                            @PathParam(value = "personId") int id) {
        FindProfileByPersonIdRequest profile = profileService.findProfileById(id);
        if (profile == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Profile with id not found:" + id).build();
        }
        return Response.ok(profile).build();
    }
    
    @APIResponses(
            value = {
                @APIResponse(
                        responseCode = "404",
                        description = "Profile not found",
                        content = @Content(mediaType = "text/plain")),
                @APIResponse(
                        responseCode = "200",
                        description = "Profile with the given person id.",
                        content = @Content(mediaType = "application/json",
                                schema = @Schema(implementation = FindProfileFullByPersonIdRequest.class)))})
    @Operation(
            summary = "Find a full profile by person id.",
            description = "Find a full profile including scores by person id from the GraphQL backend.")
    @GET
    @Path("/full/{personId}")
    public Response findFullProfileById(@Parameter(description = "The person id(value from 1 to 100).", required = true) 
                                            @PathParam(value = "personId") int id) {
        FindProfileFullByPersonIdRequest profileFull = profileService.findProfileFullById(id);
        if (profileFull == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("FullProfile with id not found:" + id).build();
        }
        return Response.ok(profileFull).build();
    }


}
