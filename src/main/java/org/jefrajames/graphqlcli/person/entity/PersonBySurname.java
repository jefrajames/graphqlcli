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
package org.jefrajames.graphqlcli.person.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.time.LocalDate;
import javax.json.bind.annotation.JsonbDateFormat;
import lombok.Data;
import lombok.ToString;

/**
 *
 * @author jefrajames
 */
@Data
@ToString
@RegisterForReflection
public class PersonBySurname {

    int id;
    
    String[] names;
    String gender;

    @JsonFormat(pattern = "dd/MM/yyyy") // Jackson annotation for Nodes
    @JsonbDateFormat("yyyy-MM-dd") // This is for JSON-B       
    LocalDate birthDate;

}
