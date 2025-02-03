package cn.ksmcbrigade.bh.mixin;

import cn.ksmcbrigade.bh.Config;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.phys.Vec3;

import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BeaconRenderer.class)
public class BeaconRendererMixin {
    @Inject(method = "render(Lnet/minecraft/world/level/block/entity/BeaconBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/blockentity/BeaconRenderer;renderBeaconBeam(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;FJII[F)V",shift = At.Shift.AFTER))
    private void render(BeaconBlockEntity par1, float p_112141_, PoseStack stack, MultiBufferSource p_112143_, int p_112144_, int p_112145_, CallbackInfo ci){
        Vec3 circleCenter = new Vec3(par1.getBlockPos().getX(),par1.getBlockPos().getY() + 1 + Config.HEIGHT.get(),par1.getBlockPos().getZ());
        Vec3 cameraPos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();

        float red = Config.RED.get() / 255.0f;
        float green = Config.GREEN.get() / 255.0f;
        float blue = Config.BLUE.get() / 255.0f;
        float alpha = Config.ALPHA.get();

        int segments = Config.SEGMENTS.get();

        for (int i = 0; i < Config.COUNT.get(); i++) {
            com.mojang.blaze3d.vertex.Tesselator tessellator = com.mojang.blaze3d.vertex.Tesselator.getInstance();
            BufferBuilder buffer = tessellator.getBuilder();

            stack.pushPose();
            stack.translate(circleCenter.x - cameraPos.x,
                    circleCenter.y - cameraPos.y,
                    circleCenter.z - cameraPos.z);

            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);

            buffer.begin(VertexFormat.Mode.DEBUG_LINE_STRIP, DefaultVertexFormat.POSITION_COLOR);

            double radius = beacon_halo$getRadius(i);
            for (int j = 0; j < segments; j++) {
                double angle = 2.0 * Math.PI * j / segments;
                double x = radius * Math.cos(angle);
                double z = radius * Math.sin(angle);

                Vector4f worldPos = new Vector4f((float) x, (float) circleCenter.y, (float) z, 1.0f);
                worldPos.mul(stack.last().pose());

                buffer.vertex(worldPos.x,worldPos.y,worldPos.z).color(red,green,blue,alpha).endVertex();
            }

            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            BufferUploader.drawWithShader(buffer.end());

            RenderSystem.disableBlend();
            stack.popPose();
        }
    }

    @Unique
    private double beacon_halo$getRadius(int i){
        int base = Config.RADIUS.get();
        int intervalR = Config.INTERVAL.get() / 2;
        double ret = base + (double) intervalR * i;
        if(!Config.INCREASING.get()){
            return ret;
        }
        else{
            return ret*i;
        }
    }
    
    @Inject(method = "getViewDistance",at = @At("RETURN"),cancellable = true)
    private void viewDistance(CallbackInfoReturnable<Integer> cir){
        cir.setReturnValue(Integer.MAX_VALUE);
    }
}
