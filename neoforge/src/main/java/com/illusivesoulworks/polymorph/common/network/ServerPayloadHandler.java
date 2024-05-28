package com.illusivesoulworks.polymorph.common.network;

import com.illusivesoulworks.polymorph.common.network.client.CPacketBlockEntityListener;
import com.illusivesoulworks.polymorph.common.network.client.CPacketPersistentRecipeSelection;
import com.illusivesoulworks.polymorph.common.network.client.CPacketPlayerRecipeSelection;
import com.illusivesoulworks.polymorph.common.network.client.CPacketStackRecipeSelection;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class ServerPayloadHandler {

  private static final ServerPayloadHandler INSTANCE = new ServerPayloadHandler();

  public static ServerPayloadHandler getInstance() {
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

  public void handlePacket(final CPacketBlockEntityListener packet, final PlayPayloadContext ctx) {
    handleData(ctx, () -> ctx.player().ifPresent(player -> {
      if (player instanceof ServerPlayer serverPlayer) {
        CPacketBlockEntityListener.handle(packet, serverPlayer);
      }
    }));
  }

  public void handlePacket(final CPacketPersistentRecipeSelection packet,
                           final PlayPayloadContext ctx) {
    handleData(ctx, () -> ctx.player().ifPresent(player -> {
      if (player instanceof ServerPlayer serverPlayer) {
        CPacketPersistentRecipeSelection.handle(packet, serverPlayer);
      }
    }));
  }

  public void handlePacket(final CPacketPlayerRecipeSelection packet,
                           final PlayPayloadContext ctx) {
    handleData(ctx, () -> ctx.player().ifPresent(player -> {
      if (player instanceof ServerPlayer serverPlayer) {
        CPacketPlayerRecipeSelection.handle(packet, serverPlayer);
      }
    }));
  }

  public void handlePacket(final CPacketStackRecipeSelection packet, final PlayPayloadContext ctx) {
    handleData(ctx, () -> ctx.player().ifPresent(player -> {
      if (player instanceof ServerPlayer serverPlayer) {
        CPacketStackRecipeSelection.handle(packet, serverPlayer);
      }
    }));
  }
}
