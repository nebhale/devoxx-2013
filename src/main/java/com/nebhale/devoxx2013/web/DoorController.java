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
import com.nebhale.devoxx2013.service.GameService;
import com.nebhale.devoxx2013.service.IllegalTransitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/games/{game}/doors")
@RestController
final class DoorController {

    private final GameService gameService;

    private final DoorResourceAssembler resourceAssembler;

    @Autowired
    DoorController(GameService gameService, DoorResourceAssembler resourceAssembler) {
        this.gameService = gameService;
        this.resourceAssembler = resourceAssembler;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{door}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    Resource<Door> read(@PathVariable Door door) {
        Assert.notNull(door);
        return this.resourceAssembler.toResource(door);
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/{door}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    void update(@PathVariable Door door, @RequestBody Door transition) throws IllegalTransitionException {
        Assert.notNull(door);
        Assert.notNull(transition);

        Door.DoorStatus targetStatus = transition.getStatus();
        if (Door.DoorStatus.CLOSED == targetStatus) {
            throw new IllegalTransitionException(door.getGame().getId(), door.getId(), targetStatus);
        } else if (Door.DoorStatus.OPENED == targetStatus) {
            this.gameService.open(door);
        } else if (Door.DoorStatus.SELECTED == targetStatus) {
            this.gameService.select(door);
        } else {
            throw new IllegalStateException();
        }
    }
}
