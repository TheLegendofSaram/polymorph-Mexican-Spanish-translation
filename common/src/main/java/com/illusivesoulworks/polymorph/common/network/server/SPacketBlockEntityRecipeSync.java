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
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SPacketBlockEntityRecipeSync(BlockPos blockPos, ResourceLocation selected)
    implements CustomPacketPayload {

  public static final ResourceLocation ID =
      new ResourceLocation(PolymorphApi.MOD_ID, "block_entity_recipe_sync");

  public SPacketBlockEntityRecipeSync(FriendlyByteBuf buf) {
    this(buf.readBlockPos(), buf.readResourceLocation());
  }

  public static void handle(SPacketBlockEntityRecipeSync packet) {
    ClientPacketHandler.handle(packet);
  }

  @Override
  public void write(@Nonnull FriendlyByteBuf buf) {
    buf.writeBlockPos(blockPos());
    buf.writeResourceLocation(selected());
  }

  @Nonnull
  @Override
  public ResourceLocation id() {
    return ID;
  }
}
