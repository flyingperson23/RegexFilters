package flyingperson.regexfilters;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.logging.log4j.Logger;

@Mod(modid = RegexFilters.MODID, name = RegexFilters.NAME, version = RegexFilters.VERSION, dependencies = "required:codechickenlib")
public class RegexFilters
{
    public static final String MODID = "regexfilters";
    public static final String NAME = "Regex Filters";
    public static final String VERSION = "1.2";

    public static final String DUMMY_STRING = "DUMMY_!@#$%^&*()&ydvjr4OHEb6oKCuFJp1AD9xBCsInI7umBM60njy8Wq8tjhWsnb5ebeP0ljrM1p3Wk0mqtQ6Vhg0FyxE7GXZiwGtk4WivBoUGNpj2";

    @SidedProxy(clientSide = "flyingperson.regexfilters.ClientProxy", serverSide = "flyingperson.regexfilters.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance
    public static RegexFilters instance;

    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("regexfilters");

    @GameRegistry.ObjectHolder("regexfilters:regexfilter")
    public static RegexBlock regexBlock;

    public static Logger logger;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        MinecraftForge.EVENT_BUS.register(this);
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GUIProxy());
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
