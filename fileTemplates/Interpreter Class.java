package ${PACKAGE_NAME};

import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter.annotation.Deobfuscated;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter.annotation.Obfuscated;
import org.slave.minecraft.retweak.loading.tweak.compilation.interpreter.annotation._class.Package;

/**
 * Created by Master on 4/20/2017 at 7:27 AM.
 *
 * @author Master
 */
@Obfuscated(
        name = "${NAME}",
        _package = @Package("${Obfuscated_Package}")
)
@Deobfuscated(
        name = "${Deobfuscated_Name}",
        _package = @Package("${Deobfuscated_Package}")
)
public interface ${NAME} {
}
