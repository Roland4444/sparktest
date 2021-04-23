package util.JSON;

public class Beatyfulizer {
    public Beatyfulizer(){

    };

    public static String trimAddress(String input){
        System.out.println("CHARS:: "+input.charAt(input.length() - 1));
        if (input.charAt(input.length() - 1) != '/') {
            return input;
        }
        return input.substring(0, input.length()-1);
     //   return input;
    };

    public static String compareundschoneJSON(ParcedJSON input, ParcedJSON compare){
        String styleinject = "  style=\"background-color: yellow;\"";
        if ((input==null) || (compare==null) )
            return schoneJSON(input);
        if (input.Brutto.equals("0.00"))
            return ("<p class=\"declined\">Позиция удалена</p>");
        StringBuilder result = new StringBuilder();
        if ((input.Date ==null) || input.Date.equals(compare.Date))
            result.append("<p>Дата: "+ input.Date +   "<br>");
        else
            result.append("<p"+ styleinject +">Дата: "+ input.Date +   "<br>");

        if ((input.Time ==null) || input.Time.equals(compare.Time))
            result.append("<p>Время: "+ input.Time +   "<br>");
        else
            result.append("<p"+ styleinject +">Время: "+ input.Time +   "<br>");

        if ((input.Waybill_number ==null) || input.Waybill_number.equals(compare.Waybill_number))
            result.append("<p>Номер накладной: "+ input.Waybill_number +   "<br>");
        else
            result.append("<p"+ styleinject +">Номер накладной: "+ input.Waybill_number +   "<br>");

        if ((input.Metall ==null) || input.Metall.equals(compare.Metall))
            result.append("<p>Металл: "+ input.Metall +   "<br>");
        else
            result.append("<p"+ styleinject +">Металл: "+ input.Metall +   "<br>");

        if ((input.Tara ==null) || input.Tara.equals(compare.Tara))
            result.append("<p>Тара: "+ input.Tara +   "<br>");
        else
            result.append("<p"+ styleinject +">Тара: "+ input.Tara +   "<br>");


        if ((input.Netto ==null) || input.Netto.equals(compare.Netto))
            result.append("<p>Нетто: "+ input.Netto +   "<br>");
        else
            result.append("<p"+ styleinject +">Нетто: "+ input.Netto +   "<br>");

        if ((input.Brutto ==null) || input.Brutto.equals(compare.Brutto))
            result.append("<p>Брутто: "+ input.Brutto +   "<br>");
        else
            result.append("<p"+ styleinject +">Брутто: "+ input.Brutto +   "<br>");

        if ((input.Clogging ==null) || input.Clogging.equals(compare.Clogging))
            result.append("<p>Засор: "+ input.Clogging +   "<br>");
        else
            result.append("<p"+ styleinject +">Засор: "+ input.Clogging +   "<br>");

        if ((input.Trash ==null) || input.Trash.equals(compare.Trash))
            result.append("<p>Примесь: "+ input.Trash +   "<br>");
        else
            result.append("<p"+ styleinject +">Примесь: "+ input.Trash +   "<br>");

        if ((input.Comment ==null) || input.Comment.equals(compare.Comment))
            result.append("<p>Комментарий: "+ input.Comment +   "<br>");
        else
            result.append("<p"+ styleinject +">Комментарий: "+ input.Comment +   "<br>");
        return result.toString();
    };

    public static String schoneJSON(ParcedJSON input){
        if (input==null)
            return "";
        StringBuilder result = new StringBuilder();
        result.append("<p>Дата: "+ input.Date +   "<br>");
        result.append("<p>Время: "+ input.Time +   "<br>");
        result.append("<p>Номер накладной: "+ input.Waybill_number +    "<br>");
        result.append("<p>Металл: "+ input.Metall +    "<br>");
        result.append("<p>Тара: "+ input.Tara +    "<br>");
        result.append("<p>Нетто: "+ input.Netto +   "<br>");
        result.append("<p>Брутто: "+ input.Brutto +   "<br>");
        result.append("<p>Засор: "+ input.Clogging +   "<br>");
        result.append("<p>Примесь: "+ input.Trash +   "<br>");
        result.append("<p>Комментарий: "+ input.Comment +   "<br>");
 /*       result.append("Время: "+ input.Time +   "<br>");
        result.append("Номер накладной: "+ input.Waybill_number +    "<br>");
        result.append("Металл: "+ input.Metall +    "<br>");
        result.append("Тара: "+ input.Tara +    "<br>");
        result.append("Нетто: "+ input.Netto +   "<br>");
        result.append("Брутто: "+ input.Brutto +   "<br>");
        result.append("Засор: "+ input.Clogging +   "<br>");
        result.append("Примесь: "+ input.Trash +   "<br>");  */
        return result.toString();
    }
}
