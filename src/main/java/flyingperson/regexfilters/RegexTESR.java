package flyingperson.regexfilters;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLSync;

import java.util.ArrayList;
import java.util.Random;

@SideOnly(Side.CLIENT)
public class RegexTESR extends TileEntitySpecialRenderer<RegexTileEntity> {

    public static ArrayList<RegexParticle> particles;

    static {
        particles = new ArrayList<>();
        for (int t = 0; t < 360; t += 30) {
            particles.add(new RegexParticle(new Vec3d(Math.sin(Math.toRadians(t))*0.25, 0, Math.cos(Math.toRadians(t))*0.25)));
        }
    }

    @Override
    public void render(RegexTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();

        GlStateManager.translate(x, y, z);
        GlStateManager.disableRescaleNormal();


        //for (EnumFacing e : EnumFacing.HORIZONTALS) {
        //    TileEntity tile = te.getWorld().getTileEntity(te.getPos().offset(e));
        //    if (tile != null) {
        //        if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, e.getOpposite())) {
        //            renderItemPort(e, te, (float) x, (float) y, (float) z, partialTicks, Minecraft.getMinecraft().player, te.getPos());
        //        }
        //    }
        //}

        renderBook(te, partialTicks);

        for (RegexParticle p : particles) {
            p.render(partialTicks, te.counter);
        }

        renderStaticLightning(te, partialTicks, Minecraft.getMinecraft().player, x, y, z);


        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }

