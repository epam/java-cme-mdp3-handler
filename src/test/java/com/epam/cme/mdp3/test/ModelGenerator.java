package com.epam.cme.mdp3.test;
import uk.co.real_logic.sbe.SbeTool;
import uk.co.real_logic.sbe.ir.Ir;
import uk.co.real_logic.sbe.xml.IrGenerator;
import uk.co.real_logic.sbe.xml.MessageSchema;
import uk.co.real_logic.sbe.xml.ParserOptions;

import java.io.InputStream;
import java.lang.reflect.Field;

import static uk.co.real_logic.sbe.xml.XmlSchemaParser.parse;

public class ModelGenerator {
    public static final String TEMPLATE_NAME = "templates_FixBinary.xml";
    public static final String MODEL_OUTPUT_DIR = ".\\src\\test\\java";
    public static final String MODEL_PACKAGE = "com.epam.cme.mdp3.test.gen";

    public static void main(String[] args) throws Exception {
        ClassLoader classLoader = ModelGenerator.class.getClassLoader();
        System.setProperty(SbeTool.KEYWORD_APPEND_TOKEN, "1");
        try(InputStream templateStream = classLoader.getResourceAsStream(TEMPLATE_NAME)){
            final ParserOptions options = ParserOptions.builder().stopOnError(true).build();
            final MessageSchema schema = parse(templateStream, options);
            Field packageNameField = schema.getClass().getDeclaredField("packageName");
            packageNameField.setAccessible(true);
            packageNameField.set(schema, MODEL_PACKAGE);
            final IrGenerator irg = new IrGenerator();
            Ir ir = irg.generate(schema);
            SbeTool.generate(ir, MODEL_OUTPUT_DIR, "Java");
        }
    }
}
