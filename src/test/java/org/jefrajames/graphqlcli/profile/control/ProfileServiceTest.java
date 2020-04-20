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

import org.jefrajames.graphqlcli.profile.entity.FindProfileFullByPersonIdRequest;
import org.jefrajames.graphqlcli.profile.entity.FindProfileByPersonIdRequest;
import io.quarkus.test.junit.QuarkusTest;
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
public class ProfileServiceTest {
    
    @Inject
    ProfileService profileService;

    @Test
    public void testFindProfile() {
        FindProfileByPersonIdRequest profile = profileService.findProfileById(50);
        assertTrue(profile.getPerson().getId() == 50);
    }

    @Test
    public void testFindProfileFull() {
        FindProfileFullByPersonIdRequest profile = profileService.findProfileFullById(25);
        assertTrue(profile.getPerson().getId() == 25);
        assertTrue(!profile.getScores().isEmpty());
    }

}
