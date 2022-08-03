/*
 * Copyright (C) 2020-2022 Illusive Soulworks
 *
 * Polymorph is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * Polymorph is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Polymorph.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.illusivesoulworks.polymorph.api;

import com.illusivesoulworks.polymorph.PolymorphConstants;
import com.illusivesoulworks.polymorph.api.client.base.IPolymorphClient;
import com.illusivesoulworks.polymorph.api.common.base.IPolymorphCommon;

public final class PolymorphApi {

  public static final String MOD_ID = PolymorphConstants.MOD_ID;

  public static IPolymorphCommon common() {
    throw new IllegalStateException("Polymorph Common API missing!");
  }

  public static IPolymorphClient client() {
    throw new IllegalStateException("Polymorph Client API missing!");
  }
}
