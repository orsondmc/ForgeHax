package dev.fiki.forgehax.api.extension;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import dev.fiki.forgehax.api.color.Color;
import dev.fiki.forgehax.api.draw.GeometryMasks;
import lombok.val;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.util.math.vector.Vector4f;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;

public class VertexBuilderEx {
  public static IVertexBuilder color(IVertexBuilder builder, @Nullable Color color) {
    if (color != null) {
      if (color.isFloatType()) {
        float[] color4f = color.toFloatArray();
        builder.color(color4f[0], color4f[1], color4f[2], color4f[3]);
      } else {
        builder.color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
      }
    }
    return builder;
  }

  public static IVertexBuilder posD(IVertexBuilder builder, double x, double y, double z, @Nullable Matrix4f matrix) {
    if (matrix != null) {
      val vec = new Vector4f((float) x, (float) y, (float) z, 1.f);
      vec.transform(matrix);
      x = vec.getX();
      y = vec.getY();
      z = vec.getZ();
    }
    return builder.pos(x, y, z);
  }

  public static IVertexBuilder line(IVertexBuilder builder,
      double startX, double startY, double startZ,
      double endX, double endY, double endZ,
      @Nullable Color color, @Nullable Matrix4f matrix) {
    color(posD(builder, startX, startY, startZ, matrix), color).endVertex();
    color(posD(builder, endX, endY, endZ, matrix), color).endVertex();
    return builder;
  }

  public static IVertexBuilder line(IVertexBuilder builder,
      Vector3d start, Vector3d end,
      @Nullable Color color, @Nullable Matrix4f matrix) {
    return line(builder, start.getX(), start.getY(), start.getZ(), end.getX(), end.getY(), end.getZ(), color, matrix);
  }

  public static IVertexBuilder line(IVertexBuilder builder,
      Vector3d start, Vector3d end,
      @Nullable Matrix4f matrix) {
    return line(builder, start, end, null, matrix);
  }

  public static IVertexBuilder line(IVertexBuilder builder,
      double startX, double startY, double startZ,
      double endX, double endY, double endZ,
      @Nullable Matrix4f matrix) {
    return line(builder, startX, startY, startZ, endX, endY, endZ, null, matrix);
  }

  public static IVertexBuilder line(IVertexBuilder builder,
      double startX, double startY,
      double endX, double endY,
      @Nullable Color color, @Nullable Matrix4f matrix) {
    return line(builder, startX, startY, 0.D, endX, endY, 0.D, color, matrix);
  }

  public static IVertexBuilder line(IVertexBuilder builder,
      double startX, double startY,
      double endX, double endY,
      @Nullable Matrix4f matrix) {
    return line(builder, startX, startY, endX, endY, null, matrix);
  }

  public static IVertexBuilder outlinedLines(IVertexBuilder builder,
      double x, double y,
      double width, double height,
      @Nullable Color color, @Nullable Matrix4f matrix) {
    line(builder, x, y, x, y + height, color, matrix);
    line(builder, x, y, x + width, y, color, matrix);
    line(builder, x, y + height, x + width, y + height, color, matrix);
    return line(builder, x + width, y, x + width, y + height, color, matrix);
  }

  public static IVertexBuilder rect(IVertexBuilder builder,
      int drawMode,
      double x, double y,
      double w, double h,
      @Nullable Color color, @Nullable Matrix4f matrix) {
    switch (drawMode) {
      case GL11.GL_QUADS:
        color(posD(builder, x, y, 0.D, matrix), color).endVertex();
        color(posD(builder, x, y + h, 0.D, matrix), color).endVertex();
        color(posD(builder, x + w, y + h, 0.D, matrix), color).endVertex();
        color(posD(builder, x + w, y, 0.D, matrix), color).endVertex();
        break;
      case GL11.GL_TRIANGLES:
        color(posD(builder, x, y, 0.D, matrix), color).endVertex();
        color(posD(builder, x, y + h, 0.D, matrix), color).endVertex();
        color(posD(builder, x + w, y, 0.D, matrix), color).endVertex();
        color(posD(builder, x, y + h, 0.D, matrix), color).endVertex();
        color(posD(builder, x + w, y + h, 0.D, matrix), color).endVertex();
        color(posD(builder, x + w, y, 0.D, matrix), color).endVertex();
        break;
      default:
        throw new IllegalStateException("draw mode must be QUADS or TRIANGLES");
    }
    return builder;
  }

