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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@Component
public final class GameResourceAssembler implements ResourceAssembler<Game, Resource<Game>> {

    private final DoorRepository doorRepository;

    @Autowired
    GameResourceAssembler(DoorRepository doorRepository) {
        this.doorRepository = doorRepository;
    }

    @Override
    public Resource<Game> toResource(Game game) {
        Resource<Game> resource = new Resource<>(game);
        resource.add(linkTo(GameController.class).slash(game).withSelfRel());

        for (Door door : this.doorRepository.findAllByGame(game)) {
            // TODO: 9
        }

        return resource;
    }
}
