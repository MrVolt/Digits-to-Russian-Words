package com.weebly.junior_freelancer;

import java.util.ArrayList;

/**
 * Created by Voltor on 03.03.2016.
 */
public class NumberUtils {
    private static boolean isThousand;

    private static String returnSuffixForThousand(String number) {
        number = removeStartedZeros(number);

        if (number.length() == 1) return unitsSuffixForThousand(number);

        if (number.length() == 3) number = number.substring(1, number.length());

        if (number.startsWith("1")) return Constants.EMPTY_STRING;

        return unitsSuffixForThousand(number);
    }

    private static String unitsSuffixForThousand(String number) {
        int lastDigits = Integer.parseInt(number.substring(number.length() - 1));

        switch (lastDigits)
        {
            case 1:
                return Constants.A_SUFFIX;
            case 2:
            case 3:
            case 4:
                return Constants.I_SUFFIX;
            default:
                return Constants.EMPTY_STRING;
        }
    }

    private static String returnSuffixForOverMillion(String number) {
        number = removeStartedZeros(number);

        if (number.length()== 1) return unitsSuffixForOverThousand(number);

        if (number.length() == 3) number = number.substring(1, number.length());
        if (number.startsWith("1")) return Constants.OV_SUFFIX;

        return unitsSuffixForOverThousand(number);
    }

    private static String unitsSuffixForOverThousand(String number) {
        int lastDigit = Integer.parseInt(number.substring(number.length() - 1));

        switch (lastDigit)
        {
            case 1:
                return Constants.EMPTY_STRING;
            case 2:
            case 3:
            case 4:
                return Constants.A_SUFFIX;
            default:
                return Constants.OV_SUFFIX;
        }
    }

    public static String prepareString(String number) {
        if (Integer.parseInt(number) == 0)
        {
            return Units.ноль.toString();
        }

        String preparedString = Constants.EMPTY_STRING;

        ArrayList<String> threes = splitOnThrees(number);

        for (int i=threes.size(); i>0; i--)
        {
            String numeric = removeStartedZeros(threes.get(i - 1));
            if (numeric.equals("0")) continue;

            setThousandFlag(i);

            if (preparedString.equals("")) preparedString += convertNumericToString(numeric);
            else preparedString += " " + convertNumericToString(numeric);

            switch (i) {
                //От единицы до сотни не выводим порядковое название
                case 1:
                    break;

                case 2:
                    preparedString += " " + Order.values()[i - 1].toString() +
                            returnSuffixForThousand(numeric);
                    break;

                default:
                    preparedString += " " + Order.values()[i - 1].toString() +
                            returnSuffixForOverMillion(numeric);
            }
        }

        return preparedString;
    }

    //У тысяч помимо самого названия могут менятся так же и числовые, вместо один - одна.
    private static void setThousandFlag(int order) {
        isThousand = order == 2;
    }

    private static String convertNumericToString(String numeric) {
        String convertedNumeric = Constants.EMPTY_STRING;

        switch (numeric.length())
        {
            case 1:
                return getUnits(Units.values()[Integer.parseInt(numeric)]);

            case 2:
                return getTens(numeric);

            case 3:
                return getHundreds(numeric, convertedNumeric);
        }
        return null;
    }

    private static String getHundreds(String numeric, String convertedNumeric) {
        if (!numeric.substring(0,1).equals("0")) {
            convertedNumeric = Hundreds.values()[Integer.parseInt(numeric.substring(0, 1)) - 1].toString();
        }

        String tens = getTens(numeric.substring(1));
        if (!tens.equals("")) convertedNumeric += " " + tens;

        return convertedNumeric;
    }

    private static String getTens(String numeric) {
        String preparedTens = Constants.EMPTY_STRING;

        if (numeric.startsWith("1") && !numeric.substring(1).startsWith("0"))
            return FromTenToTwenty.values()[Integer.parseInt(numeric.substring(1))-1].toString();

        else {
            if (!numeric.substring(0,1).equals("0")) {
                preparedTens = Tens.values()[Integer.parseInt(numeric.substring(0, 1)) - 1].toString();
            }

            if (!numeric.substring(1).equals("0")) {
                String units = getUnits(Units.values()[Integer.parseInt(numeric.substring(1))]);
                if (!units.equals("")) preparedTens += " " + units;
            }

            return preparedTens;
        }
    }

    private static String getUnits(Units units) {
        if (!units.equals(Units.ноль)) {
            if (isThousand)
            {
                switch (units)
                {
                    case один:
                        return UniqueThousandUnits.одна.toString();
                    case два:
                        return UniqueThousandUnits.две.toString();
                }
            }

            return units.toString();
        }
        else return Constants.EMPTY_STRING;
    }

    //Разбиваем число по три символа (на тройки)
    private static ArrayList<String> splitOnThrees(String number) {
        ArrayList<String> threes = new ArrayList<String>();
        String restString = number;

        for (int i=number.length(); i>0; i=i-3) {
            if (restString.length() > 2) {
                threes.add(number.substring(i-3, i));
                restString = restString.substring(0, i-3);
            }
            else {
                threes.add(restString);
            }
        }

        return threes;
    }

    public static String removeStartedZeros(String number) {
        if (number.length() > 1 && number.startsWith("0")) {
            do {
                number = number.substring(1, number.length());
            }
            while (number.startsWith("0") && number.length() > 1);
        }
        return number;
    }
}
