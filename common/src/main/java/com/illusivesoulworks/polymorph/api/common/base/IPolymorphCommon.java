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

package com.illusivesoulworks.polymorph.api.common.base;

import com.illusivesoulworks.polymorph.api.common.capability.IBlockEntityRecipeData;
import com.illusivesoulworks.polymorph.api.common.capability.IPlayerRecipeData;
import java.util.Optional;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IPolymorphCommon {

  Optional<? extends IBlockEntityRecipeData> tryCreateRecipeData(BlockEntity blockEntity);

  Optional<? extends IBlockEntityRecipeData> getRecipeData(BlockEntity blockEntity);

  Optional<? extends IBlockEntityRecipeData> getRecipeDataFromBlockEntity(
      AbstractContainerMenu containerMenu);

  Optional<? extends IPlayerRecipeData> getRecipeData(Player player);

  void registerBlockEntity2RecipeData(IBlockEntity2RecipeData blockEntity2RecipeData);

  void registerContainer2BlockEntity(IContainer2BlockEntity container2BlockEntity);

  IPolymorphPacketDistributor getPacketDistributor();

  void setServer(MinecraftServer pServer);

  Optional<MinecraftServer> getServer();

  interface IBlockEntity2RecipeData {

    IBlockEntityRecipeData createRecipeData(BlockEntity blockEntity);
  }

  interface IContainer2BlockEntity {

    BlockEntity getBlockEntity(AbstractContainerMenu containerMenu);
  }
}
