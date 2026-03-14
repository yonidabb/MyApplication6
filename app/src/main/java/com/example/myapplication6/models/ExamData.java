package com.example.myapplication6.models;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ExamData {

    public static class ExamModel {
        public String title;
        public String description;
        public boolean shuffleQuestions;
        public boolean shuffleOptions;
        public List<QuestionModel> questions = new ArrayList<>();
    }

    public static class QuestionModel {
        public String id;
        public String question;
        public List<OptionModel> options = new ArrayList<>();
        public String correctOptionId;
        public int score;
        public String hint; // optional

        // runtime
        public String selectedOptionId;
    }

    public static class OptionModel {
        public String id;   // "A"/"B"/"C"/"D"
        public String text;
    }

    public static class ExamParser {
        public static ExamModel parse(String json) throws Exception {
            JSONObject root = new JSONObject(json);
            JSONObject examObj = root.getJSONObject("exam");

            ExamModel exam = new ExamModel();
            exam.title = examObj.optString("title", "מבחן");
            exam.description = examObj.optString("description", "");
            exam.shuffleQuestions = examObj.optBoolean("shuffleQuestions", false);
            exam.shuffleOptions = examObj.optBoolean("shuffleOptions", false);

            JSONArray questionsArr = examObj.getJSONArray("questions");
            for (int i = 0; i < questionsArr.length(); i++) {
                JSONObject qObj = questionsArr.getJSONObject(i);

                QuestionModel q = new QuestionModel();
                q.id = qObj.optString("id", "q" + (i + 1));
                q.question = qObj.getString("question");
                q.correctOptionId = qObj.getString("correctOptionId");
                q.score = qObj.optInt("score", 0);
                q.hint = qObj.optString("hint", null);

                JSONArray optionsArr = qObj.getJSONArray("options");
                for (int j = 0; j < optionsArr.length(); j++) {
                    JSONObject oObj = optionsArr.getJSONObject(j);
                    OptionModel o = new OptionModel();
                    o.id = oObj.getString("id");
                    o.text = oObj.getString("text");
                    q.options.add(o);
                }

                exam.questions.add(q);
            }

            return exam;
        }
    }
}
