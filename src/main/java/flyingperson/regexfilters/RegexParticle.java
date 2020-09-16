package flyingperson.regexfilters;

import codechicken.lib.colour.Colour;
import codechicken.lib.render.CCModelLibrary;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.texture.TextureUtils;
import codechicken.lib.vec.Scale;
import codechicken.lib.vec.Vector3;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


@SideOnly(Side.CLIENT)
public class RegexParticle {
    public Vec3d offset;

    public RegexParticle(Vec3d offset) {
        this.offset = offset;
    }

    public void render(float partialTicks, int counter) {
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.5, 0.5, 0.5);
        GlStateManager.rotate((counter+partialTicks)*32, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(1, 1.0f, 0.0f, 0.0f);
        drawSphere(CCRenderState.instance(), new Vector3(offset.x, offset.y, offset.z), partialTicks, counter, new ResourceLocation(RegexFilters.MODID, "textures/particles/particle2.png"));
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();

        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.5, 0.5, 0.5);
        GlStateManager.rotate(-(counter+partialTicks)*16, 0.0f, 1.0f, 0.0f);
        GlStateManager.rotate(1, 1.0f, 0.0f, 0.0f);
        drawSphere(CCRenderState.instance(), new Vector3(offset.x*1.4, offset.y*1.4, offset.z*1.4), partialTicks, counter, new ResourceLocation(RegexFilters.MODID, "textures/particles/particle.png"));
        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }


    public void drawSphere(CCRenderState ccrs, Vector3 pos, float partialTicks, int counter, ResourceLocation texture) {
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.pushMatrix();
        pos.translation().glApply();
        new Scale(0.032).glApply();
        GlStateManager.rotate((counter+partialTicks), 0, 1, 0);
        float light = 1.0f;
        GlStateManager.disableLighting();
        ccrs.reset();
        TextureUtils.changeTexture(texture);
        ccrs.pullLightmap();
        ccrs.colour = Colour.packRGBA(light, light, light, 1);
        ccrs.startDrawing(4, DefaultVertexFormats.POSITION_TEX_NORMAL);
        CCModelLibrary.icosahedron4.render(ccrs);
        ccrs.draw();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }
}