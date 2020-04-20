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
package org.jefrajames.graphqlcli.person.control;

import io.aexp.nodes.graphql.Argument;
import io.aexp.nodes.graphql.Arguments;
import io.aexp.nodes.graphql.GraphQLRequestEntity;
import io.aexp.nodes.graphql.GraphQLRequestEntity.RequestBuilder;
import io.aexp.nodes.graphql.GraphQLResponseEntity;
import io.aexp.nodes.graphql.GraphQLTemplate;
import io.aexp.nodes.graphql.InputObject;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import lombok.extern.java.Log;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jefrajames.graphqlcli.person.entity.AddInputPerson;
import org.jefrajames.graphqlcli.person.entity.AddPersonRequest;
import org.jefrajames.graphqlcli.person.entity.FindAllPeople;
import org.jefrajames.graphqlcli.person.entity.FindAllPeopleRequest;
import org.jefrajames.graphqlcli.person.entity.FindPeopleBySurnameRequest;
import org.jefrajames.graphqlcli.person.entity.FindPersonByIdRequest;
import org.jefrajames.graphqlcli.person.entity.PersonBySurname;
import org.jefrajames.graphqlcli.person.entity.UpdateInputPerson;
import org.jefrajames.graphqlcli.person.entity.UpdatePersonRequest;

/**
 *
 * @author jefrajames
 */
@Log
@ApplicationScoped
public class PersonService {

    @ConfigProperty(name = "person.graphql.endpoint", defaultValue = "http://localhost:8080/graphql")
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

    public List<FindAllPeople> findAllPeople() {

        RequestBuilder builder = GraphQLRequestEntity.Builder();
        try {
            builder.url(url.toString());
            builder.scalars(LocalDate.class);
            builder.request(FindAllPeopleRequest.class);
        } catch (MalformedURLException shouldNotHappen) {
            throw new RuntimeException(shouldNotHappen);
        }

        GraphQLRequestEntity requestEntity = builder.build();

        log.info("findAllPeople.requestEntity=" + requestEntity);

        GraphQLTemplate graphQLTemplate = new GraphQLTemplate();
        GraphQLResponseEntity<FindAllPeopleRequest> responseEntity = graphQLTemplate.query(requestEntity, FindAllPeopleRequest.class);

        log.info("findAllPeople.responseEntity=" + responseEntity);
        handleErrors("findAllPeople", responseEntity.getErrors());

        return responseEntity.getResponse().getPeople();
    }

    public List<PersonBySurname> findPeopleBySurname(String surname) {

        GraphQLRequestEntity requestEntity;
        try {
            requestEntity = GraphQLRequestEntity.Builder()
                    .url(endpoint)
                    .arguments(new Arguments("personsWithSurname", new Argument<>("surname", surname)))
                    .request(FindPeopleBySurnameRequest.class)
                    .scalars(LocalDate.class)
                    .build();
        } catch (MalformedURLException shouldNotHappen) {
            throw new RuntimeException(shouldNotHappen);
        }

        log.info("findPersonBySurname.requestEntity=" + requestEntity);

        GraphQLTemplate graphQLTemplate = new GraphQLTemplate();
        GraphQLResponseEntity<FindPeopleBySurnameRequest> responseEntity = graphQLTemplate.query(requestEntity, FindPeopleBySurnameRequest.class);

        log.info("findPersonBySurname.responseEntity=" + responseEntity);
        handleErrors("findPersonBySurname", responseEntity.getErrors());

        return responseEntity.getResponse().getPersonsWithSurname();
    }

    public FindPersonByIdRequest findPersonById(int id) {

        GraphQLRequestEntity requestEntity;

        try {
            requestEntity = GraphQLRequestEntity.Builder()
                    .url(url.toString())
                    .arguments(new Arguments("person", new Argument<>("personId", id)))
                    .request(FindPersonByIdRequest.class)
                    // .headers(Collections.singletonMap("Content-Type", "application/json; charset=UTF-8"))
                    .build();
        } catch (MalformedURLException shouldNotHappen) {
            throw new RuntimeException(shouldNotHappen);
        }

        log.info("findPersonById.requestEntity=" + requestEntity);

        GraphQLTemplate graphQLTemplate = new GraphQLTemplate();
        GraphQLResponseEntity<FindPersonByIdRequest> responseEntity = graphQLTemplate.query(requestEntity, FindPersonByIdRequest.class);

        log.info("findPersonById.responseEntity=" + responseEntity);
        handleErrors("updatePerson", responseEntity.getErrors());

        return responseEntity.getResponse();
    }

    public AddPersonRequest addPerson(AddInputPerson inputPerson) {

        InputObject inputObject = new InputObject.Builder<>()
                .put("names", inputPerson.getNames())
                .put("surname", inputPerson.getSurname()) // Adding the surname is required for the server to avoid a server NPE
                .put("birthDate", inputPerson.getBirthDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .build();
        
        log.info("JJS => inputObject=" + inputObject.getMap());

        GraphQLRequestEntity.RequestBuilder builder = GraphQLRequestEntity.Builder();

        try {
            builder.url(url.toString());
        } catch (MalformedURLException shouldNotHappen) {
            throw new RuntimeException(shouldNotHappen);
        }
        builder.arguments(new Arguments("updatePerson", new Argument<>("person", inputObject)));
        builder.request(AddPersonRequest.class);
        builder.scalars(LocalDate.class);
        builder.requestMethod(GraphQLTemplate.GraphQLMethod.MUTATE);

        GraphQLRequestEntity requestEntity = builder.build();

        log.info("addPerson.requesEntity=" + requestEntity);

        GraphQLTemplate graphQLTemplate = new GraphQLTemplate();
        GraphQLResponseEntity<AddPersonRequest> responseEntity = graphQLTemplate.mutate(requestEntity, AddPersonRequest.class);

        log.info("addPerson.responseEntity=" + responseEntity);
        handleErrors("addPerson", responseEntity.getErrors());

        return responseEntity.getResponse();
    }

    public UpdatePersonRequest updatePerson(UpdateInputPerson inputPerson) {

        InputObject inputObject = new InputObject.Builder<>()
                .put("id", inputPerson.getId())
                .put("names", inputPerson.getNames())
                .put("surname", inputPerson.getSurname())
                .put("birthDate", inputPerson.getBirthDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .put("interests", inputPerson.getInterests())
                .build();

        log.info("JJS => updatePerson.inputObject.map=" + inputObject.getMap());
        
        GraphQLRequestEntity requestEntity;
        try {
            requestEntity = GraphQLRequestEntity.Builder()
                    .url(url.toString())
                    .arguments(new Arguments("updatePerson", new Argument<>("person", inputObject)))
                    .request(UpdatePersonRequest.class)
                    .scalars(LocalDate.class) // Required ?
                    .requestMethod(GraphQLTemplate.GraphQLMethod.MUTATE)
                    .build();
        } catch (MalformedURLException shouldNotHappen) {
            throw new RuntimeException(shouldNotHappen);
        }

        log.info("updatePerson.requestEntity=" + requestEntity);

        GraphQLTemplate graphQLTemplate = new GraphQLTemplate();
        GraphQLResponseEntity<UpdatePersonRequest> responseEntity = graphQLTemplate.mutate(requestEntity, UpdatePersonRequest.class);

        log.info("updatePerson.responseEntity=" + responseEntity);
        handleErrors("updatePerson", responseEntity.getErrors());

        return responseEntity.getResponse();
    }

}
