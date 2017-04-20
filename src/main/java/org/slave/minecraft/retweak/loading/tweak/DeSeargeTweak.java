package org.slave.minecraft.retweak.loading.tweak;

import org.objectweb.asm.tree.ClassNode;
import org.slave.minecraft.retweak.loading.capsule.versions.GameVersion;

/**
 * Created by Master on 5/15/2016 at 8:27 AM.
 *
 * @author Master
 */
public final class DeSeargeTweak implements Tweak {

    private static DeSeargeTweak instance;

    /*
    private final SRG srg;

    private DeSeargeTweak(final SRG srg) {
        this.srg = srg;
    }
    */

    private DeSeargeTweak() {
    }

    @Override
    public String getName() {
        return "DeSearge";
    }

    @Override
    public void tweak(final ClassNode classNode, final GameVersion gameVersion) throws TweakException {
        /*
        if (srg == null) {
            throw new NullPointerException(
                    "SRG is null for DeSearge tweak! DeSearge: " + toString()
            );
        }

        final Mapping mapping = Mappings.INSTANCE.getMapping(
                gameVersion
        );

        if (classNode.methods != null) {
            for(MethodNode methodNode : classNode.methods) {
                for(int i = 0; i < methodNode.instructions.size(); ++i) {
                    AbstractInsnNode abstractInsnNode = methodNode.instructions.get(i);
                    if (abstractInsnNode instanceof FieldInsnNode) {
                        FieldInsnNode fieldInsnNode = (FieldInsnNode)abstractInsnNode;
                        if (fieldInsnNode.name.startsWith("field_")) {
                            if (mapping != null && mapping.ignoreField(fieldInsnNode.getOpcode(), fieldInsnNode.owner, fieldInsnNode.name, fieldInsnNode.desc)) continue;

                            String[] entry = getFieldEntry(
                                    fieldInsnNode.owner,
                                    fieldInsnNode.name
                            );
                            if (entry != null) {
                                if (ReTweakResources.DEBUG_MESSAGES) {
                                    ReTweakResources.RETWEAK_LOGGER.info(
                                            "DeSearged name of field insn ( {} ) from method \"{}\", at index {}, from class \"{}\", from \"{}\" to \"{}\"",
                                            ASMHelper.toString(fieldInsnNode),
                                            ASMHelper.toString(methodNode),
                                            i,
                                            classNode.name,
                                            fieldInsnNode.name,
                                            entry[3]
                                    );
                                }
                                fieldInsnNode.name = entry[3];
                            } else {
                                ReTweakResources.RETWEAK_LOGGER.warn(
                                        "Found no entry for field insn \"{}\" at index {}, from method \"{}\", from class \"{}\"!",
                                        ASMHelper.toString(fieldInsnNode),
                                        i,
                                        ASMHelper.toString(methodNode),
                                        classNode.name
                                );
                            }
                        }
                    } else if (abstractInsnNode instanceof MethodInsnNode) {
                        MethodInsnNode methodInsnNode = (MethodInsnNode)abstractInsnNode;
                        if (methodInsnNode.name.startsWith("func_")) {
                            if (mapping != null && mapping.ignoreMethod(methodInsnNode.getOpcode(), methodInsnNode.owner, methodInsnNode.name, methodInsnNode.desc)) continue;

                            String[] entry = getMethodEntry(
                                    methodInsnNode.owner,
                                    methodInsnNode.name,
                                    methodInsnNode.desc
                            );
                            if (entry != null) {
                                if (ReTweakResources.DEBUG_MESSAGES) {
                                    ReTweakResources.RETWEAK_LOGGER.info(
                                            "DeSearged name of method insn ( {} ) at index {}, from method \"{}\", from class \"{}\", from \"{}\" to \"{}\"",
                                            ASMHelper.toString(methodInsnNode),
                                            i,
                                            ASMHelper.toString(methodNode),
                                            classNode.name,
                                            methodInsnNode.name,
                                            entry[4]
                                    );
                                }
                                methodInsnNode.name = entry[4];
                            } else {
                                ReTweakResources.RETWEAK_LOGGER.warn(
                                        "Found no entry for method insn \"{}\" at index {}, from method \"{}\", from class \"{}\"!",
                                        ASMHelper.toString(methodInsnNode),
                                        i,
                                        ASMHelper.toString(methodNode),
                                        classNode.name
                                );
                            }
                        }
                    }
                }

                if (methodNode.name.startsWith("func_")) {
                    if (mapping != null && mapping.ignoreMethod(methodNode.access, null, methodNode.name, methodNode.desc)) continue;

                    String[] methodEntry = getMethodEntry(
                            classNode.name,
                            methodNode.name,
                            methodNode.desc
                    );
                    if (methodEntry != null) {
                        methodNode.name = methodEntry[4];
                        methodNode.desc = methodEntry[5];
                    } else {
                        if (ReTweakResources.DEBUG_MESSAGES) {
                            ReTweakResources.RETWEAK_LOGGER.warn(
                                    "Found no entry for method \"{}\" from class \"{}\"!",
                                    ASMHelper.toString(methodNode),
                                    classNode.name
                            );
                        }
                    }
                }
            }
        }

        if (classNode.fields != null) {
            for(FieldNode fieldNode : classNode.fields) {
                if (fieldNode.name.startsWith("field_")) {
                    if (mapping != null && mapping.ignoreMethod(fieldNode.access, null, fieldNode.name, fieldNode.desc)) continue;

                    String[] fieldEntry = getFieldEntry(
                            classNode.name,
                            fieldNode.name
                    );
                    if (fieldEntry != null) {
                        fieldNode.name = fieldEntry[4];
                        fieldNode.desc = fieldEntry[5];
                    } else {
                        if (ReTweakResources.DEBUG_MESSAGES) {
                            ReTweakResources.RETWEAK_LOGGER.warn(
                                    "Found no entry for field \"{}\" from class \"{}\"!",
                                    ASMHelper.toString(fieldNode),
                                    classNode.name
                            );
                        }
                    }
                }
            }
        }
        */
    }

    @Override
    public int getWantedSortIndex() {
        return Integer.MAX_VALUE;//Last tweak
    }

    /*
    private String[] getFieldEntry(final String owner, final String name) {
        if (StringHelper.isNullOrEmpty(owner) || StringHelper.isNullOrEmpty(name)) return null;

        String[] entry = srg.getFieldEntry(
                owner,
                name
        );
        if (entry == null) {
            for(String[] iteratingEntry : srg.getSRGEntries(SRG_Type.FD).getEntries()) {
                if (iteratingEntry[1].equals(name)) {
                    entry = iteratingEntry;
                    break;
                }
            }
        }
        return entry;
    }

    private String[] getMethodEntry(final String owner, final String name, final String desc) {
        if (StringHelper.isNullOrEmpty(owner) || StringHelper.isNullOrEmpty(name) || StringHelper.isNullOrEmpty(desc)) return null;

        String[] entry = srg.getMethodEntry(
                owner,
                name,
                desc
        );
        if (entry == null) {
            for(String[] iteratingEntry : srg.getSRGEntries(SRG_Type.MD).getEntries()) {
                if (iteratingEntry[1].equals(name) && iteratingEntry[2].equals(desc)) {
                    entry = iteratingEntry;
                    break;
                }
            }
        }
        return entry;
    }
    */

    public static Tweak getInstance() {
//        if (DeSeargeTweak.instance == null) DeSeargeTweak.instance = new DeSeargeTweak(ReTweakDeobfuscation.INSTANCE.getLatestSRG());
        return DeSeargeTweak.instance;
    }

}
