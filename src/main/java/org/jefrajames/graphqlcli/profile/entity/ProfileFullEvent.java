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
package org.jefrajames.graphqlcli.profile.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.math.BigDecimal;
import java.util.Date;
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
public class ProfileFullEvent {
    String action;
    BigDecimal value;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss") // Jackson format for Nodes
    @JsonbDateFormat("yyyy-MM-dd'T'HH:mm:ss") // JSON-B format for Quarkus
    Date dateTime;
}