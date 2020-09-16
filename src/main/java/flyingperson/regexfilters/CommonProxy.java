package flyingperson.regexfilters;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber
public class CommonProxy {

    public void preInit(FMLPreInitializationEvent e) {
    }

    public void init(FMLInitializationEvent e) {
        RegexFilters.INSTANCE.registerMessage(MessageSetExpression.MessageHandler.class, MessageSetExpression.class, 0, Side.SERVER);
        RegexFilters.INSTANCE.registerMessage(MessageUpdate.MessageHandler.class, MessageUpdate.class, 1, Side.CLIENT);
        RegexFilters.INSTANCE.registerMessage(MessageRequestUpdate.MessageHandler.class, MessageRequestUpdate.class, 2, Side.SERVER);
    }

    public void postInit(FMLPostInitializationEvent e) {
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(new RegexBlock());
        GameRegistry.registerTileEntity(RegexTileEntity.class, new ResourceLocation(RegexFilters.MODID+"_regexfilter"));
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new ItemBlock(RegexFilters.regexBlock).setRegistryName(RegexFilters.regexBlock.getRegistryName()));
    }
}
