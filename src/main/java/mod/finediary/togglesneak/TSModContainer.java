package mod.finediary.togglesneak;

import java.io.IOException;
import java.util.Arrays;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class TSModContainer extends DummyModContainer{
	public TSModContainer(){
		super(new ModMetadata());
		ModMetadata meta = getMetadata();
		meta.modId       = Version.MODID;
		meta.name        = "Toggle Sneak Mod";
		meta.version     = Version.VERSION;
		meta.credits     = "Using ASM";
		meta.authorList  = Arrays.asList("fine_diary");
		meta.description = "30-minute quality";
		meta.url         = "http://twitter.com/finediry";
		meta.screenshots = new String[0];
		//meta.logoFile    = "/logo.png";
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller){
		bus.register(this);
		return true;
	}

    @Subscribe
    public void modPreinitialization(FMLPreInitializationEvent evt) throws IOException{
    }

	public class Version{
		public static final String MODID = "togglesneak";
		public static final String VERSION = "1";
	}
}
