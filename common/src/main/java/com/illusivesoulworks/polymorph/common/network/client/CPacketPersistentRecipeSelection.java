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

package com.illusivesoulworks.polymorph.common.network.client;

import com.illusivesoulworks.polymorph.api.PolymorphApi;
import com.illusivesoulworks.polymorph.common.integration.PolymorphIntegrations;
import java.util.Optional;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

public record CPacketPersistentRecipeSelection(ResourceLocation recipe) implements
    CustomPacketPayload {

  public static final ResourceLocation ID =
      new ResourceLocation(PolymorphApi.MOD_ID, "persistent_recipe_selection");

  public CPacketPersistentRecipeSelection(final FriendlyByteBuf buf) {
    this(buf.readResourceLocation());
  }

  public static void handle(CPacketPersistentRecipeSelection packet, ServerPlayer player) {
    Level world = player.getCommandSenderWorld();
    Optional<RecipeHolder<?>> maybeRecipe =
        world.getRecipeManager().byKey(packet.recipe);
    maybeRecipe.ifPresent(recipe -> {
      AbstractContainerMenu container = player.containerMenu;
      PolymorphApi.common().getRecipeDataFromBlockEntity(container)
          .ifPresent(recipeData -> {
            recipeData.selectRecipe(recipe);
            PolymorphIntegrations.selectRecipe(recipeData.getOwner(), container, recipe);
          });
    });
  }

  @Override
  public void write(@Nonnull FriendlyByteBuf buf) {
    buf.writeResourceLocation(recipe());
  }

  @Nonnull
  @Override
  public ResourceLocation id() {
    return ID;
  }
}
