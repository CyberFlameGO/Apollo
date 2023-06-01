package com.lunarclient.apollo.module.serverrule;

import com.lunarclient.apollo.module.ApolloModule;
import com.lunarclient.apollo.module.ModuleDefinition;
import com.lunarclient.apollo.option.NumberOption;
import com.lunarclient.apollo.option.Option;
import com.lunarclient.apollo.option.SimpleOption;
import io.leangen.geantyref.TypeToken;

/**
 * Represents the server rule module.
 *
 * @since 1.0.0
 */
@ModuleDefinition(id = "server_rule", name = "Server Rule")
public final class ServerRuleModule extends ApolloModule {

    /**
     * Whether the player should see a popup prior to disconnecting.
     *
     * @since 1.0.0
     */
    public static final SimpleOption<Boolean> COMPETITIVE_GAME = Option.<Boolean>builder()
        .comment("Set to 'true' to enable leaving game warning, otherwise 'false'.")
        .node("competitive-game").type(TypeToken.get(Boolean.class))
        .defaultValue(false).notifyClient().build();

    /**
     * Disables shaders.
     *
     * @since 1.0.0
     */
    public static final SimpleOption<Boolean> DISABLE_SHADERS = Option.<Boolean>builder()
        .comment("Set to 'true' to disable shaders, otherwise 'false'.")
        .node("disable-shaders").type(TypeToken.get(Boolean.class))
        .defaultValue(false).notifyClient().build();

    /**
     * Disables broadcast menu (F6).
     *
     * @since 1.0.0
     */
    public static final SimpleOption<Boolean> DISABLE_BROADCASTING = Option.<Boolean>builder()
        .comment("Set to 'true' to disable broadcasting, otherwise 'false'.")
        .node("disable-broadcasting").type(TypeToken.get(Boolean.class))
        .defaultValue(false).notifyClient().build();

    /**
     * Anti portal traps.
     *
     * <p>Allows players to open their chat while in a portal.</p>
     *
     * @since 1.0.0
     */
    public static final SimpleOption<Boolean> ANTI_PORTAL_TRAPS = Option.<Boolean>builder()
        .comment("Set to 'true' to enable anti portal traps, otherwise 'false'.")
        .node("anti-portal-traps").type(TypeToken.get(Boolean.class))
        .defaultValue(true).notifyClient().build();

    /**
     * Override brightness.
     *
     * @since 1.0.0
     */
    public static final SimpleOption<Boolean> OVERRIDE_BRIGHTNESS = Option.<Boolean>builder()
        .comment("Set to 'true' to override brightness, otherwise 'false'.")
        .node("override-brightness").type(TypeToken.get(Boolean.class))
        .defaultValue(false).notifyClient().build();

    /**
     * Sets brightness amount.
     *
     * @since 1.0.0
     */
    public static final NumberOption<Integer> BRIGHTNESS = Option.<Integer>number()
        .comment("Set the brightness amount.")
        .node("brightness").type(TypeToken.get(Integer.class))
        .defaultValue(50).min(1).max(10000).notifyClient().build();

    /**
     * Sets the nametag render distance amount.
     *
     * @since 1.0.0
     */
    public static final NumberOption<Integer> NAMETAG_RENDER_DISTANCE = Option.<Integer>number()
        .comment("Set the nametag render distance amount.")
        .node("nametag-render-distance").type(TypeToken.get(Integer.class))
        .defaultValue(64).min(1).max(96).notifyClient().build();

    /**
     * Override max chat length.
     *
     * @since 1.0.0
     */
    public static final SimpleOption<Boolean> OVERRIDE_MAX_CHAT_LENGTH = Option.<Boolean>builder()
        .comment("Set to 'true' to override max chat length, otherwise 'false'.")
        .node("override-max-chat-length").type(TypeToken.get(Boolean.class))
        .defaultValue(false).notifyClient().build();

    /**
     * Sets the max chat length amount.
     *
     * @since 1.0.0
     */
    public static final NumberOption<Integer> MAX_CHAT_LENGTH = Option.<Integer>number()
        .comment("Set the nametag render distance amount.")
        .node("max-chat-length").type(TypeToken.get(Integer.class))
        .defaultValue(256).min(1).max(256).notifyClient().build();

    ServerRuleModule() {
        this.registerOptions(
            COMPETITIVE_GAME,
            DISABLE_SHADERS,
            DISABLE_BROADCASTING,
            ANTI_PORTAL_TRAPS,
            OVERRIDE_BRIGHTNESS,
            BRIGHTNESS,
            NAMETAG_RENDER_DISTANCE,
            OVERRIDE_MAX_CHAT_LENGTH,
            MAX_CHAT_LENGTH
        );
    }

    @Override
    public boolean isClientNotify() {
        return true;
    }

}
