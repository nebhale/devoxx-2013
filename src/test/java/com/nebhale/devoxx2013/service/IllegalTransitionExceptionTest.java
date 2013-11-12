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
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public final class IllegalTransitionExceptionTest {

    @Test
    public void doorTransitionFromTo() {
        IllegalTransitionException exception = new IllegalTransitionException(0, 1, Door.DoorStatus.CLOSED,
                Door.DoorStatus.OPENED);
        assertThat(exception.getMessage(), equalTo("It is illegal to transition door 1 in game 0 from CLOSED to " +
                "OPENED"));
    }

    @Test
    public void doorTransitionTo() {
        IllegalTransitionException exception = new IllegalTransitionException(0, 1, Door.DoorStatus.CLOSED);
        assertThat(exception.getMessage(), equalTo("It is illegal to transition door 1 in game 0 to CLOSED"));
    }

    @Test
    public void gameTransition() {
        IllegalTransitionException exception = new IllegalTransitionException(0,
                Game.GameStatus.AWAITING_FINAL_SELECTION, Game.GameStatus.AWAITING_INITIAL_SELECTION);
        assertThat(exception.getMessage(), equalTo("It is illegal to transition game 0 from AWAITING_FINAL_SELECTION " +
                "to AWAITING_INITIAL_SELECTION"));
    }

}
