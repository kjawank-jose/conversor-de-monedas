import com.google.gson.JsonObject;

public class ResultsDisplay {

    public static void displayRates(JsonObject rates, String[] targetCurrencies) {
        JsonObject conversionRates = rates.getAsJsonObject("conversion_rates");
        for (String targetCurrency : targetCurrencies) {
            if (conversionRates.has(targetCurrency)) {
                double rate = conversionRates.get(targetCurrency).getAsDouble();
                System.out.println(targetCurrency + ": " + rate);
            } else {
                System.out.println("No se encontró la tasa para " + targetCurrency);
            }
        }
    }
}
