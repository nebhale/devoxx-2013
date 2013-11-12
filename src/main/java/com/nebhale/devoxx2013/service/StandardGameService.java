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

package com.nebhale.devoxx2013.service;

import com.nebhale.devoxx2013.domain.Door;
import com.nebhale.devoxx2013.domain.DoorRepository;
import com.nebhale.devoxx2013.domain.Game;
import com.nebhale.devoxx2013.domain.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Random;

@Service
final class StandardGameService implements GameService {

    private static final Random WINNERS = new SecureRandom();

    private final GameRepository gameRepository;

    private final DoorRepository doorRepository;

    @Autowired
    StandardGameService(GameRepository gameRepository, DoorRepository doorRepository) {
        this.gameRepository = gameRepository;
        this.doorRepository = doorRepository;
    }

    @Override
    @Transactional
    public Game create() {
        Game game = this.gameRepository.saveAndFlush(new Game());

        int winner = WINNERS.nextInt(3);
        for (int i = 0; i < 3; i++) {
            Door.DoorContent doorContent = i == winner ? Door.DoorContent.BICYCLE : Door.DoorContent
                    .SMALL_FURRY_ANIMAL;
            this.doorRepository.saveAndFlush(new Door(doorContent, game));
        }

        return game;
    }

    @Override
    @Transactional
    public void delete(Game game) {
        this.doorRepository.deleteInBatch(this.doorRepository.findAllByGame(game));
        this.gameRepository.delete(game);
    }

    @Override
    @Transactional
    public void open(Door door) throws IllegalTransitionException {
        Game game = door.getGame();

        if (Game.GameStatus.AWAITING_FINAL_SELECTION != game.getStatus()) {
            throw new IllegalTransitionException(game.getId(), game.getStatus(), Game.GameStatus.WON);
        }

        if (Door.DoorStatus.OPENED == door.getStatus()) {
            throw new IllegalTransitionException(game.getId(), door.getId(), door.getStatus(), Door.DoorStatus.OPENED);
        }

        door.setStatus(Door.DoorStatus.OPENED);

        if (Door.DoorContent.BICYCLE == door.getContent()) {
            game.setStatus(Game.GameStatus.WON);
        } else {
            game.setStatus(Game.GameStatus.LOST);
        }
    }

    @Override
    @Transactional
    public void select(Door door) throws IllegalTransitionException {
        Game game = door.getGame();

        if (Game.GameStatus.AWAITING_INITIAL_SELECTION != game.getStatus()) {
            throw new IllegalTransitionException(game.getId(), game.getStatus(),
                    Game.GameStatus.AWAITING_FINAL_SELECTION);
        }

        door.setStatus(Door.DoorStatus.SELECTED);
        openHintDoor(game);

        game.setStatus(Game.GameStatus.AWAITING_FINAL_SELECTION);
    }

    private void openHintDoor(Game game) {
        for (Door door : this.doorRepository.findAllByGame(game)) {
            if (Door.DoorStatus.CLOSED == door.getStatus()
                    && Door.DoorContent.SMALL_FURRY_ANIMAL == door.getContent()) {
                door.setStatus(Door.DoorStatus.OPENED);
                break;
            }
        }
    }

}
