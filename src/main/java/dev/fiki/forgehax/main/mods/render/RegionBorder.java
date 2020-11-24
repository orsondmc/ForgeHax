package dev.fiki.forgehax.main.mods.render;

import dev.fiki.forgehax.api.cmd.settings.BooleanSetting;
import dev.fiki.forgehax.api.cmd.settings.IntegerSetting;
import dev.fiki.forgehax.api.color.Colors;
import dev.fiki.forgehax.api.draw.GeometryMasks;
import dev.fiki.forgehax.api.events.RenderEvent;
import dev.fiki.forgehax.api.extension.VectorEx;
import dev.fiki.forgehax.api.extension.VertexBuilderEx;
import dev.fiki.forgehax.api.mod.Category;
import dev.fiki.forgehax.api.mod.ToggleMod;
import dev.fiki.forgehax.api.modloader.RegisterMod;
import lombok.experimental.ExtensionMethod;
import lombok.val;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import static dev.fiki.forgehax.main.Common.getLocalPlayer;

@RegisterMod(
    name = "RegionBorder",
    description = "Shows a border in front of the edge of the region you are in",
    category = Category.RENDER
)
@ExtensionMethod({VectorEx.class, VertexBuilderEx.class})
public class RegionBorder extends ToggleMod {
  private final IntegerSetting chunkDistance = newIntegerSetting()
      .name("chunk-distance")
      .description("how many chunks in front of the region the border should be drawn."
          + " I you don't want it just set it to 0 so it is like the normal region border.")
      .defaultTo(5)
      .build();

  private final BooleanSetting drawRegionBorder = newBooleanSetting()
      .name("draw-region-border")
      .description("whether you even want to draw the actual region border.")
      .defaultTo(true)
      .build();

  /**
   * to draw the border
   *
   * @param event
   */
  @SubscribeEvent
  public void onRender(RenderEvent event) {
    val stack = event.getMatrixStack();
    val builder = event.getBuffer();
    stack.push();

    builder.beginLines(DefaultVertexFormats.POSITION_COLOR);

    BlockPos from = new BlockPos((((int) getLocalPlayer().getPosX()) / 512) * 512,
        0, (((int) getLocalPlayer().getPosZ()) / 512) * 512);
    BlockPos to = from.add(511, 256, 511);

    if (drawRegionBorder.getValue()) {
      builder.outlinedCube(from, to, GeometryMasks.Line.ALL, Colors.ORANGE, stack.getLastMatrix());
    }

    final int chunkDistanceSetting = chunkDistance.getValue() * 16;
    from = from.add(chunkDistanceSetting, 0, chunkDistanceSetting);
    to = to.add(-chunkDistanceSetting, 0, -chunkDistanceSetting);

    builder.outlinedCube(from, to, GeometryMasks.Line.ALL, Colors.YELLOW, stack.getLastMatrix());

    builder.draw();
    stack.pop();
  }
}

