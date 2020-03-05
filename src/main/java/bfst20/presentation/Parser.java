package bfst20.presentation;

import java.io.Reader;
import java.io.StringReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import static javax.xml.stream.XMLStreamConstants.*;

public class Parser {
    private Parser() {
    }

    public static void parseString(String string) throws XMLStreamException {
        Reader stringReader = new StringReader(string);
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader reader = factory.createXMLStreamReader(stringReader);

        while(reader.hasNext()){
            reader.next();

            switch(reader.getEventType()){
                case START_ELEMENT:
                    
                    break;
                case END_ELEMENT:
                    
                    break;
            }
        }
    }
}