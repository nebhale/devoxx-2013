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
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GameControllerTest extends AbstractTest {

    @Autowired
    private volatile DoorRepository doorRepository;

    @Autowired
    private volatile GameRepository gameRepository;

    @Test
    public void create() throws Exception {
        ResultActions result = this.mockMvc.perform(post("/games"))
                .andExpect(status().isCreated());

        assertThat(this.gameRepository.count(), equalTo(1L));
        Game game = this.gameRepository.findAll().get(0);

        result.andExpect(header().string("Location", String.format("http://localhost/games/%d", game.getId())));
    }

    @Test
    public void read() throws Exception {
        Game game = createGame();
        List<Door> doors = createDoors(game);

        ResultActions result = this.mockMvc.perform(get("/games/{game}", game.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("AWAITING_INITIAL_SELECTION"))
                .andExpect(jsonPath("$.links[?(@.rel==self)].href[0]").value(String.format("http://localhost/games/%d",
                        game.getId())));

        for (int i = 0; i < 3; i++) {
            result.andExpect(jsonPath(String.format("$.links[?(@.rel==door)].href[%d]",
                    i)).value(String.format("http://localhost/games/%d/doors/%d", game.getId(), doors.get(i).getId())));
        }

    }

    @Test
    public void readDoesNotExist() throws Exception {
        this.mockMvc.perform(get("/games/{game}", Integer.MIN_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    public void destroy() throws Exception {
        Game game = createGame();
        createDoors(game);

        this.mockMvc.perform(delete("/games/{game}", game.getId()))
                .andExpect(status().isOk());

        assertThat(this.gameRepository.count(), equalTo(0L));
        assertThat(this.doorRepository.count(), equalTo(0L));
    }

    @Test
    public void destroyDoesNotExist() throws Exception {
        this.mockMvc.perform(delete("/games/{game}", Integer.MIN_VALUE))
                .andExpect(status().isNotFound());
    }

}
