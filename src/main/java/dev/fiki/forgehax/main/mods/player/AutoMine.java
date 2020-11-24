package dev.fiki.forgehax.main.mods.player;

import dev.fiki.forgehax.api.extension.LocalPlayerEx;
import dev.fiki.forgehax.api.key.BindingHelper;
import dev.fiki.forgehax.api.mod.Category;
import dev.fiki.forgehax.api.mod.ToggleMod;
import dev.fiki.forgehax.api.modloader.RegisterMod;
import dev.fiki.forgehax.asm.events.BlockControllerProcessEvent;
import dev.fiki.forgehax.asm.events.LeftClickCounterUpdateEvent;
import lombok.experimental.ExtensionMethod;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static dev.fiki.forgehax.main.Common.*;

@RegisterMod(
    name = "AutoMine",
    description = "Auto mine blocks",
    category = Category.PLAYER
)
@ExtensionMethod({LocalPlayerEx.class})
public class AutoMine extends ToggleMod {

  private boolean pressed = false;

  private void setPressed(boolean state) {
    getGameSettings().keyBindAttack.setPressed(state);
    pressed = state;
  }

  @Override
  protected void onEnabled() {
    BindingHelper.disableContextHandler(getGameSettings().keyBindAttack);
  }

  @Override
  protected void onDisabled() {
    setPressed(false);
    BindingHelper.restoreContextHandler(getGameSettings().keyBindAttack);
  }

  @SubscribeEvent
  public void onTick(TickEvent.ClientTickEvent event) {
    if (!isInWorld()) {
      return;
    }

    switch (event.phase) {
      case START: {
        RayTraceResult tr = getLocalPlayer().getBlockViewTrace();

        if (RayTraceResult.Type.MISS.equals(tr.getType())) {
          setPressed(false);
          return;
        }

        setPressed(true);
        break;
      }
      case END:
        setPressed(false);
        break;
    }
  }

  @SubscribeEvent
  public void onLeftClickCouterUpdate(LeftClickCounterUpdateEvent event) {
    // prevent the leftClickCounter from changing
    event.setCanceled(true);
  }

  @SubscribeEvent
  public void onBlockCounterUpdate(BlockControllerProcessEvent event) {
    // bug fix - left click is actually false after processing the key bindings
    // this will set that boolean to the correct value
    if (pressed) {
      event.setLeftClicked(true);
    }
  }
}
