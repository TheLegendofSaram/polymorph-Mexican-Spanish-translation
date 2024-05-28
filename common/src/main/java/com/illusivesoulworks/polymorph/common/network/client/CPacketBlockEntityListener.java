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
import com.illusivesoulworks.polymorph.common.util.BlockEntityTicker;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;

public record CPacketBlockEntityListener(boolean add) implements CustomPacketPayload {

  public static final ResourceLocation ID =
      new ResourceLocation(PolymorphApi.MOD_ID, "block_entity_listener");

  public CPacketBlockEntityListener(FriendlyByteBuf buf) {
    this(buf.readBoolean());
  }

  public static void handle(CPacketBlockEntityListener packet, ServerPlayer player) {

    if (player != null) {

      if (packet.add) {
        AbstractContainerMenu container = player.containerMenu;
        PolymorphApi.common().getRecipeDataFromBlockEntity(container)
            .ifPresent(recipeData -> BlockEntityTicker.add(player, recipeData));
      } else {
        BlockEntityTicker.remove(player);
      }
    }
  }

  @Override
  public void write(@Nonnull FriendlyByteBuf buf) {
    buf.writeBoolean(add());
  }

  @Nonnull
  @Override
  public ResourceLocation id() {
    return ID;
  }
}
