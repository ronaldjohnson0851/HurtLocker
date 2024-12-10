import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RawDataParserTest {

    @Test
    void testNormalizeValue() {
        RawDataParser parser = new RawDataParser();


        assertEquals("Milk", parser.normalizeValue("MILK"));
        assertEquals("Milk", parser.normalizeValue("MiLK"));
        assertEquals("Cookies", parser.normalizeValue("c00Kies"));
        assertEquals("Cookies", parser.normalizeValue("COokIeS"));
        assertEquals("3.23", parser.normalizeValue("3.23"));
    }

    @Test
    void testParseSingleRecord() throws Exception {
        RawDataParser parser = new RawDataParser();

        String record = "naMe:Milk;price:3.23;type:Food;expiration:1/25/2016";
        Map<String, String> parsedData = parser.parseRecord(record);

        assertEquals("Milk", parsedData.get("name"));
        assertEquals("3.23", parsedData.get("price"));
        assertEquals("Foo", parsedData.get("type"));
        assertEquals("1/25/2016", parsedData.get("expiration"));
    }

    @Test
    void testParseAndCombine() {
        RawDataParser parser = new RawDataParser();

        String rawData = "naMe:Milk;price:3.23;type:Food;expiration:1/25/2016##" +
                "naMe:MiLK;price:3.23;type:Food;expiration:1/17/2016##" +
                "naMe:Cookies;price:2.25;type:Food;expiration:1/25/2016##" +
                "naMe:MiLK;price:1.23;type:Food;expiration:1/25/2016##" +
                "naMe:;price:3.23;type:Food;expiration:1/25/2016";

        parser.parseAndCombine(rawData);

        Map<String, Map<String, Integer>> combinedData = parser.getCombinedData();


        assertTrue(combinedData.containsKey("Milk"));
        assertEquals(3, combinedData.get("Milk").values().stream().mapToInt(Integer::intValue).sum());
        assertEquals(2, combinedData.get("Milk").get("3.23"));
        assertEquals(1, combinedData.get("Milk").get("1.23"));


        assertTrue(combinedData.containsKey("Cookies"));
        assertEquals(1, combinedData.get("Cookies").values().stream().mapToInt(Integer::intValue).sum());
        assertEquals(1, combinedData.get("Cookies").get("2.25"));


        assertEquals(1, parser.getErrorCount());
    }

    @Test
    void testErrorHandling() {
        RawDataParser parser = new RawDataParser();

        String rawData = "naMe:Milk;price:3.23;type:Food;expiration:1/25/2016##" +
                "naMe:;price:;type:Food;expiration:##" +
                "invalidrecord##";

        parser.parseAndCombine(rawData);


        assertEquals(2, parser.getErrorCount());
    }
}

