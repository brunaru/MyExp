package br.usp.myexp.log;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import br.usp.myexp.Util;

public class Logger {

    /* Events constants. */
    public static final String BUTTON_CLICK_EVENT = "button_click";

    public static final String RADIO_CLICK_EVENT = "radio_click";

    public static final String CHECKBOX_CLICK_EVENT = "checkbox_click";

    public static final String TEXT_CLICK_EVENT = "text_click";

    public static final String EDIT_TEXT_CLICK_EVENT = "edit_text_click";

    public static final String BACK_CLICK_EVENT = "back_click";
    
    public static final String DIALOG_CLICK_EVENT = "dialog_click";
    
    public static final String LAYOUT_CLICK_EVENT = "layout_click";

    /* CSV record separator */
    private static final String NEW_LINE_SEPARATOR = "\n";
    /* CSV file header */
    private static final Object[] FILE_HEADER = { "questionnaireId", "time", "event", "questionNumber", "viewText", "answer" };

    public void logQuestionnaireEvent(QuestionnaireEvent event) {
        try {
            File dir = Util.getDir();
            String today = Util.getTodayDateForFileName();
            String fileName = dir + File.separator + event.getQuestionnaireId() + "_" + today + ".csv";
            File file = new File(fileName);
            boolean isANewFile = !file.exists();
            FileWriter fileWriter = new FileWriter(file, true);

            CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator(NEW_LINE_SEPARATOR);
            CSVPrinter csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);

            if (isANewFile) {
                csvFilePrinter.printRecord(FILE_HEADER);
            }

            List<String[]> data = new ArrayList<String[]>();
            data.add(new String[] { event.getQuestionnaireId(), event.getTime(), event.getEvent(), event.getQuestionNumber(), event.getViewText(), event.getAnswer() });
            csvFilePrinter.printRecords(data);

            fileWriter.flush();
            fileWriter.close();
            csvFilePrinter.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
