import java.util.*;
import java.util.regex.*;

public class RawDataParser {
    private static final String RECORD_SEPARATOR = "##";
    private static final String KEY_VALUE_PATTERN = "(\\w+):([^;#@%*!^]*)";

    private int errorCount = 0;
    private Map<String, Map<String, Integer>> combinedData = new LinkedHashMap<>();


    public void parseAndCombine(String rawData) {

        Pattern recordPattern = Pattern.compile("([^#]+)##?");
        Matcher recordMatcher = recordPattern.matcher(rawData);


//        while (recordMatcher.find()) {
//            String record = recordMatcher.group(1).trim();
//            try {
//                Map<String, String> productData = parseRecord(record);
//                combine(productData);
//            } catch (Exception e) {
//                errorCount++;
//            }
//        }
    }


    public Map<String, String> parseRecord(String record) throws Exception {
        Map<String, String> productData = new HashMap<>();
        Matcher matcher = Pattern.compile(KEY_VALUE_PATTERN).matcher(record);

        while (matcher.find()) {
            String key = matcher.group(1).toLowerCase();
            String value = matcher.group(2).trim();

            if (value.isEmpty() && !key.equals("name")) {
                throw new Exception("Missing value for key: " + key);
            }


            productData.put(normalizeKey(key), normalizeValue(value));
        }


        if (productData.isEmpty()) {
            throw new Exception("Empty record");
        }

        return productData;
    }


    private void combine(Map<String, String> productData) {
        String name = productData.getOrDefault("name", "unknown");
        String price = productData.getOrDefault("price", "unknown");


        combinedData.putIfAbsent(name, new LinkedHashMap<>());
        Map<String, Integer> priceCounts = combinedData.get(name);
        priceCounts.put(price, priceCounts.getOrDefault(price, 0) + 1);
    }

    private String normalizeKey(String key) {
        return key.toLowerCase();
    }

    public String normalizeValue(String value) {
        if (Pattern.compile("(?i)c[o0]{2}kies").matcher(value).matches()) {
            return "Cookies";
        }
        if (value.matches("(?i)[a-z]+")) {

            return value.substring(0, 1).toUpperCase() + value.substring(1).toLowerCase();
        }
        return value;
    }


    public Map<String, Map<String, Integer>> getCombinedData() {
        return combinedData;
    }


    public int getErrorCount() {
        return errorCount;
    }


    public List<Map<String, String>> parse(String rawData) {
        return null;
    }
}

