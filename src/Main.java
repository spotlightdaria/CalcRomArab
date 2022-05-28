import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите одной строкой арифметческую операцию с 2-мя числами в формате с арабскми числами \"3 + 2\" или римскими числами \"I + V\"" +
                " \nЧисла должны быть в диапазоне от 1 до 10");

        while (true){
            String s = scanner.nextLine();
            System.out.println(calc(s));
        }
    }
    public static String calc(String input){
        String [] strings = input.split(" ");

        //задаем паттерны формата ввода чисел и знака операции
        Pattern arabic = Pattern.compile("\\d+");
        Pattern rom = Pattern.compile("[A-Z]+");
        Pattern oper = Pattern.compile("[+]?[-]?[*]?[/]?");


        // ловим ситуацию, когда ввели неверное кол-во смволов
        if (strings.length != 3)
            throw new IllegalArgumentException("Вы ввели данные в неверном формате");


        Matcher rm1 = rom.matcher(strings[0]);
        boolean isrm1 = rm1.matches();
        Matcher rm2 = rom.matcher(strings[2]);
        boolean isrm2 = rm2.matches();
        Matcher am1 = arabic.matcher(strings[0]);
        boolean isam1 = am1.matches();
        Matcher am2 = arabic.matcher(strings[2]);
        boolean isam2 = am2.matches();

        Matcher om = oper.matcher(strings[1]);
        boolean isom = om.matches();

        // ловим неверную операцию
        if (!isom){
            throw new IllegalArgumentException("Вы ввели неверный знак операци");
        }

        // ловим, когда числа арабске и римские одновременно
        if ((isam1&&isrm2) || (isam2&&isrm1)){
            throw  new IllegalArgumentException("Вы ввели арабские и римские числа одновременно");
        }

        // ловим, когда неверно введенные числа (не по шаблону)
        if ( !(isam1&&isam2) && !(isrm1&&isrm2)){
            throw new IllegalArgumentException("Вы ввели числа в неверном формате");
        }

        int a = 0; // вспомогательная переменная
        int b = 0; // вспомогательная переменная
        String result = null;

        //переводим римские числа в арабские для вычисления

        if (isrm1 && isrm2){
            a = romanToArabic(strings[0]);
            b = romanToArabic(strings[2]);
            if (a<1||a>10||b<1||b>10){
                throw new IllegalArgumentException("Вы ввели числа вне диапазона 1-10");
            }
        }

        //переводм введеные строки в арабские числа
        if (isam1 && isam2){
            a = Integer.parseInt(strings[0]);
            b = Integer.parseInt(strings[2]);
            if (a<1||a>10||b<1||b>10){
                throw new IllegalArgumentException("Вы ввели числа вне диапазона 1-10");
            }
        }
        int res = 0;  // вспомогательная переменная

        // сами операции
        if (strings[1].equals("+")){
            res = a+b;
        }
        if (strings[1].equals("-")){
            res = a-b;
        }
        if (strings[1].equals("*")){
            res = a*b;
        }
        if (strings[1].equals("/")){
            res = a/b;
        }

        // перевод результата вычисления в римский формат
        if (isrm1&&isrm2){
            if (res <= 0){
                throw new IllegalArgumentException("Результат вычсления римских чисел меньше или равно 0");
            }
            else result = arabicToRoman(res);
        }

        // перевод числового результата в строку
        if (isam1&&isam2){
            result = Integer.toString(res);
        }
        return result;
    }


    // метод преобразования чисел из римских в арабские
    public static int romanToArabic(String input) {
        String romanNumeral = input;
        int result = 0;
        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();
        int i = 0;
        while ((romanNumeral.length() > 0) && (i < romanNumerals.size())) {
            RomanNumeral symbol = romanNumerals.get(i);
            if (romanNumeral.startsWith(symbol.name())) {
                result += symbol.getValue();
                romanNumeral = romanNumeral.substring(symbol.name().length());
            } else {
                i++;
            }
        }
        if (romanNumeral.length() > 0) {
            throw new IllegalArgumentException("неверный формат римских чисел");
        }
        return result;
    }

    // метод преобразования чисел из арабские в римские
    public static String arabicToRoman(int number) {
        List<RomanNumeral> romanNumerals = RomanNumeral.getReverseSortedValues();
        int i = 0;
        StringBuilder sb = new StringBuilder();
        while ((number > 0) && (i < romanNumerals.size())) {
            RomanNumeral currentSymbol = romanNumerals.get(i);
            if (currentSymbol.getValue() <= number) {
                sb.append(currentSymbol.name());
                number -= currentSymbol.getValue();
            } else {
                i++;
            }
        }
        return sb.toString();
    }
}

