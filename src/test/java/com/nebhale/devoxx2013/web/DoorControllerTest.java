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
import com.nebhale.devoxx2013.domain.Game;
import com.nebhale.devoxx2013.service.GameService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DoorControllerTest extends AbstractTest {

    @Autowired
    private volatile GameService gameService;

    @Test
    public void read() throws Exception {
        Game game = createGame();
        Door door = createDoors(game).get(0);

        this.mockMvc.perform(get("/games/{game}/doors/{door}", game.getId(), door.getId()))
                .andExpect(status().isOk())
                // TODO: 10
                .andExpect(jsonPath("$.status").value("CLOSED"))
                .andExpect(jsonPath("$.links[?(@.rel==self)].href[0]").value(String.format
                        ("http://localhost/games/%d/doors/%d", game.getId(), door.getId())));
    }

    @Test
    public void readDoesNotExist() throws Exception {
        this.mockMvc.perform(get("/games/{game}/door/{door}", Integer.MIN_VALUE, Integer.MIN_VALUE + 1))
                .andExpect(status().isNotFound());
    }

    @Test
    public void modifyDoorSelected() throws Exception {
        Game game = createGame();
        Door door = createDoors(game).get(0);

        this.mockMvc.perform(put("/games/{game}/doors/{door}", game.getId(), door.getId())
                .content("{ \"status\" : \"SELECTED\" }").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertThat(door.getStatus(), equalTo(Door.DoorStatus.SELECTED));
    }

    @Test
    public void modifyDoorOpened() throws Exception {
        Game game = createGame();
        List<Door> doors = createDoors(game);
        Door door0 = doors.get(0);
        Door door1 = doors.get(1);

        this.gameService.select(door0);

        this.mockMvc.perform(put("/games/{game}/doors/{door}", game.getId(), door1.getId())
                .content("{ \"status\" : \"OPENED\" }").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        assertThat(door1.getStatus(), equalTo(Door.DoorStatus.OPENED));
    }

    @Test
    public void modifyDoorClosed() throws Exception {
        Game game = createGame();
        Door door = createDoors(game).get(0);

        this.mockMvc.perform(put("/games/{game}/doors/{door}", game.getId(), door.getId())
                .content("{ \"status\" : \"CLOSED\" }").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void modifyDoorDoesNotExist() throws Exception {
        this.mockMvc.perform(put("/games/{game}/doors/{door}", Integer.MIN_VALUE, Integer.MIN_VALUE + 1)
                .content("{ \"status\" : \"SELECTED\" }").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void modifyDoorMissingKey() throws Exception {
        Game game = createGame();
        Door door = createDoors(game).get(0);

        this.mockMvc.perform(put("/games/{game}/doors/{door}", game.getId(), door.getId())
                .content("{}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void modifyDoorIllegalStatus() throws Exception {
        Game game = createGame();
        Door door = createDoors(game).get(0);

        this.mockMvc.perform(put("/games/{game}/doors/{door}", game.getId(), door.getId())
                .content("{ \"status\" : \"ALPHA\" }").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
