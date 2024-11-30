package com.studyorganizer.googleschedule.services;

import com.google.api.services.docs.v1.model.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GoogleDocsService {

    public List<Request> createTextRequest(String text, int index, boolean bold, int fontSize, boolean italic, boolean underline, String alignment) {
        List<Request> requests = new ArrayList<>();

        requests.add(new Request()
                .setInsertText(new InsertTextRequest()
                        .setText(text)
                        .setLocation(new Location().setIndex(index))));

        TextStyle textStyle = new TextStyle()
                .setBold(bold)
                .setItalic(italic)
                .setUnderline(underline)
                .setFontSize(new Dimension()
                        .setMagnitude((double) fontSize)
                        .setUnit("PT"));

        requests.add(new Request().setUpdateTextStyle(new UpdateTextStyleRequest()
                .setTextStyle(textStyle)
                .setFields("bold,italic,underline,fontSize")
                .setRange(new Range()
                        .setStartIndex(index)
                        .setEndIndex(index + text.length()))));

        if (alignment != null && !alignment.isEmpty()) {
            requests.add(new Request().setUpdateParagraphStyle(new UpdateParagraphStyleRequest()
                    .setParagraphStyle(new ParagraphStyle().setAlignment(alignment))
                    .setFields("alignment")
                    .setRange(new Range()
                            .setStartIndex(index)
                            .setEndIndex(index + text.length()))));
        }

        return requests;
    }

    public List<Request> createNumberedList(List<List<String>> items, int startIndex,Boolean setOnline, Boolean setTopic) {
        List<Request> requests = new ArrayList<>();
        int index = 1;
        for (List<String> item : items) {

            String content = index + ".\t" + "Дата: " + item.get(0) + "\n\t" +
                    "Час: " + item.get(1) + "\n\t" +
                    "Дисципліна: " + item.get(2) + "\n\t";
            if (setTopic) content += "Тема: ..." + "\n\t";
            if (setOnline) content += "Місце проведення: " + item.get(3) + "\n\n";

            requests.addAll(createTextRequest(content, startIndex, false, 12, false, false, "START"));

            index++;
            startIndex += content.length();
        }
        return requests;
    }

    public int calculateTotalLength(List<List<String>> items,Boolean setOnline, Boolean setTopic) {
        int totalLength = 0;
        for (List<String> item : items) {
            String content = "Дата: " + item.get(0) + "\n\t" +
                    "Час: " + item.get(1) + "\n\t" +
                    "Дисципліна: " + item.get(2) + "\n\t";
            if (setTopic) content += "Тема: ..." + "\n\t";
            if (setOnline) content += "Місце проведення: " + item.get(3) + "\n\n";
            totalLength += content.length() + 3;
        }
        return totalLength;
    }

    public String getLessonTime(int order){
        return switch (order) {
            case 1 -> "8:20";
            case 2 -> "9:50";
            case 3 -> "11:30";
            case 4 -> "13:00";
            case 5 -> "14:40";
            case 6 -> "16:10";
            default -> "";
        };
    }

    public String getLessonType(String type){
        return switch (type) {
            case "LECTURE" -> "Лекційне заняття";
            case "LABORATORY" -> "Лабораторне заняття";
            case "PRACTICE" -> "Практичне заняття";
            default -> "";
        };
    }
}
