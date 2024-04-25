import com.google.gson.JsonObject;
import java.util.*;

public class CurrencyConverter {

    // Monedas disponibles en este proyecto

    private static final Set<String> AVAILABLE_CURRENCIES = new HashSet<>(Set.of(
            "USD", "EUR", "GBP", "JPY", "AUD", "CAD", "CHF", "CNY", "SEK", "NZD",
            "MXN", "SGD", "HKD", "NOK", "KRW", "TRY", "INR", "RUB", "BRL", "ZAR",
            "PEN"
    ));

    // Nombres de las monedas

    private static final Map<String, String> CURRENCY_NAMES = new HashMap<>();

    static {
        CURRENCY_NAMES.put("USD", "Dólares Americanos");
        CURRENCY_NAMES.put("EUR", "Euros");
        CURRENCY_NAMES.put("GBP", "Libras Esterlinas");
        CURRENCY_NAMES.put("JPY", "Yenes");
        CURRENCY_NAMES.put("AUD", "Dólares Australianos");
        CURRENCY_NAMES.put("CAD", "Dólares Canadienses");
        CURRENCY_NAMES.put("CHF", "Francos Suizos");
        CURRENCY_NAMES.put("CNY", "Yuanes Chinos");
        CURRENCY_NAMES.put("SEK", "Coronas Suecas");
        CURRENCY_NAMES.put("NZD", "Dólares Neozelandeses");
        CURRENCY_NAMES.put("MXN", "Pesos Mexicanos");
        CURRENCY_NAMES.put("SGD", "Dólares de Singapur");
        CURRENCY_NAMES.put("HKD", "Dólares de Hong Kong");
        CURRENCY_NAMES.put("NOK", "Coronas Noruegas");
        CURRENCY_NAMES.put("KRW", "Won Surcoreano");
        CURRENCY_NAMES.put("TRY", "Liras Turcas");
        CURRENCY_NAMES.put("INR", "Rupias Indias");
        CURRENCY_NAMES.put("RUB", "Rublos Rusos");
        CURRENCY_NAMES.put("BRL", "Reales Brasileños");
        CURRENCY_NAMES.put("ZAR", "Rand Sudafricano");
        CURRENCY_NAMES.put("PEN", "Soles Peruanos");

    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        boolean continuar = true; // Variable para controlar la repetición
        while (continuar) {
            String baseCurrency;
            do {
                // Inicio de interacción con el usuario
                System.out.println("\n********************************************");
                System.out.println("\nIngrese la moneda a convertir: \n");
                int i = 1;
                for (String currency : AVAILABLE_CURRENCIES) {
                    if (i % 3 != 0) {
                        System.out.printf(" %2d. %-4s - %-25s", i, currency, CURRENCY_NAMES.get(currency));
                    } else {
                        System.out.printf(" %2d. %-4s - %-25s\n", i, currency, CURRENCY_NAMES.get(currency));
                    }
                    i++;
                }

                baseCurrency = scanner.nextLine().toUpperCase();
                if (!AVAILABLE_CURRENCIES.contains(baseCurrency)) {
                    System.out.println("Moneda ingresada no válida. Por favor, ingrese una moneda de la lista.");
                }
            } while (!AVAILABLE_CURRENCIES.contains(baseCurrency));

            System.out.println("--------------------------------------------");
            System.out.println("Ha seleccionado la moneda " + baseCurrency + " - " + CURRENCY_NAMES.get(baseCurrency));
            System.out.println("--------------------------------------------");

            System.out.println("\nElija el número de la opción que desea convertir:");

            //Opción de monedas disponibles, se visualizará en 05 filas  y 04 columnas

            int totalCurrencies = AVAILABLE_CURRENCIES.size() - 1; // Excluye la moneda base
            int currenciesPerColumn = (totalCurrencies + 1) / 10; // Redondea hacia arriba

            int columnCounter = 0;
            for (String currency : AVAILABLE_CURRENCIES) {
                if (!currency.equals(baseCurrency)) {
                    if (columnCounter % currenciesPerColumn == 0) {
                        System.out.println();
                    }
                    System.out.printf("%2d. --> %-25s ", columnCounter + 1, CURRENCY_NAMES.get(currency));
                    columnCounter++;
                }
            }

            System.out.println("\n\nIngrese el número de la opción de conversión:");
            int selectedOption;
            while (true) {
                if (scanner.hasNextInt()) {
                    selectedOption = scanner.nextInt();
                    if (selectedOption < 1 || selectedOption > totalCurrencies) {
                        System.out.println("Opción no válida. Por favor, ingrese un número válido.");
                    } else {
                        break;
                    }
                } else {
                    System.out.println("Entrada no valida. Por favor, ingrese un número de la lista.");
                    scanner.next(); //Limpia el bufeer del scanner
                }
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
            System.out.println("Ha seleccionado convertir de " + CURRENCY_NAMES.get(baseCurrency) + " a " + CURRENCY_NAMES.get(targetCurrency));
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
                    System.out.printf("La cantidad convertida es: %.2f %s\n", convertedAmount, CURRENCY_NAMES.get(targetCurrency));
                    System.out.println("++++++++++++++++++++++++++++++++++++++++++++");

                } else {
                    System.out.println("No se encontró la tasa para " + CURRENCY_NAMES.get(targetCurrency));
                }
            } else {
                System.out.println("Error al obtener las tasas de cambio.");
            }

            //Limpia
            scanner.nextLine();

            // Menu de opción de salida
            System.out.println("\n ¿Desea realizar otra operación?");
            System.out.println("1. Si");
            System.out.println("2. No");
            System.out.println("Ingrese su opción: ");

            int opcionMenu;
            while (true) {
                if (scanner.hasNextInt()) {
                    opcionMenu = scanner.nextInt();
                    scanner.nextLine();
                    if (opcionMenu == 1 || opcionMenu == 2) {
                        break;
                    } else {
                        System.out.println("Opción no valida. POr favor ingrese 1 para continuar o 2 para salir");
                    }
                } else {
                    System.out.println("Entrada no valida. Por favor ingrese 1 para continuar o 2 para salir.");
                    scanner.next(); // Limpia
                }
            }
            if (opcionMenu != 1) {
                continuar = false;
            }
        }
            scanner.close();

            System.out.println("\nGracias por usar nuestros servicios");
        }
    }

