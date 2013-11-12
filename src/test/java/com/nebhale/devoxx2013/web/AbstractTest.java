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

import com.nebhale.devoxx2013.ApplicationConfiguration;
import com.nebhale.devoxx2013.domain.Door;
import com.nebhale.devoxx2013.domain.DoorRepository;
import com.nebhale.devoxx2013.domain.Game;
import com.nebhale.devoxx2013.domain.GameRepository;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.initializer.ConfigFileApplicationContextInitializer;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@ContextConfiguration(classes = ApplicationConfiguration.class,
        initializers = ConfigFileApplicationContextInitializer.class)
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@WebAppConfiguration
public abstract class AbstractTest {

    protected volatile MockMvc mockMvc;

    @Autowired
    private volatile DoorRepository doorRepository;

    @Autowired
    private volatile GameRepository gameRepository;

    @Autowired
    private volatile WebApplicationContext webApplicationContext;

    @Before
    public void mockMvc() {
        this.mockMvc = webAppContextSetup(this.webApplicationContext).build();
    }

    protected final Game createGame() {
        return this.gameRepository.saveAndFlush(new Game());
    }

    protected final List<Door> createDoors(Game game) {
        List<Door> doors = new ArrayList<>(3);
        for (int i = 0; i < 3; i++) {
            doors.add(this.doorRepository.saveAndFlush(new Door(Door.DoorContent.BICYCLE, game)));
        }
        return doors;
    }

    protected final List<Link> getLinks(String rel, Resource<?> resource) {
        List<Link> links = new ArrayList<>();

        for (Link link : resource.getLinks()) {
            if(rel.equals(link.getRel())) {
                links.add(link);
            }
        }

        return links;
    }

    protected <T, U> void pairWise(List<T> first, List<U> second, PairWiseFunction<T, U> function) {
        for (int i = 0; i < first.size(); i++) {
            function.exec(first.get(i), second.get(i));
        }
    }

    protected static interface PairWiseFunction<T, U> {

        void exec(T first, U second);
    }
}
