package com.matt.forgehax.asm;

//import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;


@Deprecated
public class ForgeHaxCoreMod implements /*IFMLLoadingPlugin,*/ ASMCommon {

    static {
        //AsmLibApi.init();
        //AsmLibApi.registerConfig("asmlib.forgehax.config.json");
    }

    /*@Override
    public String[] getASMTransformerClass() {
        return new String[0];//new String[] {ForgeHaxTransformer.class.getName()};
    }

    @Override
    public String getModContainerClass() {
        return null;
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
        if(data.containsKey("runtimeDeobfuscationEnabled")) {
            try {
                Boolean isObfuscated = (Boolean)data.get("runtimeDeobfuscationEnabled");
                if(isObfuscated) {
                    RuntimeState.markDefaultAsObfuscated();
                } else {
                    RuntimeState.markDefaultAsNormal();
                }
                //FileDumper.dumpAllFiles();
            } catch (Exception e) {
                LOGGER.error("Failed to obtain runtimeDeobfuscationEnabled: " + e.getMessage());
                ASMStackLogger.printStackTrace(e);
            }
        }
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }*/
}
