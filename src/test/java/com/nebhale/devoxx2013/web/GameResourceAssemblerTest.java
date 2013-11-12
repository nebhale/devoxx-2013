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
import com.nebhale.devoxx2013.domain.GameRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class GameResourceAssemblerTest extends AbstractTest {

    @Autowired
    private volatile DoorRepository doorRepository;

    @Autowired
    private volatile GameRepository gameRepository;

    @Autowired
    private volatile GameResourceAssembler resourceAssembler;

    @Test
    public void toResource() {
        final Game game = createGame();
        List<Door> doors = createDoors(game);

        Resource<Game> resource = this.resourceAssembler.toResource(game);

        assertThat(resource.getContent(), is(game));
        assertThat(resource.getId(), equalTo(new Link(String.format("http://localhost/games/%d", game.getId()),
                "self")));

        pairWise(doors, getLinks("door", resource), new PairWiseFunction<Door, Link>() {

            @Override
            public void exec(Door door, Link link) {
                assertThat(link, equalTo(new Link(String.format("http://localhost/games/%d/doors/%d", game.getId(),
                        door.getId()), "door")));
            }

        });
    }

}
