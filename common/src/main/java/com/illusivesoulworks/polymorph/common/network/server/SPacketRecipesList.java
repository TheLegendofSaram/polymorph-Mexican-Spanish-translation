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
import com.illusivesoulworks.polymorph.api.common.base.IRecipePair;
import com.illusivesoulworks.polymorph.common.impl.RecipePair;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public class SPacketRecipesList implements CustomPacketPayload {

  public static final ResourceLocation ID =
      new ResourceLocation(PolymorphApi.MOD_ID, "recipes_list");

  public final SortedSet<IRecipePair> recipeList;
  public final ResourceLocation selected;

  public SPacketRecipesList(FriendlyByteBuf buf) {
    SortedSet<IRecipePair> recipeDataset = new TreeSet<>();
    ResourceLocation selected = null;

    if (buf.isReadable()) {
      int size = buf.readInt();

      for (int i = 0; i < size; i++) {
        recipeDataset.add(new RecipePair(buf.readResourceLocation(), buf.readItem()));
      }

      if (buf.isReadable()) {
        selected = buf.readResourceLocation();
      }
    }
    this.recipeList = recipeDataset;
    this.selected = selected;
  }

  public SPacketRecipesList(SortedSet<IRecipePair> recipeList, ResourceLocation selected) {
    this.recipeList = new TreeSet<>();

    if (recipeList != null) {
      this.recipeList.addAll(recipeList);
    }
    this.selected = selected;
  }

  public static void handle(SPacketRecipesList packet) {
    ClientPacketHandler.handle(packet);
  }

  @Override
  public void write(@Nonnull FriendlyByteBuf buf) {

    if (!this.recipeList.isEmpty()) {
      buf.writeInt(this.recipeList.size());

      for (IRecipePair data : this.recipeList) {
        buf.writeResourceLocation(data.getResourceLocation());
        buf.writeItem(data.getOutput());
      }

      if (this.selected != null) {
        buf.writeResourceLocation(this.selected);
      }
    }
  }

  @Nonnull
  @Override
  public ResourceLocation id() {
    return ID;
  }
}
