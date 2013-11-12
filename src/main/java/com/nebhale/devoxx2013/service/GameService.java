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

public interface GameService {

    Game create();

    void delete(Game game);

    void open(Door door) throws IllegalTransitionException;

    void select(Door door) throws IllegalTransitionException;

}
