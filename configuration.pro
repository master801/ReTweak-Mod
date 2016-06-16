-injars build\libs\ReTweak-Mod-1.0.2.jar
-outjars build\libs\ReTweak-Mod-1.0.2-obf.jar


-skipnonpubliclibraryclasses
-target 1.7
-forceprocessing
-dontshrink
-optimizationpasses 4
-mergeinterfacesaggressively
-printmapping 'names.mapping'
-overloadaggressively
-useuniqueclassmembernames
-flattenpackagehierarchy ''
-repackageclasses ''
-keepattributes LineNumberTable,LocalVariable*Table,*Annotation*,Synthetic,EnclosingMethod
-renamesourcefileattribute this_is_obfuscated___you_re_not_meant_to_look_at_this
-verbose
-dontnote
-dontwarn


-keep,allowshrinking,allowoptimization !interface public  org.slave.minecraft.retweak.loading.ReTweakLoader {
    !private !protected !volatile !transient !synthetic public static final org.slave.minecraft.retweak.loading.ReTweakLoader INSTANCE;
    !private !protected !static !synchronized !bridge !varargs !native !abstract !strictfp !synthetic public void loadMods();
}

-keep,allowshrinking,allowoptimization !@!interface public  org.slave.minecraft.retweak.loading.ReTweakStateHandler {
    !private !protected !synchronized !bridge !varargs !native !abstract !strictfp !synthetic public static void step(cpw.mods.fml.common.LoadController,cpw.mods.fml.common.LoaderState,cpw.mods.fml.common.LoaderState);
}

-keep,allowshrinking,allowoptimization !@!interface public  org.slave.minecraft.retweak.client.config.elements.GuiScreenConfigReTweakConfig {
    !private !protected !static !synchronized !bridge !varargs !native !abstract !strictfp !synthetic public *** getConfigElements();
}

-keep,allowshrinking,allowoptimization !@!interface public  org.slave.minecraft.retweak.client.config.ReTweakGUIFactory {
    !private !protected !static !synchronized !bridge !varargs !native !abstract !strictfp !synthetic public <methods>;
}

-keep,allowshrinking,allowoptimization !@!interface public  org.slave.minecraft.retweak.resources.ReTweakMetadata {
    !private !protected !static !synchronized !bridge !varargs !native !strictfp !synthetic public java.lang.String[] getAuthorList();
    !private !protected !static !synchronized !bridge !varargs !abstract !strictfp !synthetic public boolean isAutoGenerated();
    !private !protected !static !synchronized !bridge !varargs !native !abstract !strictfp !synthetic public cpw.mods.fml.common.ModContainer[] getChildMods();
    !private !protected !static !synchronized !bridge !varargs !native !abstract !strictfp !synthetic public java.lang.String getCredits();
    !private !protected !static !synchronized !bridge !varargs !native !abstract !strictfp !synthetic public java.lang.String getDescription();
    !private !protected !static !synchronized !bridge !varargs !native !abstract !strictfp !synthetic public java.lang.String getLogo();
    !private !protected !static !synchronized !bridge !varargs !native !abstract !strictfp !synthetic public java.lang.String getModID();
    !private !protected !static !synchronized !bridge !varargs !native !abstract !strictfp !synthetic public java.lang.String getModName();
    !private !protected !static !synchronized !bridge !varargs !native !abstract !strictfp !synthetic public java.lang.String getParentMod();
    !private !protected !static !synchronized !bridge !varargs !native !abstract !strictfp !synthetic public java.lang.String[] getScreenShots();
    !private !protected !static !synchronized !bridge !varargs !native !abstract !strictfp !synthetic public java.lang.String getUpdateURL();
    !private !protected !static !synchronized !bridge !varargs !native !abstract !strictfp !synthetic public java.lang.String getURL();
    !private !protected !static !synchronized !bridge !varargs !native !abstract !strictfp !synthetic public java.lang.String getModVersion();
}

-keep,allowshrinking,allowoptimization !@!interface public  org.slave.minecraft.retweak.asm.ReTweakASM {
    !private !protected !static !synchronized !bridge !varargs !native !abstract !strictfp !synthetic public <methods>;
}

-keep,allowshrinking,allowoptimization !@!interface public  org.slave.minecraft.retweak.asm.ReTweakSetup {
    !private !protected !static !synchronized !bridge !varargs !native !abstract !strictfp !synthetic public <methods>;
}

# Transformer
-keepclassmembers,allowshrinking,allowoptimization !@!interface public  * {
    !public !private !static !synchronized !bridge !varargs !native !abstract !strictfp !synthetic protected boolean transformClass(org.objectweb.asm.tree.ClassNode);
    !public !private !static !synchronized !bridge !varargs !native !abstract !strictfp !synthetic protected boolean writeClassFile(...);
    !public !private !static !synchronized !bridge !varargs !native !abstract !strictfp !synthetic protected boolean writeASMFile(...);
    !public !private !static !synchronized !bridge !varargs !native !abstract !strictfp !synthetic protected java.lang.String getClassName(boolean);
}

# serialVersionUID
-keepclassmembers,allowshrinking,allowoptimization !@!interface  * {
    !public !protected !volatile !transient !synthetic private static final long serialVersionUID;
}

-keep,allowshrinking,allowoptimization !@!interface public  org.slave.minecraft.retweak.client.screens.GuiScreenReTweakMods {
    <fields>;
    <methods>;
}

# Also keep - Enumerations. Keep the special static methods that are required in
# enumeration classes.
-keepclassmembers enum  * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# Also keep - Serialization code. Keep all fields and methods that are used for
# serialization.
-keepclassmembers class * extends java.io.Serializable {
    static final long serialVersionUID;
    static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}