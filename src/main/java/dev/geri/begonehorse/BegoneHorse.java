package dev.geri.begonehorse;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Environment(EnvType.CLIENT)
public class BegoneHorse implements ClientModInitializer {

    private static final Logger logger = LoggerFactory.getLogger("BegoneHorse");
    private static boolean headless = true;

    @Override
    public void onInitializeClient() {
        logger.info("Registering hoarss");

        KeyBinding keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.begonehorse.toggle",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_V,
                "key.categories.gameplay"
        ));

        // Listen for hotkeys
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            PlayerEntity player = client.player;
            if (client.world == null || player == null || player.isDead()) return;

            // Check if it was pressed
            if (keyBinding.wasPressed()) {
                player.sendMessage(Text.translatable(headless ? "begonehorse.chat.hoarsback" : "begonehorse.chat.hoarsgon"));
                headless = !headless;
            }

        });
    }

    /**
     * @return Weather horse heads should be rendered
     */
    public static boolean isHeadless() {
        return headless;
    }

}
