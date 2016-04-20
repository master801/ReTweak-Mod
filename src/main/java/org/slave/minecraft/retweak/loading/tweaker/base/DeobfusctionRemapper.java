package org.slave.minecraft.retweak.loading.tweaker.base;

import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.signature.SignatureVisitor;
import org.slave.tool.remapper.SRG;

/**
 * Created by Master801 on 4/19/2016 at 6:18 PM.
 *
 * @author Master801
 */
public final class DeobfusctionRemapper extends Remapper {

    private final SRG srg;

    public DeobfusctionRemapper(SRG srg) {
        this.srg = srg;
    }

    @Override
    public String map(final String typeName) {
        //TODO
        return super.map(typeName);
    }

    @Override
    public String mapFieldName(final String owner, final String name, final String desc) {
        //TODO
        return super.mapFieldName(owner, name, desc);
    }

    @Override
    public String mapMethodName(final String owner, final String name, final String desc) {
        //TODO
        return super.mapMethodName(owner, name, desc);
    }

    @Override
    public String mapDesc(final String desc) {
        //TODO
        return super.mapDesc(desc);
    }

    @Override
    public String mapType(final String type) {
        //TODO
        return super.mapType(type);
    }

    @Override
    public String[] mapTypes(final String[] types) {
        //TODO
        return super.mapTypes(types);
    }

    @Override
    public String mapMethodDesc(final String desc) {
        //TODO
        return super.mapMethodDesc(desc);
    }

    @Override
    public Object mapValue(final Object value) {
        //TODO
        return super.mapValue(value);
    }

    @Override
    public String mapSignature(final String signature, final boolean typeSignature) {
        //TODO
        return super.mapSignature(signature, typeSignature);
    }

    @Override
    protected SignatureVisitor createRemappingSignatureAdapter(final SignatureVisitor v) {
        //TODO
        return super.createRemappingSignatureAdapter(v);
    }

    @Override
    public String mapInvokeDynamicMethodName(final String name, final String desc) {
        //TODO
        return super.mapInvokeDynamicMethodName(name, desc);
    }

}
