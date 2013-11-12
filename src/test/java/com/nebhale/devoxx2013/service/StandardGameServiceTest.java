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
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public final class StandardGameServiceTest {

    private final DoorRepository doorRepository = mock(DoorRepository.class);

    private final GameRepository gameRepository = mock(GameRepository.class);

    private final StandardGameService gameService = new StandardGameService(this.gameRepository, this.doorRepository);

    @Test
    public void create() {
        ArgumentCaptor<Game> gameArgumentCaptor = ArgumentCaptor.forClass(Game.class);
        ArgumentCaptor<Door> doorArgumentCaptor = ArgumentCaptor.forClass(Door.class);

        this.gameService.create();

        verify(this.gameRepository).saveAndFlush(gameArgumentCaptor.capture());
        verify(this.doorRepository, times(3)).saveAndFlush(doorArgumentCaptor.capture());

        assertThat(gameArgumentCaptor.getValue().getStatus(), equalTo(Game.GameStatus.AWAITING_INITIAL_SELECTION));

        int winnerCount = 0;
        for (Door door : doorArgumentCaptor.getAllValues()) {
            if (Door.DoorContent.BICYCLE == door.getContent()) {
                winnerCount++;
            }
        }

        assertThat(winnerCount, equalTo(1));
    }

    @Test
    public void delete() {
        Game game = new Game();
        Door door = new Door(Door.DoorContent.SMALL_FURRY_ANIMAL, game);
        when(this.doorRepository.findAllByGame(game)).thenReturn(Arrays.asList(door, door, door));

        this.gameService.delete(game);

        verify(this.gameRepository).delete(game);
        verify(this.doorRepository).deleteInBatch(Arrays.asList(door, door, door));
    }

    @Test(expected = IllegalTransitionException.class)
    public void openIllegalGameTransition() throws IllegalTransitionException {
        Game game = new Game();
        Door door = new Door(Door.DoorContent.SMALL_FURRY_ANIMAL, game);

        this.gameService.open(door);
    }

    @Test(expected = IllegalTransitionException.class)
    public void openIllegalDoorTransition() throws IllegalTransitionException {
        Game game = new Game(Game.GameStatus.AWAITING_FINAL_SELECTION);
        Door door = new Door(Door.DoorContent.SMALL_FURRY_ANIMAL, game);
        door.setStatus(Door.DoorStatus.OPENED);

        this.gameService.open(door);
    }

    @Test
    public void openWin() throws IllegalTransitionException {
        Game game = new Game(Game.GameStatus.AWAITING_FINAL_SELECTION);
        Door door = new Door(Door.DoorContent.BICYCLE, game);

        this.gameService.open(door);

        assertThat(game.getStatus(), equalTo(Game.GameStatus.WON));
    }

    @Test
    public void openLost() throws IllegalTransitionException {
        Game game = new Game(Game.GameStatus.AWAITING_FINAL_SELECTION);
        Door door = new Door(Door.DoorContent.SMALL_FURRY_ANIMAL, game);

        this.gameService.open(door);

        assertThat(game.getStatus(), equalTo(Game.GameStatus.LOST));
    }

    @Test(expected = IllegalTransitionException.class)
    public void selectIllegalGameTransition() throws IllegalTransitionException {
        Game game = new Game(Game.GameStatus.AWAITING_FINAL_SELECTION);
        Door door = new Door(Door.DoorContent.BICYCLE, game);

        this.gameService.select(door);
    }

    @Test
    public void select() throws IllegalTransitionException {
        Game game = new Game();
        Door door1 = new Door(Door.DoorContent.SMALL_FURRY_ANIMAL, game);
        Door door2 = new Door(Door.DoorContent.BICYCLE, game);
        Door door3 = new Door(Door.DoorContent.SMALL_FURRY_ANIMAL, game);
        when(this.doorRepository.findAllByGame(game)).thenReturn(Arrays.asList(door1, door2, door3));

        this.gameService.select(door1);

        assertThat(door1.getStatus(), equalTo(Door.DoorStatus.SELECTED));
        assertThat(door2.getStatus(), equalTo(Door.DoorStatus.CLOSED));
        assertThat(door3.getStatus(), equalTo(Door.DoorStatus.OPENED));
    }

}
