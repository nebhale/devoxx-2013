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

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.Identifiable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public final class Game implements Identifiable<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private volatile Integer id;

    private volatile GameStatus status;

    public Game() {
        this(GameStatus.AWAITING_INITIAL_SELECTION);
    }

    public Game(GameStatus status) {
        this.status = status;
    }

    @JsonIgnore
    public Integer getId() {
        return this.id;
    }

    public GameStatus getStatus() {
        return this.status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
    }

    public enum GameStatus {
        AWAITING_INITIAL_SELECTION,
        AWAITING_FINAL_SELECTION,
        LOST,
        WON
    }

}
