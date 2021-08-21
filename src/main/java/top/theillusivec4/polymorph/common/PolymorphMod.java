package top.theillusivec4.polymorph.common;

import java.util.Optional;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import top.theillusivec4.polymorph.api.PolymorphComponents;
import top.theillusivec4.polymorph.common.network.PolymorphPackets;
import top.theillusivec4.polymorph.common.util.CraftingPlayers;

public class PolymorphMod implements ModInitializer {

  public static final String MOD_ID = "polymorph";
  public static final Logger LOGGER = LogManager.getLogger();

  @Override
  public void onInitialize() {
    ServerPlayNetworking.registerGlobalReceiver(PolymorphPackets.SELECT_CRAFT,
        (minecraftServer, serverPlayerEntity, serverPlayNetworkHandler, packetByteBuf, packetSender) -> {
          Identifier id = packetByteBuf.readIdentifier();
          minecraftServer.execute(() -> {
            CraftingPlayers.add(serverPlayerEntity, id);
            serverPlayerEntity.currentScreenHandler.onContentChanged(serverPlayerEntity.inventory);
          });
        });
    ServerPlayNetworking.registerGlobalReceiver(PolymorphPackets.SELECT_PERSIST,
        (minecraftServer, serverPlayerEntity, serverPlayNetworkHandler, packetByteBuf, packetSender) -> {
          Identifier id = packetByteBuf.readIdentifier();
          BlockPos pos = packetByteBuf.readBlockPos();
          minecraftServer.execute(() -> {
            World world = serverPlayerEntity.getEntityWorld();
            Optional<? extends Recipe<?>> recipe = world.getRecipeManager().get(id);
            recipe.ifPresent(res -> {
              BlockEntity be = serverPlayerEntity.getEntityWorld().getBlockEntity(pos);
              PolymorphComponents.BLOCK_ENTITY_RECIPE_SELECTOR.maybeGet(be)
                  .ifPresent(selector -> selector.setSelectedRecipe(res));
            });
          });
        });
  }
}
