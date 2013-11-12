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

package com.nebhale.devoxx2013.domain;

import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public final class GameTest {

    private final Game game = new Game();

    @Test
    public void getId() {
        assertThat(this.game.getId(), nullValue());
    }

    @Test
    public void getStatus() {
        assertThat(this.game.getStatus(), equalTo(Game.GameStatus.AWAITING_INITIAL_SELECTION));
        this.game.setStatus(Game.GameStatus.AWAITING_FINAL_SELECTION);
        assertThat(this.game.getStatus(), equalTo(Game.GameStatus.AWAITING_FINAL_SELECTION));
    }

}