    private void renderItemPort(EnumFacing side, RegexTileEntity te, float x, float y, float z, float partialTicks, EntityPlayer player, BlockPos pos) {


        bindTexture(new ResourceLocation(RegexFilters.MODID, "textures/blocks/machine.png"));
        GlStateManager.pushMatrix();
        GlStateManager.translate(0.5, 0.5, 0.5);
        GlStateManager.disableCull();
        BufferBuilder buf = Tessellator.getInstance().getBuffer();
        buf.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        switch(side) {
            case NORTH:
                buf.pos(-0.25, -0.25, -0.5).tex(0, 0).endVertex();
                buf.pos(0.25, -0.25, -0.5).tex(0, 1).endVertex();
                buf.pos(0.25, 0.25, -0.5).tex(1, 1).endVertex();
                buf.pos(-0.25, 0.25, -0.5).tex(1, 0).endVertex();
/*
                buf.pos(-0.25, -0.25, -0.45).tex(0, 0).endVertex();
                buf.pos(0.25, -0.25, -0.45).tex(0, 1).endVertex();
                buf.pos(0.25, 0.25, -0.45).tex(1, 1).endVertex();
                buf.pos(-0.25, 0.25, -0.45).tex(1, 0).endVertex();

                buf.pos(-0.25, -0.25, -0.5).tex(0.25, 0.525).endVertex();
                buf.pos(-0.25, -0.25, -0.45).tex(0.25, 0.475).endVertex();
                buf.pos(0.25, -0.25, -0.45).tex(0.75, 0.475).endVertex();
                buf.pos(0.25, -0.25, -0.5).tex(0.75, 0.525).endVertex();

                buf.pos(-0.25, -0.25, -0.5).tex(0.25, 0.525).endVertex();
                buf.pos(-0.25, -0.25, -0.45).tex(0.25, 0.475).endVertex();
                buf.pos(0.25, 0.25, -0.45).tex(0.75, 0.475).endVertex();
                buf.pos(0.25, 0.25, -0.5).tex(0.75, 0.525).endVertex();

                buf.pos(-0.25, -0.25, -0.5).tex(0.25, 0.525).endVertex();
                buf.pos(-0.25, -0.25, -0.45).tex(0.25, 0.475).endVertex();
                buf.pos(-0.25, 0.25, -0.45).tex(0.75, 0.475).endVertex();
                buf.pos(-0.25, 0.25, -0.5).tex(0.75, 0.525).endVertex();

                buf.pos(-0.25, -0.25, -0.5).tex(0.25, 0.525).endVertex();
                buf.pos(-0.25, -0.25, -0.45).tex(0.25, 0.475).endVertex();
                buf.pos(-0.25, -0.25, -0.45).tex(0.75, 0.475).endVertex();
                buf.pos(-0.25, -0.25, -0.5).tex(0.75, 0.525).endVertex();*/




                break;
            case SOUTH:
                buf.pos(-0.25, -0.25, 0.5).tex(0, 0).endVertex();
                buf.pos(0.25, -0.25, 0.5).tex(0, 1).endVertex();
                buf.pos(0.25, 0.25, 0.5).tex(1, 1).endVertex();
                buf.pos(-0.25, 0.25, 0.5).tex(1, 0).endVertex();
/*
                buf.pos(-0.25, -0.25, 0.45).tex(0, 0).endVertex();
                buf.pos(0.25, -0.25, 0.45).tex(0, 1).endVertex();
                buf.pos(0.25, 0.25, 0.45).tex(1, 1).endVertex();
                buf.pos(-0.25, 0.25, 0.45).tex(1, 0).endVertex();

                buf.pos(-0.25, -0.25, 0.5).tex(0.25, 0.525).endVertex();
                buf.pos(-0.25, -0.25, 0.45).tex(0.25, 0.475).endVertex();
                buf.pos(0.25, -0.25, 0.45).tex(0.75, 0.475).endVertex();
                buf.pos(0.25, -0.25, 0.5).tex(0.75, 0.525).endVertex();

                buf.pos(-0.25, -0.25, 0.5).tex(0.25, 0.525).endVertex();
                buf.pos(-0.25, -0.25, 0.45).tex(0.25, 0.475).endVertex();
                buf.pos(0.25, 0.25, 0.45).tex(0.75, 0.475).endVertex();
                buf.pos(0.25, 0.25, 0.5).tex(0.75, 0.525).endVertex();

                buf.pos(-0.25, -0.25, 0.5).tex(0.25, 0.525).endVertex();
                buf.pos(-0.25, -0.25, 0.45).tex(0.25, 0.475).endVertex();
                buf.pos(-0.25, 0.25, 0.45).tex(0.75, 0.475).endVertex();
                buf.pos(-0.25, 0.25, 0.5).tex(0.75, 0.525).endVertex();

                buf.pos(-0.25, -0.25, 0.5).tex(0.25, 0.525).endVertex();
                buf.pos(-0.25, -0.25, 0.45).tex(0.25, 0.475).endVertex();
                buf.pos(-0.25, -0.25, 0.45).tex(0.75, 0.475).endVertex();
                buf.pos(-0.25, -0.25, 0.5).tex(0.75, 0.525).endVertex();*/


                break;
            case EAST:
                buf.pos(0.5, -0.25, -0.25).tex(0, 0).endVertex();
                buf.pos(0.5, -0.25, 0.25).tex(0, 1).endVertex();
                buf.pos(0.5, 0.25, 0.25).tex(1, 1).endVertex();
                buf.pos(0.5, 0.25, -0.25).tex(1, 0).endVertex();
/*
                buf.pos(0.45, -0.25, -0.25).tex(0, 0).endVertex();
                buf.pos(0.45, -0.25, 0.25).tex(0, 1).endVertex();
                buf.pos(0.45, 0.25, 0.25).tex(1, 1).endVertex();
                buf.pos(0.45, 0.25, -0.25).tex(1, 0).endVertex();

                buf.pos(0.5, -0.25, -0.25).tex(0.25, 0.525).endVertex();
                buf.pos(0.45, -0.25, -0.25).tex(0.25, 0.475).endVertex();
                buf.pos(0.45, -0.25, 0.25).tex(0.75, 0.475).endVertex();
                buf.pos(0.5, -0.25, 0.25).tex(0.75, 0.525).endVertex();

                buf.pos(0.5, -0.25, -0.25).tex(0.25, 0.525).endVertex();
                buf.pos(0.45, -0.25, -0.25).tex(0.25, 0.475).endVertex();
                buf.pos(0.45, 0.25, 0.25).tex(0.75, 0.475).endVertex();
                buf.pos(0.5, 0.25, 0.25).tex(0.75, 0.525).endVertex();

                buf.pos(0.5, -0.25, -0.25).tex(0.25, 0.525).endVertex();
                buf.pos(0.45, -0.25, -0.25).tex(0.25, 0.475).endVertex();
                buf.pos(0.45, 0.25, -0.25).tex(0.75, 0.475).endVertex();
                buf.pos(0.5, 0.25, -0.25).tex(0.75, 0.525).endVertex();

                buf.pos(0.5, -0.25, -0.25).tex(0.25, 0.525).endVertex();
                buf.pos(0.45, -0.25, -0.25).tex(0.25, 0.475).endVertex();
                buf.pos(0.45, -0.25, -0.25).tex(0.75, 0.475).endVertex();
                buf.pos(0.5, -0.25, -0.25).tex(0.75, 0.525).endVertex();*/


                break;
            case WEST:
                buf.pos(-0.5, -0.25, -0.25).tex(0, 0).endVertex();
                buf.pos(-0.5, -0.25, 0.25).tex(0, 1).endVertex();
                buf.pos(-0.5, 0.25, 0.25).tex(1, 1).endVertex();
                buf.pos(-0.5, 0.25, -0.25).tex(1, 0).endVertex();

                /*
                buf.pos(-0.45, -0.25, -0.25).tex(0, 0).endVertex();
                buf.pos(-0.45, -0.25, 0.25).tex(0, 1).endVertex();
                buf.pos(-0.45, 0.25, 0.25).tex(1, 1).endVertex();
                buf.pos(-0.45, 0.25, -0.25).tex(1, 0).endVertex();

                buf.pos(-0.5, -0.25, -0.25).tex(0.25, 0.525).endVertex();
                buf.pos(-0.45, -0.25, -0.25).tex(0.25, 0.475).endVertex();
                buf.pos(-0.45, -0.25, 0.25).tex(0.75, 0.475).endVertex();
                buf.pos(-0.5, -0.25, 0.25).tex(0.75, 0.525).endVertex();

                buf.pos(-0.5, -0.25, -0.25).tex(0.25, 0.525).endVertex();
                buf.pos(-0.45, -0.25, -0.25).tex(0.25, 0.475).endVertex();
                buf.pos(-0.45, 0.25, 0.25).tex(0.75, 0.475).endVertex();
                buf.pos(-0.5, 0.25, 0.25).tex(0.75, 0.525).endVertex();

                buf.pos(-0.5, -0.25, -0.25).tex(0.25, 0.525).endVertex();
                buf.pos(-0.45, -0.25, -0.25).tex(0.25, 0.475).endVertex();
                buf.pos(-0.45, 0.25, -0.25).tex(0.75, 0.475).endVertex();
                buf.pos(-0.5, 0.25, -0.25).tex(0.75, 0.525).endVertex();

                buf.pos(-0.5, -0.25, -0.25).tex(0.25, 0.525).endVertex();
                buf.pos(-0.45, -0.25, -0.25).tex(0.25, 0.475).endVertex();
                buf.pos(-0.45, -0.25, -0.25).tex(0.75, 0.475).endVertex();
                buf.pos(-0.5, -0.25, -0.25).tex(0.75, 0.525).endVertex();*/

                break;
        }


        Tessellator.getInstance().draw();



        GlStateManager.popMatrix();
    }



