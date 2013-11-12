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
import com.nebhale.devoxx2013.domain.Game;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public final class IllegalTransitionException extends Exception {

    private static final long serialVersionUID = -7234293084303395854L;

    private static final String DOOR_TRANSITION_FROM_TO_MESSAGE_FORMAT = "It is illegal to transition door %d in game %d from %s to %s";

    private static final String DOOR_TRANSITION_TO_MESSAGE_FORMAT = "It is illegal to transition door %d in game %d to %s";

    private static final String GAME_TRANSITION_MESSAGE_FORMAT = "It is illegal to transition game %d from %s to %s";

    public IllegalTransitionException(Integer gameId, Integer doorId, Door.DoorStatus from, Door.DoorStatus to) {
        super(String.format(DOOR_TRANSITION_FROM_TO_MESSAGE_FORMAT, doorId, gameId, from, to));
    }

    public IllegalTransitionException(Integer gameId, Integer doorId, Door.DoorStatus to) {
        super(String.format(DOOR_TRANSITION_TO_MESSAGE_FORMAT, doorId, gameId, to));
    }

    public IllegalTransitionException(Integer gameId, Game.GameStatus from, Game.GameStatus to) {
        super(String.format(GAME_TRANSITION_MESSAGE_FORMAT, gameId, from, to));
    }
}
