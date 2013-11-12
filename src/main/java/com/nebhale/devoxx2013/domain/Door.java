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
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.Identifiable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public final class Door implements Identifiable<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private volatile Integer id;

    private volatile DoorContent content;

    @ManyToOne
    private volatile Game game;

    private volatile DoorStatus status;

    Door() {
    }

    public Door(DoorContent content, Game game) {
        this.content = content;
        this.game = game;
        this.status = DoorStatus.CLOSED;
    }

    @JsonIgnore
    public Integer getId() {
        return this.id;
    }

    @JsonIgnore
    public DoorContent getContent() {
        return this.content;
    }

    @JsonProperty("content")
    public DoorContent getContentObfuscated() {
        if (this.status == DoorStatus.OPENED) {
            return this.content;
        }
        return DoorContent.UNKNOWN;
    }

    @JsonIgnore
    public Game getGame() {
        return this.game;
    }

    public DoorStatus getStatus() {
        return this.status;
    }

    public void setStatus(DoorStatus status) {
        this.status = status;
    }

    public enum DoorContent {
        BICYCLE,
        SMALL_FURRY_ANIMAL,
        UNKNOWN
    }

    public enum DoorStatus {
        CLOSED,
        OPENED,
        SELECTED
    }

}
