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

import com.jayway.jsonpath.JsonPath;
import com.nebhale.devoxx2013.domain.Door;
import com.nebhale.devoxx2013.domain.Game;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.util.List;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class IntegrationTest extends AbstractTest {

    @Test
    public void play() throws Exception {
        String game = this.mockMvc.perform(post("/games"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getHeader("Location");

        List<String> doors = getDoors(game);
        this.mockMvc.perform(put(doors.get(1))
                .content("{ \"status\": \"SELECTED\"}").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        if (Door.DoorStatus.CLOSED == getDoorStatus(doors.get(0))) {
            this.mockMvc.perform(put(doors.get(0))
                    .content("{ \"status\": \"OPENED\"}").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        } else {
            System.out.println("OTHER");
            this.mockMvc.perform(put(doors.get(2))
                    .content("{ \"status\": \"OPENED\"}").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());

        }

        assertThat(getGameStatus(game), anyOf(equalTo(Game.GameStatus.WON), equalTo(Game.GameStatus.LOST)));
    }

    private List<String> getDoors(String game) throws Exception {
        String content = this.mockMvc.perform(get(game))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return JsonPath.read(content, "$.links[?(@.rel==door)].href");
    }

    private Door.DoorStatus getDoorStatus(String door) throws Exception {
        return Door.DoorStatus.valueOf(getStatus(door));
    }

    private Game.GameStatus getGameStatus(String game) throws Exception {
        return Game.GameStatus.valueOf(getStatus(game));
    }

    private String getStatus(String location) throws Exception {
        String content = this.mockMvc.perform(get(location))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return JsonPath.read(content, "$.status");
    }
}
