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
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public final class DoorTest {

    private final Game game = new Game();

    private final Door door = new Door(Door.DoorContent.BICYCLE, this.game);

    @Test
    public void getId() {
        assertThat(this.door.getId(), nullValue());
    }

    @Test
    public void getContent() {
        assertThat(this.door.getContent(), equalTo(Door.DoorContent.BICYCLE));
    }

    @Test
    public void getContentObfuscated() {
        assertThat(this.door.getContentObfuscated(), equalTo(Door.DoorContent.UNKNOWN));
        this.door.setStatus(Door.DoorStatus.SELECTED);
        assertThat(this.door.getContentObfuscated(), equalTo(Door.DoorContent.UNKNOWN));
        this.door.setStatus(Door.DoorStatus.OPENED);
        assertThat(this.door.getContentObfuscated(), equalTo(Door.DoorContent.BICYCLE));
    }

    @Test
    public void getGame() {
        assertThat(this.door.getGame(), is(this.game));
    }

    @Test
    public void getStatus() {
        assertThat(this.door.getStatus(), equalTo(Door.DoorStatus.CLOSED));
        this.door.setStatus(Door.DoorStatus.OPENED);
        assertThat(this.door.getStatus(), equalTo(Door.DoorStatus.OPENED));
    }

}
