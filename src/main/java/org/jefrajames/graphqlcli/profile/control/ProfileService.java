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
package org.jefrajames.graphqlcli.profile.control;

import io.aexp.nodes.graphql.Argument;
import io.aexp.nodes.graphql.Arguments;
import io.aexp.nodes.graphql.GraphQLRequestEntity;
import io.aexp.nodes.graphql.GraphQLResponseEntity;
import io.aexp.nodes.graphql.GraphQLTemplate;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import lombok.extern.java.Log;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jefrajames.graphqlcli.profile.entity.FindProfileByPersonIdRequest;
import org.jefrajames.graphqlcli.profile.entity.FindProfileFullByPersonIdRequest;

/**
 *
 * @author jefrajames
 */
@Log
@ApplicationScoped
public class ProfileService {

    @ConfigProperty(name = "profile.graphql.endpoint", defaultValue = "http://localhost:8080/graphql")
    String endpoint;

    private URL url;

    @PostConstruct
    public void postConstruct() {
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void handleErrors(String useCase, io.aexp.nodes.graphql.internal.Error[] errors) {
        if (errors != null) {
            for (int i = 0; i < errors.length; i++) {
                io.aexp.nodes.graphql.internal.Error error = errors[i];
                log.warning(useCase + " NOK, GraphQL error=" + error.getMessage());
            }
            throw new RuntimeException("GraphQL error");
        }
    }

    public FindProfileByPersonIdRequest findProfileById(int id) {

        GraphQLRequestEntity requestEntity;
        try {
            requestEntity = GraphQLRequestEntity.Builder()
                    .url(url.toString())
                    .arguments(new Arguments("profile", new Argument<>("personId", id)))
                    .scalars(List.of(Date.class, BigDecimal.class))
                    .request(FindProfileByPersonIdRequest.class)
                    .headers(Collections.singletonMap("Authorization", "Bearer jwtoken"))
                    .build();
        } catch (MalformedURLException shouldNotHappen) {
            throw new RuntimeException(shouldNotHappen);
        }

        log.info("findProfileById.requestEntity=" + requestEntity.getRequest());

        GraphQLTemplate graphQLTemplate = new GraphQLTemplate();
        GraphQLResponseEntity<FindProfileByPersonIdRequest> responseEntity = graphQLTemplate.query(requestEntity, FindProfileByPersonIdRequest.class);

        log.info("findProfileById.responseEntity=" + responseEntity);
        handleErrors("findProfileById", responseEntity.getErrors());

        return responseEntity.getResponse();
    }

    public FindProfileFullByPersonIdRequest findProfileFullById(int id) {

        GraphQLRequestEntity requestEntity;
        try {
            requestEntity = GraphQLRequestEntity.Builder()
                    .url(url.toString())
                    .arguments(new Arguments("profileFull", new Argument<>("personId", id)))
                    .request(FindProfileFullByPersonIdRequest.class)
                    .build();
        } catch (MalformedURLException shouldNotHappen) {
            throw new RuntimeException(shouldNotHappen);
        }

        log.info("findProfileFullById.requestEntity=" + requestEntity);

        GraphQLTemplate graphQLTemplate = new GraphQLTemplate();
        GraphQLResponseEntity<FindProfileFullByPersonIdRequest> responseEntity = graphQLTemplate.query(requestEntity, FindProfileFullByPersonIdRequest.class);

        log.info("findProfileFullById.responseEntity=" + responseEntity);
        handleErrors("findProfileFullById", responseEntity.getErrors());

        return responseEntity.getResponse();
    }

}
