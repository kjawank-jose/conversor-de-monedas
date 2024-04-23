import com.google.gson.JsonObject;
import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;

public class CurrencyConverter {

    private static final Set<String> AVAILABLE_CURRENCIES = new HashSet<>(Set.of(
            "USD", "EUR", "GBP", "JPY", "AUD", "CAD", "CHF", "CNY", "SEK", "NZD",
            "MXN", "SGD", "HKD", "NOK", "KRW", "TRY", "INR", "RUB", "BRL", "ZAR",
            "PEN"
    ));

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String baseCurrency;
        do {
            System.out.println("\n********************************************");

            System.out.println("\nIngrese la moneda a convertir: " +
                    "\n USD, EUR, GBP, JPY, AUD, CAD, CHF, CNY, SEK, NZD, MXN," +
                    "\n SGD, HKD, NOK, KRW, TRY, INR, RUB, BRL, ZAR, PEN):");
            baseCurrency = scanner.nextLine().toUpperCase();
            if (!AVAILABLE_CURRENCIES.contains(baseCurrency)) {
                System.out.println("Moneda ingresada no válida. Por favor, ingrese una moneda de la lista.");
            }
        } while (!AVAILABLE_CURRENCIES.contains(baseCurrency));

        System.out.println("--------------------------------------------");
        System.out.println("Ha seleccionado la moneda " + baseCurrency);
        System.out.println("--------------------------------------------");

        System.out.println("Elija la opción que desea convertir:");

        //Opción de monedas disponibles en cinco columnas
        int totalCurrencies = AVAILABLE_CURRENCIES.size() - 1; // Excluye la moneda base
        int currenciesPerColumn = (totalCurrencies + 4) / 5; // Redondea hacia arriba

        int columnCounter = 0;
        for (String currency : AVAILABLE_CURRENCIES) {
            if (!currency.equals(baseCurrency)) {
                System.out.printf("%-3d. %-4s a %-4s   ", columnCounter + 1, baseCurrency, currency);
                columnCounter++;
                if (columnCounter % currenciesPerColumn == 0) {
                    System.out.println();
                }
            }
        }

        System.out.println("\nIngrese el número de la opción de conversión:");
        int selectedOption = scanner.nextInt();
        if (selectedOption < 1 || selectedOption > totalCurrencies) {
            System.out.println("Opción no válida. Por favor, ingrese un número válido.");
            return;
        }

        String targetCurrency = null;
        int currentIndex = 1;
        for (String currency : AVAILABLE_CURRENCIES) {
            if (!currency.equals(baseCurrency)) {
                if (currentIndex == selectedOption) {
                    targetCurrency = currency;
                    break;
                }
                currentIndex++;
            }
        }
        System.out.println("--------------------------------------------");
        System.out.println("Ha seleccionado convertir de " + baseCurrency + " a " + targetCurrency);
        System.out.println("--------------------------------------------");

        System.out.println("Ingrese la cantidad a convertir:");
        double amount = scanner.nextDouble();

        ExchangeService exchangeService = new ExchangeService();
        JsonObject rates = exchangeService.getRates(baseCurrency);

        if (rates != null) {
            JsonObject conversionRates = rates.getAsJsonObject("conversion_rates");
            if (conversionRates.has(targetCurrency)) {
                double rate = conversionRates.get(targetCurrency).getAsDouble();
                double convertedAmount = rate * amount;
                System.out.println("\n++++++++++++++++++++++++++++++++++++++++++++");
                System.out.printf("La cantidad convertida es: %.2f %s\n", convertedAmount, targetCurrency);
                System.out.println("++++++++++++++++++++++++++++++++++++++++++++");

            } else {
                System.out.println("No se encontró la tasa para " + targetCurrency);
            }
        } else {
            System.out.println("Error al obtener las tasas de cambio.");
        }

        scanner.close();

        System.out.println("\nGracias por usar nuestros servicios");
    }
}
