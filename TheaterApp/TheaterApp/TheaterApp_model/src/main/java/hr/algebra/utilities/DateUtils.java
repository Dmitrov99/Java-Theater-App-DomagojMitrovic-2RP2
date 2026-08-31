package hr.algebra.utilities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

    public final class DateUtils {

        private static final DateTimeFormatter CROATIAN_DATE_FORMAT =
                DateTimeFormatter.ofPattern("dd.MM.yyyy.");

        private DateUtils() {
        }

        public static String formatCroatianDate(LocalDate date) {
            if (date == null) {
                return "";
            }

            return date.format(CROATIAN_DATE_FORMAT);
        }

        public static String calculateSeason(LocalDate date) {
            if (date == null) {
                return "";
            }

            int year = date.getYear();

            if (date.getMonthValue() >= 9) {
                return year + "/" + (year + 1);
            }

            return (year - 1) + "/" + year;
        }
    }
