package org.slave.tool.remapper;

import com.github.pwittchen.kirai.library.Kirai;
import com.google.common.base.Joiner;
import org.slave.lib.helpers.ArrayHelper;
import org.slave.lib.helpers.CSVHelper;
import org.slave.lib.helpers.StringHelper;
import org.slave.lib.resources.CSV;
import org.slave.lib.resources.Index;
import org.slave.tool.remapper.Parser.Type;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by Master801 on 4/12/2016 at 7:42 PM.
 *
 * @author Master801
 */
public final class Main {

    private Main() {
        throw new IllegalStateException();
    }

    public static void main(final String[] arguments) {
        boolean help = false;
        String input = null;
        String output = null;

        if (ArrayHelper.isNullOrEmpty(arguments)) help = true;

        if (help) {
            Main.help();
            return;
        }

        int skip = -1;
        for(int i = 0; i < arguments.length; ++i) {
            if (i == skip) continue;
            String argument = arguments[i];
            if (argument.equalsIgnoreCase("--input") || argument.equalsIgnoreCase("-i")) input = arguments[skip = i + 1];
            if (argument.equalsIgnoreCase("--output") || argument.equalsIgnoreCase("-o")) output = arguments[skip = i + 1];
            if (argument.equalsIgnoreCase("--help") || argument.equalsIgnoreCase("-h")) help = true;
        }

        if (help) {
            Main.help();
            return;
        }
        if (StringHelper.isNullOrEmpty(input)) throw new IllegalArgumentException("Input is null!");
        if (StringHelper.isNullOrEmpty(output)) throw new IllegalArgumentException("Output is null!");

        try {
            //noinspection ConstantConditions
            Main.convert(new File(input), new File(output));
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void help() {
        final String tab = "    ";

        String helpMessage = System.lineSeparator() + "Arguments:" + System.lineSeparator();

        helpMessage += StringHelper.joinRepeatedly(
                System.lineSeparator(),
                tab,
                Index.FIRST,
                new String[] {
                        "--input, -i" + System.lineSeparator() + StringHelper.joinRepeatedly(
                                System.lineSeparator(),
                                tab + tab,
                                Index.FIRST,
                                new String[] {
                                        "The \"conf\" directory of MCP." + System.lineSeparator()
                                }
                        ),
                        "--output, -o" + System.lineSeparator() + StringHelper.joinRepeatedly(
                                System.lineSeparator(),
                                tab + tab,
                                Index.FIRST,
                                new String[] {
                                        "The MCP mappings file (obfuscated to searge) in LZMA format, for FML/Forge usage.)" + System.lineSeparator()
                                }
                        ),
                        "--help, -h" + System.lineSeparator() + StringHelper.joinRepeatedly(
                                System.lineSeparator(),
                                tab + tab,
                                Index.FIRST,
                                new String[] {
                                        "Shows this help message."
                                }
                        )
                }
        );

        System.out.println(helpMessage);
    }

    private static void convert(File input, File output) throws FileNotFoundException {
        if (!input.exists()) {
            throw new FileNotFoundException(
                    Kirai.from(
                            "Directory \"{input_path}\" does not exist!"
                    ).put(
                            "input_path",
                            input.getPath()
                    ).format().toString()
            );
        }
        if (!input.isDirectory()) {
            System.out.println(
                    Kirai.from(
                            "Input \"{input_path}\" is not a directory!"
                    ).put(
                            "input_path",
                            input.getPath()
                    ).format().toString()
            );
        }

        Map<Type, List<String[]>> parsedSRG;
        File joinedSRG = new File(
                input,
                "joined.srg"
        );
        if (!joinedSRG.exists()) throw new FileNotFoundException(joinedSRG.getPath());
        try {
            FileInputStream inputFileIS = new FileInputStream(joinedSRG);
            parsedSRG = Parser.INSTANCE.parse(inputFileIS);
            inputFileIS.close();
        } catch(IOException e) {
            e.printStackTrace();
            return;
        }

        File csvFile = new File(
                input,
                "packages.csv"
        );
        if (!csvFile.exists()) throw new FileNotFoundException(csvFile.getPath());
        CSV csv;
        try {
            InputStream csvIS = new FileInputStream(csvFile);
            csv = CSVHelper.readCSV(csvIS);
            csvIS.close();
        } catch(IOException e) {
            e.printStackTrace();
            return;
        }

        String packageName = null;
        for(Type type : parsedSRG.keySet()) {
            List<String[]> strings = parsedSRG.get(type);
            for(String[] string : strings) {
                if (type == Type.PK && string[0].equals(".") && packageName == null) {
                    packageName = string[1] + "/";
                    continue;
                }

                if (packageName != null && type != Type.PK && type != Type.CL) {
                    final int x = type.split - 1;
                    int index = string[x].lastIndexOf(packageName);
                    if (index != -1) string[x] = string[x].substring(packageName.length());

                    if (type == Type.MD) {
                        System.out.println("");
                    }

                    for(String[] a : csv.getValues()) {
                        String b = string[x].substring(0, string[x].indexOf('/'));
                        if (b.equals(a[0])) {
                            string[x] = a[1] + "/" + string[x];
                            break;
                        }
                    }
                }

                System.out.println(Joiner.on(' ').join(string));
            }
        }
    }

}
