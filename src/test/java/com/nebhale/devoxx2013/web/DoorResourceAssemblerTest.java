/*
 * Copyright 2013 the original author or authors.
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

package com.nebhale.devoxx2013.web;

import com.nebhale.devoxx2013.domain.Door;
import com.nebhale.devoxx2013.domain.DoorRepository;
import com.nebhale.devoxx2013.domain.Game;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class DoorResourceAssemblerTest extends AbstractTest {

    @Autowired
    private volatile DoorRepository doorRepository;

    @Autowired
    private volatile DoorResourceAssembler resourceAssembler;

    @Test
    public void toResource() {
        Game game = createGame();
        Door door = this.doorRepository.saveAndFlush(new Door(Door.DoorContent.BICYCLE, game));

        Resource<Door> resource = this.resourceAssembler.toResource(door);

        assertThat(resource.getContent(), is(door));
        assertThat(resource.getId(), equalTo(new Link(String.format("http://localhost/games/%d/doors/%d",
                game.getId(), door.getId()), "self")));
    }
}
