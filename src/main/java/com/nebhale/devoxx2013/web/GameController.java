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

import com.nebhale.devoxx2013.domain.Game;
import com.nebhale.devoxx2013.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@RequestMapping("/games")
@RestController
final class GameController {

    private final GameService gameService;

    private final ResourceAssembler<Game, Resource<Game>> resourceAssembler;

    @Autowired
    GameController(GameService gameService, ResourceAssembler<Game, Resource<Game>> resourceAssembler) {
        this.gameService = gameService;
        this.resourceAssembler = resourceAssembler;
    }

    @RequestMapping(method = RequestMethod.POST, value = "")
    @Transactional
    ResponseEntity<Void> create() {
        Game game = this.gameService.create();

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(linkTo(GameController.class).slash(game).toUri());

        return new ResponseEntity<>(headers, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{game}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Transactional(readOnly = true)
    Resource<Game> read(@PathVariable Game game) {
        Assert.notNull(game);
        return this.resourceAssembler.toResource(game);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{game}")
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    void delete(@PathVariable Game game) {
        Assert.notNull(game);
        this.gameService.delete(game);
    }

}