  public static IVertexBuilder rect(IVertexBuilder builder,
      int drawMode,
      double x, double y,
      double w, double h,
      @Nullable Matrix4f matrix) {
    return rect(builder, drawMode, x, y, w, h, null, matrix);
  }

  public static IVertexBuilder outlinedRect(IVertexBuilder builder,
      int drawMode,
      double x, double y,
      double w, double h,
      double lineSize,
      @Nullable Color color, @Nullable Matrix4f matrix) {
    rect(builder, drawMode, x, y, w, lineSize, color, matrix);
    rect(builder, drawMode, x + w - lineSize, y, lineSize, h, color, matrix);
    rect(builder, drawMode, x, y, lineSize, h, color, matrix);
    return rect(builder, drawMode, x, y + h - lineSize, w, lineSize, color, matrix);
  }

  public static IVertexBuilder texturedRect(IVertexBuilder builder,
      double x, double y,
      float textureX, float textureY,
      float width, float height,
      double depth,
      @Nullable Color color, @Nullable Matrix4f matrix) {
    color(posD(builder, x, y + height, depth, matrix)
        .tex(textureX + 0, textureY + height), color)
        .endVertex();
    color(posD(builder, x + width, y + height, depth, matrix)
        .tex(textureX + width, textureY + height), color)
        .endVertex();
    color(posD(builder, x + width, y + 0, depth, matrix)
        .tex(textureX + width, textureY + 0), color)
        .endVertex();
    color(posD(builder, x + 0, y + 0, depth, matrix)
        .tex(textureX + 0, textureY + 0), color)
        .endVertex();
    return builder;
  }

  public static IVertexBuilder texturedRect(IVertexBuilder builder,
      double x, double y,
      float textureX, float textureY,
      float width, float height,
      float depth,
      @Nullable Matrix4f matrix) {
    return texturedRect(builder, x, y, textureX, textureY, width, height, depth, null, matrix);
  }

  public static IVertexBuilder gradientRect(IVertexBuilder builder,
      double x, double y,
      double x2, double y2,
      Color outlineColor, Color shadeColor, @Nullable Matrix4f matrix) {
    color(posD(builder, x2, y, 0, matrix), outlineColor).endVertex();
    color(posD(builder, x, y, 0, matrix), outlineColor).endVertex();
    color(posD(builder, x, y2, 0, matrix), shadeColor).endVertex();
    color(posD(builder, x2, y2, 0, matrix), shadeColor).endVertex();
    return builder;
  }

