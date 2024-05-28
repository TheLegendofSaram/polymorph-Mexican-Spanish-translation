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

package com.illusivesoulworks.polymorph.common;

import com.illusivesoulworks.polymorph.api.PolymorphApi;
import com.illusivesoulworks.polymorph.api.common.base.IPolymorphPacketDistributor;
import com.illusivesoulworks.polymorph.api.common.base.IRecipePair;
import com.illusivesoulworks.polymorph.common.network.client.CPacketBlockEntityListener;
import com.illusivesoulworks.polymorph.common.network.client.CPacketPersistentRecipeSelection;
import com.illusivesoulworks.polymorph.common.network.client.CPacketPlayerRecipeSelection;
import com.illusivesoulworks.polymorph.common.network.client.CPacketStackRecipeSelection;
import com.illusivesoulworks.polymorph.common.network.server.SPacketBlockEntityRecipeSync;
import com.illusivesoulworks.polymorph.common.network.server.SPacketHighlightRecipe;
import com.illusivesoulworks.polymorph.common.network.server.SPacketPlayerRecipeSync;
import com.illusivesoulworks.polymorph.common.network.server.SPacketRecipesList;
import java.util.SortedSet;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class PolymorphFabricPacketDistributor implements IPolymorphPacketDistributor {

  @Override
  public void sendPlayerRecipeSelectionC2S(ResourceLocation resourceLocation) {
    FriendlyByteBuf buf = PacketByteBufs.create();
    new CPacketPlayerRecipeSelection(resourceLocation).write(buf);
    ClientPlayNetworking.send(CPacketPlayerRecipeSelection.ID, buf);
  }

  @Override
  public void sendPersistentRecipeSelectionC2S(ResourceLocation resourceLocation) {
    FriendlyByteBuf buf = PacketByteBufs.create();
    new CPacketPersistentRecipeSelection(resourceLocation).write(buf);
    ClientPlayNetworking.send(CPacketPersistentRecipeSelection.ID, buf);
  }

  @Override
  public void sendStackRecipeSelectionC2S(ResourceLocation resourceLocation) {
    FriendlyByteBuf buf = PacketByteBufs.create();
    new CPacketStackRecipeSelection(resourceLocation).write(buf);
    ClientPlayNetworking.send(CPacketStackRecipeSelection.ID, buf);
  }

  @Override
  public void sendRecipesListS2C(ServerPlayer player) {
    sendRecipesListS2C(player, null);
  }

  @Override
  public void sendRecipesListS2C(ServerPlayer player, SortedSet<IRecipePair> recipesList) {
    sendRecipesListS2C(player, recipesList, null);
  }

  @Override
  public void sendRecipesListS2C(ServerPlayer player, SortedSet<IRecipePair> recipesList,
                                 ResourceLocation selected) {
    FriendlyByteBuf buf = PacketByteBufs.create();
    new SPacketRecipesList(recipesList, selected).write(buf);
    ServerPlayNetworking.send(player, SPacketRecipesList.ID, buf);
  }

  @Override
  public void sendHighlightRecipeS2C(ServerPlayer player, ResourceLocation resourceLocation) {
    FriendlyByteBuf buf = PacketByteBufs.create();
    new SPacketHighlightRecipe(resourceLocation).write(buf);
    ServerPlayNetworking.send(player, SPacketHighlightRecipe.ID, buf);
  }

  @Override
  public void sendPlayerSyncS2C(ServerPlayer player, SortedSet<IRecipePair> recipesList,
                                ResourceLocation selected) {
    FriendlyByteBuf buf = PacketByteBufs.create();
    new SPacketPlayerRecipeSync(recipesList, selected).write(buf);
    ServerPlayNetworking.send(player, SPacketPlayerRecipeSync.ID, buf);
  }

  @Override
  public void sendBlockEntitySyncS2C(BlockPos blockPos, ResourceLocation selected) {
    FriendlyByteBuf buf = PacketByteBufs.create();
    new SPacketBlockEntityRecipeSync(blockPos, selected).write(buf);
    PolymorphApi.common().getServer().ifPresent(server -> PlayerLookup.all(server).forEach(player -> ServerPlayNetworking.send(player, SPacketBlockEntityRecipeSync.ID, buf)));
  }

  @Override
  public void sendBlockEntityListenerC2S(boolean add) {
    FriendlyByteBuf buf = PacketByteBufs.create();
    new CPacketBlockEntityListener(add).write(buf);
    ClientPlayNetworking.send(CPacketBlockEntityListener.ID, buf);
  }
}
