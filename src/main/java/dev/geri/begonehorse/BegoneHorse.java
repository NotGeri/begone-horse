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
    private static HoarsState state;

    @Override
    public void onInitializeClient() {
        logger.info("Registering hoarss");
        state = HoarsState.NORMAL;

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

                HoarsState nextState = state.getNext();

                player.sendMessage(Text.translatable("begonehorse.chat." + nextState.name().toLowerCase()));
                state = nextState;
            }

        });
    }

    /**
     * @return Get the current {@link HoarsState} of the mod
     */
    public static HoarsState getState() {
        return state;
    }

    public enum HoarsState {

        NORMAL,
        HEADLESS,
        TAIL,
        HEADED,
        GON;

        /**
         * Should a certain part of a hoars be rendered?
         *
         * @param partName The name of the body part
         * @return Whether it should be rendered client-side
         */
        public boolean shouldPartBeRendered(String partName) {
            switch (partName) {

                // Hoars head
                case "head" -> {
                    if (this == HEADLESS) return false;
                    if (this == GON) return false;
                    if (this == TAIL) return false;
                }

                // Hoars body
                case "body" -> {
                    if (this == GON) return false;
                    if (this == TAIL) return false;
                    if (this == HEADED) return false;
                }

            }

            return true;
        }

        /**
         * @return The next state in line
         */
        public HoarsState getNext() {
            logger.info(this.name());
            return values()[(this.ordinal() + 1) % values().length];
        }
    }

}