  public static IVertexBuilder filledCube(IVertexBuilder builder,
      final double x0, final double y0, final double z0,
      final double x1, final double y1, final double z1,
      final int sides,
      @Nullable Color color, @Nullable Matrix4f matrix) {
    if ((sides & GeometryMasks.Quad.DOWN) != 0) {
      color(posD(builder, x1, y0, z0, matrix), color).endVertex();
      color(posD(builder, x1, y0, z1, matrix), color).endVertex();
      color(posD(builder, x0, y0, z1, matrix), color).endVertex();
      color(posD(builder, x0, y0, z0, matrix), color).endVertex();
    }

    if ((sides & GeometryMasks.Quad.UP) != 0) {
      color(posD(builder, x1, y1, z0, matrix), color).endVertex();
      color(posD(builder, x0, y1, z0, matrix), color).endVertex();
      color(posD(builder, x0, y1, z1, matrix), color).endVertex();
      color(posD(builder, x1, y1, z1, matrix), color).endVertex();
    }

    if ((sides & GeometryMasks.Quad.NORTH) != 0) {
      color(posD(builder, x1, y0, z0, matrix), color).endVertex();
      color(posD(builder, x0, y0, z0, matrix), color).endVertex();
      color(posD(builder, x0, y1, z0, matrix), color).endVertex();
      color(posD(builder, x1, y1, z0, matrix), color).endVertex();
    }

    if ((sides & GeometryMasks.Quad.SOUTH) != 0) {
      color(posD(builder, x0, y0, z1, matrix), color).endVertex();
      color(posD(builder, x1, y0, z1, matrix), color).endVertex();
      color(posD(builder, x1, y1, z1, matrix), color).endVertex();
      color(posD(builder, x0, y1, z1, matrix), color).endVertex();
    }

    if ((sides & GeometryMasks.Quad.WEST) != 0) {
      color(posD(builder, x0, y0, z0, matrix), color).endVertex();
      color(posD(builder, x0, y0, z1, matrix), color).endVertex();
      color(posD(builder, x0, y1, z1, matrix), color).endVertex();
      color(posD(builder, x0, y1, z0, matrix), color).endVertex();
    }

    if ((sides & GeometryMasks.Quad.EAST) != 0) {
      color(posD(builder, x1, y0, z1, matrix), color).endVertex();
      color(posD(builder, x1, y0, z0, matrix), color).endVertex();
      color(posD(builder, x1, y1, z0, matrix), color).endVertex();
      color(posD(builder, x1, y1, z1, matrix), color).endVertex();
    }

    return builder;
  }

  public static IVertexBuilder filledCube(IVertexBuilder builder,
      Vector3d start, Vector3d finish,
      final int sides,
      @Nullable Color color, @Nullable Matrix4f matrix) {
    return filledCube(builder, start.getX(), start.getY(), start.getZ(),
        finish.getX(), finish.getY(), finish.getZ(), sides, color, matrix);
  }

  public static IVertexBuilder filledCube(IVertexBuilder builder,
      Vector3i start, Vector3i finish,
      final int sides,
      @Nullable Color color, @Nullable Matrix4f matrix) {
    return filledCube(builder, start.getX(), start.getY(), start.getZ(),
        finish.getX(), finish.getY(), finish.getZ(), sides, color, matrix);
  }

  public static IVertexBuilder filledCube(IVertexBuilder builder,
      AxisAlignedBB bb,
      final int sides,
      @Nullable Color color, @Nullable Matrix4f matrix) {
    return filledCube(builder, bb.minX, bb.minY, bb.maxX,
        bb.maxX, bb.maxY, bb.maxZ, sides, color, matrix);
  }

