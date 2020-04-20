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

import org.jefrajames.graphqlcli.person.entity.FindPersonByIdRequest;
import org.jefrajames.graphqlcli.person.entity.AddPersonRequest;
import org.jefrajames.graphqlcli.person.entity.FindAllPeople;
import org.jefrajames.graphqlcli.person.entity.UpdatePersonRequest;
import org.jefrajames.graphqlcli.person.entity.PersonBySurname;
import org.jefrajames.graphqlcli.person.entity.UpdateInputPerson;
import org.jefrajames.graphqlcli.person.entity.AddInputPerson;
import io.quarkus.test.junit.QuarkusTest;
import java.time.LocalDate;
import java.util.List;
import javax.inject.Inject;
import lombok.extern.java.Log;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

/**
 *
 * @author jefrajames
 */
@Log
@QuarkusTest
public class PersonServiceTest {

    @Inject
    PersonService personService;

    @Test
    public void testFindAllPeople() {
        List<FindAllPeople> allPeople = personService.findAllPeople();
        log.info("testFindAllPeople.allPeople.size=" + allPeople.size());
        assertTrue(allPeople.size() >= 100);
    }

    @Test
    public void testFindAllPeopleBySurname() {
        List<PersonBySurname> peopleBySurname = personService.findPeopleBySurname("James");
        log.info("testFindAllPeopleBySurname.peopleBySurname=" + peopleBySurname);
    }

    @Test
    public void testFindPersonById() {
        FindPersonByIdRequest person = personService.findPersonById(30);
        assertTrue(person.getId() == 30);
    }

    @Test
    public void testAddPerson() {
        AddInputPerson inputPerson = new AddInputPerson();
        inputPerson.setNames(List.of("Jean-Francois", "Alphonse"));
        inputPerson.setSurname("James");
        inputPerson.setBirthDate(LocalDate.of(1962, 4, 27));

        AddPersonRequest createdPerson = personService.addPerson(inputPerson);

        assertTrue(createdPerson.getNames().length == 2);
        assertTrue("Jean-Francois".equals(createdPerson.getNames()[0]));
    }

    @Test
    public void testUpdatePerson() {
        UpdateInputPerson inputPerson = new UpdateInputPerson();
        inputPerson.setId(101);
        inputPerson.setNames(List.of("Jean-Francois", "Alphonse"));
        inputPerson.setSurname("James");
        inputPerson.setBirthDate(LocalDate.of(1962, 4, 27));
        inputPerson.setInterests(List.of("Martial arts", "Food", "Cinema", "Music", "Trek"));

        UpdatePersonRequest updatedPerson = personService.updatePerson(inputPerson);

        assertTrue(updatedPerson.getId() == 101);
    }

}
