package org.slave.tool.remapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Master801 on 4/12/2016 at 8:37 PM.
 *
 * @author Master801
 */
public final class Parser {

    public static final Parser INSTANCE = new Parser();

    private Parser() {
    }

    Map<Type, List<String[]>> parse(InputStream inputStream) throws IOException {
        Map<Type, List<String[]>> map = new HashMap<>();
        for(Type type : Type.values()) map.put(type, new ArrayList<String[]>());

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String currentLine;
        while((currentLine = bufferedReader.readLine()) != null) {
            final Type type = Type.get(
                    currentLine.substring(
                            0,
                            4
                    )
            );
            if (type == null) {
                System.out.println("Invalid line:");
                System.out.println(currentLine);
                continue;
            }

            String[] parts = currentLine.substring(type.header.length()).split(
                    " ",
                    type.split
            );

            map.get(type).add(parts);
        }

        bufferedReader.close();
        inputStreamReader.close();

        return map;
    }

    public enum Type {

        PK(
                "PK: ",
                2
        ),

        CL(
                "CL: ",
                2
        ),

        FD(
                "FD: ",
                2
        ),

        MD(
                "MD: ",
                4
        );

        final String header;
        final int split;

        Type(final String header, final int split) {
            this.header = header;
            this.split = split;
        }

        static Type get(String header) {
            for(Type type : Type.values()) {
                if (type.header.equals(header)) return type;
            }
            return null;
        }

    }

}