  public static IVertexBuilder outlinedCube(IVertexBuilder builder,
      double x0, double y0, double z0,
      double x1, double y1, double z1,
      final int sides,
      @Nullable Color color, @Nullable Matrix4f matrix) {
    if ((sides & GeometryMasks.Line.DOWN_WEST) != 0) {
      color(posD(builder, x0, y0, z0, matrix), color).endVertex();
      color(posD(builder, x0, y0, z1, matrix), color).endVertex();
    }

    if ((sides & GeometryMasks.Line.UP_WEST) != 0) {
      color(posD(builder, x0, y1, z0, matrix), color).endVertex();
      color(posD(builder, x0, y1, z1, matrix), color).endVertex();
    }

    if ((sides & GeometryMasks.Line.DOWN_EAST) != 0) {
      color(posD(builder, x1, y0, z0, matrix), color).endVertex();
      color(posD(builder, x1, y0, z1, matrix), color).endVertex();
    }

    if ((sides & GeometryMasks.Line.UP_EAST) != 0) {
      color(posD(builder, x1, y1, z0, matrix), color).endVertex();
      color(posD(builder, x1, y1, z1, matrix), color).endVertex();
    }

    if ((sides & GeometryMasks.Line.DOWN_NORTH) != 0) {
      color(posD(builder, x0, y0, z0, matrix), color).endVertex();
      color(posD(builder, x1, y0, z0, matrix), color).endVertex();
    }

    if ((sides & GeometryMasks.Line.UP_NORTH) != 0) {
      color(posD(builder, x0, y1, z0, matrix), color).endVertex();
      color(posD(builder, x1, y1, z0, matrix), color).endVertex();
    }

    if ((sides & GeometryMasks.Line.DOWN_SOUTH) != 0) {
      color(posD(builder, x0, y0, z1, matrix), color).endVertex();
      color(posD(builder, x1, y0, z1, matrix), color).endVertex();
    }

    if ((sides & GeometryMasks.Line.UP_SOUTH) != 0) {
      color(posD(builder, x0, y1, z1, matrix), color).endVertex();
      color(posD(builder, x1, y1, z1, matrix), color).endVertex();
    }

    if ((sides & GeometryMasks.Line.NORTH_WEST) != 0) {
      color(posD(builder, x0, y0, z0, matrix), color).endVertex();
      color(posD(builder, x0, y1, z0, matrix), color).endVertex();
    }

    if ((sides & GeometryMasks.Line.NORTH_EAST) != 0) {
      color(posD(builder, x1, y0, z0, matrix), color).endVertex();
      color(posD(builder, x1, y1, z0, matrix), color).endVertex();
    }

    if ((sides & GeometryMasks.Line.SOUTH_WEST) != 0) {
      color(posD(builder, x0, y0, z1, matrix), color).endVertex();
      color(posD(builder, x0, y1, z1, matrix), color).endVertex();
    }

    if ((sides & GeometryMasks.Line.SOUTH_EAST) != 0) {
      color(posD(builder, x1, y0, z1, matrix), color).endVertex();
      color(posD(builder, x1, y1, z1, matrix), color).endVertex();
    }

    return builder;
  }

  public static IVertexBuilder outlinedCube(IVertexBuilder builder,
      Vector3d start, Vector3d finish,
      final int sides,
      @Nullable Color color, @Nullable Matrix4f matrix) {
    return outlinedCube(builder, start.getX(), start.getY(), start.getZ(),
        finish.getX(), finish.getY(), finish.getZ(), sides, color, matrix);
  }

  public static IVertexBuilder outlinedCube(IVertexBuilder builder,
      Vector3i start, Vector3i finish,
      final int sides,
      @Nullable Color color, @Nullable Matrix4f matrix) {
    return outlinedCube(builder, start.getX(), start.getY(), start.getZ(),
        finish.getX(), finish.getY(), finish.getZ(), sides, color, matrix);
  }

  public static IVertexBuilder outlinedCube(IVertexBuilder builder,
      AxisAlignedBB bb,
      final int sides,
      @Nullable Color color, @Nullable Matrix4f matrix) {
    return outlinedCube(builder, bb.minX, bb.minY, bb.minZ,
        bb.maxX, bb.maxY, bb.maxZ, sides, color, matrix);
  }

  public static void beginLines(BufferBuilder builder, VertexFormat format) {
    builder.begin(GL11.GL_LINES, format);
  }

  public static void beginLineLoop(BufferBuilder builder, VertexFormat format) {
    builder.begin(GL11.GL_LINE_LOOP, format);
  }

  public static void beginQuads(BufferBuilder builder, VertexFormat format) {
    builder.begin(GL11.GL_QUADS, format);
  }

  public static void draw(BufferBuilder builder) {
    WorldVertexBufferUploader.draw(builder);
  }

  public static void translateVec(MatrixStack stack, Vector3d vec) {
    stack.translate(vec.getX(), vec.getY(), vec.getZ());
  }

  public static void translateVec(MatrixStack stack, Vector3i vec) {
    stack.translate(vec.getX(), vec.getY(), vec.getZ());
  }

  public static Matrix4f getLastMatrix(MatrixStack stack) {
    return stack.getLast().getMatrix();
  }
}
