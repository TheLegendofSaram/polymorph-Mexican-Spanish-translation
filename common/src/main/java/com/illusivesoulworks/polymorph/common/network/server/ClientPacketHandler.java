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

package com.illusivesoulworks.polymorph.common.network.server;

import com.illusivesoulworks.polymorph.api.PolymorphApi;
import com.illusivesoulworks.polymorph.api.client.base.IRecipesWidget;
import com.illusivesoulworks.polymorph.client.recipe.RecipesWidget;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;

public class ClientPacketHandler {

  public static void handle(SPacketPlayerRecipeSync packet) {
    LocalPlayer clientPlayerEntity = Minecraft.getInstance().player;

    if (clientPlayerEntity != null) {
      PolymorphApi.common().getRecipeData(clientPlayerEntity).ifPresent(recipeData -> {
        recipeData.setRecipesList(packet.recipeList().orElse(new TreeSet<>()));
        packet.selected().flatMap(resourceLocation -> clientPlayerEntity.level().getRecipeManager()
            .byKey(resourceLocation)).ifPresent(recipeData::setSelectedRecipe);
      });
    }
  }

  public static void handle(SPacketRecipesList packet) {
    LocalPlayer clientPlayerEntity = Minecraft.getInstance().player;

    if (clientPlayerEntity != null) {
      Optional<IRecipesWidget> maybeWidget = RecipesWidget.get();
      maybeWidget.ifPresent(
          widget -> widget.setRecipesList(packet.recipeList().orElse(new TreeSet<>()),
              packet.selected().orElse(null)));

      if (maybeWidget.isEmpty()) {
        RecipesWidget.enqueueRecipesList(packet.recipeList().orElse(new TreeSet<>()),
            packet.selected().orElse(null));
      }
    }
  }

  public static void handle(SPacketHighlightRecipe packet) {
    LocalPlayer clientPlayerEntity = Minecraft.getInstance().player;

    if (clientPlayerEntity != null) {
      RecipesWidget.get().ifPresent(widget -> widget.highlightRecipe(packet.recipe()));
    }
  }
}
