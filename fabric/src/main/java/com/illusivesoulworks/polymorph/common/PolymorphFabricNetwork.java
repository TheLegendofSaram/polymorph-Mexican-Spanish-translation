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

import com.illusivesoulworks.polymorph.common.network.client.CPacketBlockEntityListener;
import com.illusivesoulworks.polymorph.common.network.client.CPacketPersistentRecipeSelection;
import com.illusivesoulworks.polymorph.common.network.client.CPacketPlayerRecipeSelection;
import com.illusivesoulworks.polymorph.common.network.client.CPacketStackRecipeSelection;
import com.illusivesoulworks.polymorph.common.network.server.SPacketBlockEntityRecipeSync;
import com.illusivesoulworks.polymorph.common.network.server.SPacketHighlightRecipe;
import com.illusivesoulworks.polymorph.common.network.server.SPacketPlayerRecipeSync;
import com.illusivesoulworks.polymorph.common.network.server.SPacketRecipesList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class PolymorphFabricNetwork {

  public static void setup() {
    registerServerReceiver(CPacketPlayerRecipeSelection.ID, CPacketPlayerRecipeSelection::new,
        CPacketPlayerRecipeSelection::handle);
    registerServerReceiver(CPacketPersistentRecipeSelection.ID,
        CPacketPersistentRecipeSelection::new, CPacketPersistentRecipeSelection::handle);
    registerServerReceiver(CPacketStackRecipeSelection.ID, CPacketStackRecipeSelection::new,
        CPacketStackRecipeSelection::handle);
    registerServerReceiver(CPacketBlockEntityListener.ID, CPacketBlockEntityListener::new,
        CPacketBlockEntityListener::handle);
  }

  public static void clientSetup() {
    registerClientReceiver(SPacketHighlightRecipe.ID, SPacketHighlightRecipe::new,
        SPacketHighlightRecipe::handle);
    registerClientReceiver(SPacketPlayerRecipeSync.ID, SPacketPlayerRecipeSync::new,
        SPacketPlayerRecipeSync::handle);
    registerClientReceiver(SPacketRecipesList.ID, SPacketRecipesList::new,
        SPacketRecipesList::handle);
    registerClientReceiver(SPacketBlockEntityRecipeSync.ID, SPacketBlockEntityRecipeSync::new,
        SPacketBlockEntityRecipeSync::handle);
  }

  private static <M> void registerServerReceiver(ResourceLocation resourceLocation,
                                                 Function<FriendlyByteBuf, M> decoder,
                                                 BiConsumer<M, ServerPlayer> handler) {
    ServerPlayNetworking.registerGlobalReceiver(resourceLocation,
        (server, player, listener, buf, responseSender) -> {
          M packet = decoder.apply(buf);
          server.execute(() -> handler.accept(packet, player));
        });
  }

  private static <M> void registerClientReceiver(ResourceLocation resourceLocation,
                                                 Function<FriendlyByteBuf, M> decoder,
                                                 Consumer<M> handler) {
    ClientPlayNetworking.registerGlobalReceiver(resourceLocation,
        (client, listener, buf, responseSender) -> {
          M packet = decoder.apply(buf);
          client.execute(() -> handler.accept(packet));
        });
  }
}
