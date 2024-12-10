import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {


        Main main = new Main();
        String rawData = main.readRawDataToString();


        RawDataParser parser = new RawDataParser();
        parser.parseAndCombine(rawData);


        printFormattedData(parser.getCombinedData(), parser.getErrorCount());
    }


    public String readRawDataToString() throws Exception {
        ClassLoader classLoader = getClass().getClassLoader();
        return org.apache.commons.io.IOUtils.toString(classLoader.getResourceAsStream("RawData.txt"));
    }


    private static void printFormattedData(Map<String, Map<String, Integer>> combinedData, int errorCount) {
        for (String name : combinedData.keySet()) {
            Map<String, Integer> priceCounts = combinedData.get(name);
            int totalOccurrences = priceCounts.values().stream().mapToInt(Integer::intValue).sum();

            System.out.println("name:   " + name + " \t\tseen: " + totalOccurrences + " times");
            System.out.println("=============  \t\t =============");

            for (String price : priceCounts.keySet()) {
                System.out.printf("Price:   %-6s\t\tseen: %d %s\n", price, priceCounts.get(price),
                        (priceCounts.get(price) == 1 ? "time" : "times"));
                System.out.println("-------------\t\t -------------");
            }
            System.out.println();
        }


        System.out.println("Errors \t\t\t seen: " + errorCount + " times");
    }
}

