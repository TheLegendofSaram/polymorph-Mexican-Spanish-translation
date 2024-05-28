package com.illusivesoulworks.polymorph.common.network;

import com.illusivesoulworks.polymorph.common.network.server.SPacketBlockEntityRecipeSync;
import com.illusivesoulworks.polymorph.common.network.server.SPacketHighlightRecipe;
import com.illusivesoulworks.polymorph.common.network.server.SPacketPlayerRecipeSync;
import com.illusivesoulworks.polymorph.common.network.server.SPacketRecipesList;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class ClientPayloadHandler {

  private static final ClientPayloadHandler INSTANCE = new ClientPayloadHandler();

  public static ClientPayloadHandler getInstance() {
    return INSTANCE;
  }

  private static void handleData(final PlayPayloadContext context, Runnable handler) {
    context.workHandler().submitAsync(handler)
        .exceptionally(e -> {
          context.packetHandler()
              .disconnect(Component.translatable("polymorph.networking.failed", e.getMessage()));
          return null;
        });
  }

  public void handlePacket(final SPacketBlockEntityRecipeSync packet,
                           final PlayPayloadContext ctx) {
    handleData(ctx, () -> SPacketBlockEntityRecipeSync.handle(packet));
  }

  public void handlePacket(final SPacketHighlightRecipe packet, final PlayPayloadContext ctx) {
    handleData(ctx, () -> SPacketHighlightRecipe.handle(packet));
  }

  public void handlePacket(final SPacketPlayerRecipeSync packet, final PlayPayloadContext ctx) {
    handleData(ctx, () -> SPacketPlayerRecipeSync.handle(packet));
  }

  public void handlePacket(final SPacketRecipesList packet, final PlayPayloadContext ctx) {
    handleData(ctx, () -> SPacketRecipesList.handle(packet));
  }
}