    private void renderStaticLightning(RegexTileEntity te, float partialTicks, EntityPlayer aPlayer, double x, double y, double z) {
        renderLightning(new Vec3d(0.25, 0.03125, 0.25), new Vec3d(0.25, 0.96875, 0.25), x, y, z, partialTicks, aPlayer, te.getPos());
        renderLightning(new Vec3d(-0.25, 0.03125, 0.25), new Vec3d(-0.25, 0.96875, 0.25), x, y, z, partialTicks, aPlayer, te.getPos());
        renderLightning(new Vec3d(-0.25, 0.03125, -0.25), new Vec3d(-0.25, 0.96875, -0.25), x, y, z, partialTicks, aPlayer, te.getPos());
        renderLightning(new Vec3d(0.25, 0.03125, -0.25), new Vec3d(0.25, 0.96875, -0.25), x, y, z, partialTicks, aPlayer, te.getPos());

    }

    private void renderLightning(Vec3d pos1, Vec3d pos2, double aX, double aY, double aZ, float aPartialTicks, EntityPlayer aPlayer, BlockPos pos) {
        Random rand = new Random();

        float r = 1.0f;
        float g = 1.0f;
        float b = 1.0f;
        float a = 1.0f;

        GL11.glDisable(GL11.GL_CULL_FACE);

        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glPushMatrix();
        GL11.glTranslated((aPlayer.lastTickPosX + (aPlayer.posX - aPlayer.lastTickPosX) * aPartialTicks), (aPlayer.lastTickPosY + (aPlayer.posY - aPlayer.lastTickPosY) * aPartialTicks), (aPlayer.lastTickPosZ + (aPlayer.posZ - aPlayer.lastTickPosZ) * aPartialTicks));
        GL11.glTranslated(aX + 0.5, aY + 0.5, aZ + 0.5);
        GL11.glTranslated(-pos.getX(), -pos.getY(), -pos.getZ());
        GL11.glTranslated(0, -0.5, 0);
        float f = 8.0F - (float) aPlayer.getDistance(pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5);
        GL11.glLineWidth(f <= 0 ? 0.00001f : f);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glBegin(GL11.GL_LINES);


        GL11.glColor4d(r, g, b, a);


        GL11.glVertex3d((pos1.x+((double) rand.nextInt(100)/1000)), pos1.y, (pos1.z+((double) rand.nextInt(100)/1000)));
        GL11.glVertex3d((pos2.x+((double) rand.nextInt(100)/1000)), pos2.y - rand.nextFloat()/4, (pos2.z+((double) rand.nextInt(100)/1000)));


        GL11.glVertex3d((pos2.x+((double) rand.nextInt(100)/1000)), pos2.y, (pos2.z+((double) rand.nextInt(100)/1000)));
        GL11.glVertex3d((pos1.x+((double) rand.nextInt(100)/1000)), pos1.y + rand.nextFloat()/4, (pos1.z+((double) rand.nextInt(100)/1000)));


        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }


    private void renderBook(RegexTileEntity te, float partialTicks) {
        RenderHelper.enableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        GlStateManager.pushMatrix();
        GlStateManager.translate(.5, .5, .5);
        GlStateManager.scale(.4f, .4f, .4f);
        GlStateManager.rotate(te.counter+partialTicks, 0, 1.0f, 0);
        Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(Items.BOOK, 1), ItemCameraTransforms.TransformType.NONE);

        GlStateManager.popMatrix();
    }

}