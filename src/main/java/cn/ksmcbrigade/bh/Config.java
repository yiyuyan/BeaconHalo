package cn.ksmcbrigade.bh;

import net.minecraftforge.common.ForgeConfigSpec;

import java.awt.*;

public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec.IntValue HEIGHT = BUILDER.defineInRange("height",128,100,4096);
    public static final ForgeConfigSpec.IntValue SEGMENTS = BUILDER.defineInRange("segments",240,50,1000000);

    public static final ForgeConfigSpec.IntValue COUNT = BUILDER.defineInRange("count",5,1,10);
    public static final ForgeConfigSpec.IntValue RADIUS = BUILDER.defineInRange("radius",48,12,100000);
    public static final ForgeConfigSpec.IntValue INTERVAL = BUILDER.comment("Radius increment between consecutive halo circles").defineInRange("interval",32,10,1000);
    public static final ForgeConfigSpec.BooleanValue INCREASING = BUILDER.define("increasing",true);

    public static final ForgeConfigSpec.ConfigValue<Float> RED = BUILDER.define("color_red", (float) Color.WHITE.getRed());
    public static final ForgeConfigSpec.ConfigValue<Float> GREEN = BUILDER.define("color_green", (float) Color.WHITE.getGreen());
    public static final ForgeConfigSpec.ConfigValue<Float> BLUE = BUILDER.define("color_blue", (float) Color.WHITE.getBlue());
    public static final ForgeConfigSpec.ConfigValue<Float> ALPHA = BUILDER.comment("Range: 0.0 ~ 1.0").define("color_alpha", 0.85F);

    static final ForgeConfigSpec SPEC = BUILDER.build();
}
